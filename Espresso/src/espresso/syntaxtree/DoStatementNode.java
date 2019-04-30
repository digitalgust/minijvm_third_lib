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

public class DoStatementNode extends StatementNode {

	public StatementNode statement_d;
    public ExpressionNode condition_d;

	public DoStatementNode() {
		super();
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type ctype = condition_d.typeCheck(stable);
		
		if (ctype instanceof BooleanType) {
			statement_d.typeCheck(stable);
			return Type.Void;
		}
			
		throw new TypeCheckError(this);
	}

	/**
	  * Translation:	do s while b
	  *
	  *			1: s
	  *			   b	[truelist := 1, falselist := 2]
	  *			   goto 1
	  *			2: nop
	  */
	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		
		InstructionHandle cstart = il.getEnd();
		statement_d.translate(classGen, methodGen);	

		BigDecimal value = condition_d.evaluateExp();
		if (value != null) {
			condition_d = new BooleanLiteral(value);
		}
		condition_d.translate(classGen, methodGen);
		il.append(new GOTO(cstart.getNext()));

		ExpressionNode.backPatch(condition_d.trueList_d, cstart.getNext());

		if (condition_d.falseList_d != null) {
			InstructionHandle eh = il.append(new NOP());		// jump out of the loop
			ExpressionNode.backPatch(condition_d.falseList_d, eh);
		}
	}

}
