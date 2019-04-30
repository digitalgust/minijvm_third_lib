/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 1/23/98
 *
 */

package espresso.syntaxtree;

import espresso.util.Type;
import espresso.util.MethodType;
import espresso.util.Operators;
import espresso.util.NotConstant;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class UnaryPlusExpNode extends ExpressionNode {

	public ExpressionNode left_d;

	public UnaryPlusExpNode(ExpressionNode left) {
		left_d = left;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		return left_d.evaluate(stable);
	}		
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type tleft  = left_d.typeCheck(stable);

		MethodType ptype = lookupPrimop(stable, Operators.UN_PS, 
			new MethodType(Type.Void, tleft));		// defined in ExpressionNode
		
		if (ptype != null) {

			// Add the appropriate casts to the AST
				
			Type arg1 = (Type) ptype.argsType().elementAt(0);
			if (!arg1.identicalTo(tleft)) {
				left_d = new CastExpNode(left_d, arg1);
			}
				
			type_d = ptype.resultType();
			return type_d;
		}
		 
		throw new TypeCheckError(this);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		left_d.translate(classGen, methodGen);		
	}

	
}
