/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 2/03/98
 *
 */

package espresso.syntaxtree;

import espresso.util.Type;
import espresso.util.MethodType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class StatementExpNode extends StatementNode {

	public ExpressionNode expression_d;
	 
	public StatementExpNode() {
		super();
  	}	

	public StatementExpNode(ExpressionNode expr ) {
		super();
		expression_d = expr;
  	}	

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		expression_d.typeCheck(stable);
		return Type.Void;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		expression_d.translate(classGen, methodGen);

		/*
		 * If the expression is a method call we need to discard the
		 * returned value.
		 */		
		if (expression_d instanceof MethodExpNode) {
			MethodExpNode mnode = (MethodExpNode) expression_d;
			InstructionList il = methodGen.getInstructionList();
			MethodType mtype = (MethodType) mnode.callee_d.type();
			il.append(mtype.resultType().POP());		// discard the result
		}
	}	

}
