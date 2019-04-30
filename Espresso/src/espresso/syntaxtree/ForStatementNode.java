/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 2/02/98
 *
 */

package espresso.syntaxtree;

import espresso.util.Type;
import espresso.util.BooleanType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class ForStatementNode extends StatementNode {

	public StatementNode statement_d;
    public StatementNode init_d = null;
	public ExpressionNode bound_d = null;
	public StatementNode update_d = null;

	public ForStatementNode() {
		super();
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	
		if (init_d != null) {
			init_d.typeCheck(stable);
		}		

		if (bound_d != null) {
			Type tbound = bound_d.typeCheck(stable);
			if (!(tbound instanceof BooleanType)) {
				throw new TypeCheckError(this);
			}
		}
		
		if (update_d != null) {
			update_d.typeCheck(stable);
		}

		statement_d.typeCheck(stable);				
		return Type.Void;		
	}

	/**
	  * Translation:	for (i; b; u) s
	  *
	  *			   i	
	  *			1: b	[truelist := 2, falselist := 3]
	  *			2: s
	  *			   u
	  *			   goto 1
	  *			3: nop
	  */
	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		if (init_d != null) {
			init_d.translate(classGen, methodGen);
		}

		InstructionHandle bstart = il.getEnd();
		if (bound_d != null) {
			BigDecimal value = bound_d.evaluateExp();
			if (value != null) {
				bound_d = new BooleanLiteral(value);
			}
			bound_d.translate(classGen, methodGen);
		}
		else {
			il.append(new NOP());		// need a place to jump back
		}

		InstructionHandle sstart = il.getEnd();
		statement_d.translate(classGen, methodGen);

		if (update_d != null) {
			update_d.translate(classGen, methodGen);
		}

		il.append(new GOTO(bstart.getNext()));

		if (bound_d != null) {
			ExpressionNode.backPatch(bound_d.trueList_d, sstart.getNext());
			
			if (bound_d.falseList_d != null) {
				InstructionHandle eh = il.append(new NOP());		// jump out of the loop
				ExpressionNode.backPatch(bound_d.falseList_d, eh);
			}
		}
	}

}
