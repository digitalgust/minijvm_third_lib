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

public class IfStatementNode extends StatementNode {

    public ExpressionNode condition_d;
	public StatementNode thenStatement_d;
	public StatementNode elseStatement_d;			// may be null

	public IfStatementNode() {
		super();
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type ctype = condition_d.typeCheck(stable);
		
		if (ctype instanceof BooleanType) {
			thenStatement_d.typeCheck(stable);
			if (elseStatement_d != null) {
				elseStatement_d.typeCheck(stable);
			}
			return Type.Void;
		}
			
		throw new TypeCheckError(this);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		BigDecimal value = condition_d.evaluateExp();
		if (value != null) {
			condition_d = new BooleanLiteral(value);
		}
		condition_d.translate(classGen, methodGen);
				
		InstructionHandle falsec = null;
		InstructionHandle truec = il.getEnd();			// remember end of condition

		thenStatement_d.translate(classGen, methodGen);	
		
		if (elseStatement_d != null) {
			BranchHandle te = il.append(new GOTO(null));
			falsec = il.getEnd();
			elseStatement_d.translate(classGen, methodGen);	
			falsec = falsec.getNext();			
			te.setTarget(il.append(new NOP()));
		}
		else {
			falsec = il.append(new NOP());
		}

		ExpressionNode.backPatch(condition_d.trueList_d, truec.getNext());
		ExpressionNode.backPatch(condition_d.falseList_d, falsec);
	}
	
}
