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
import espresso.util.MethodType;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.syntaxtree.MethodDeclarationNode;

import espresso.classfile.classgen.*;

import java.util.Vector;
import java.math.BigDecimal;

public class ReturnStatementNode extends StatementNode {

	public Type returnType_d;
    public ExpressionNode expression_d;

	/**
	  *	Gets the result type of the method in which this return statement 
	  * occurs (the last in the symbol table entry). This info is needed 
	  * for type checking.
	  */	
	public void fetchResultType(Symbol methodName, SymbolTable stable)
	{
		Vector methods = stable.lookupMethod(methodName);
		MethodDeclarationNode mnode = 
					(MethodDeclarationNode) methods.elementAt(methods.size()-1);
		returnType_d = ((MethodType) mnode.type_d).resultType();				
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type etype = null;
		if (expression_d == null) {
			etype = Type.Void;
		}
		else {
			etype = expression_d.typeCheck(stable);
		}
		if (!etype.subTypeOf(returnType_d)) {
			throw new TypeCheckError(this);
		}
		return Type.Void;
	}
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		BranchHandle gototc = null;
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		if (expression_d != null) {
			BigDecimal value = expression_d.evaluateExp();
			if (value != null) {
				il.append(expression_d.type().PUSH(cpg, value));
			}
			else {
				gototc = expression_d.translateSynthesized(classGen, methodGen);
			}
		}

		TryStatementNode.translateFinally(classGen,
						  methodGen,
						  returnType_d);
		
		InstructionHandle ret = il.append(returnType_d.RETURN());
		if (gototc != null) {
			gototc.setTargetNoCopy(ret);
		}
	}

}
