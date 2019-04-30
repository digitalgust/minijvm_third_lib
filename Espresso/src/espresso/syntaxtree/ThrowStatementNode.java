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

import espresso.Espresso;

import espresso.classfile.classgen.ATHROW;
import espresso.classfile.classgen.ClassGen;
import espresso.classfile.classgen.MethodGen;

import espresso.util.ClassType;
import espresso.util.Type;
import espresso.util.ErrorMsg;
import espresso.util.SymbolTable;
import espresso.util.ExceptionStack;
import espresso.util.TypeCheckError;

public class ThrowStatementNode extends StatementNode {

    public ExpressionNode expression_d;
   
    public ThrowStatementNode() {
	super();
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	
	Type type_d = expression_d.typeCheck(stable);

	if (!type_d.throwable())
	    throw new TypeCheckError
		(new ErrorMsg(ErrorMsg.UNTHRWBL_ERR,
			      type_d.name(),
			      this));
	
	// Get ExceptionStack and push type_d onto it
	ExceptionStack es = Espresso.exceptionStack();
	es.add_throw(type_d,this);

	return Type.Void;
    }	

    public void translate(ClassGen classGen, MethodGen methodGen) {
	expression_d.translate(classGen, methodGen);
	methodGen.getInstructionList().append(new ATHROW());
    }	
}
