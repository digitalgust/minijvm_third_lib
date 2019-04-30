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

import espresso.Espresso;
import espresso.util.Type;
import espresso.util.LongType;
import espresso.util.Operators;
import espresso.util.MethodType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class ShiftExpNode extends ExpressionNode {

	public int operator_d;
	public ExpressionNode left_d;
	public ExpressionNode right_d;

	public ShiftExpNode(ExpressionNode left, ExpressionNode right, int operator) {
		left_d = left;
		right_d = right;
		operator_d = operator;
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type tleft  = left_d.typeCheck(stable);
		Type tright = right_d.typeCheck(stable);

		if (tright instanceof LongType) {
			tright = Type.Int;
			right_d = new CastExpNode(right_d, tright);
		}

		MethodType ptype = lookupPrimop(stable, operator_d, new MethodType(Type.Void, 
			tleft, tright));		// defined in ExpressionNode
		
		if (ptype != null) {

			// Add the appropriate casts to the AST
				
			Type arg1 = (Type) ptype.argsType().elementAt(0);
			if (!arg1.identicalTo(tleft)) {
				left_d = new CastExpNode(left_d, arg1);
			}
			Type arg2 = (Type) ptype.argsType().elementAt(1);
			if (!arg2.identicalTo(tright)) {
				right_d = new CastExpNode(right_d, arg1);				
			}
				
			type_d = ptype.resultType();
			return type_d;
		}
		 
		throw new TypeCheckError(this);
	}
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		left_d.translate(classGen, methodGen);
		right_d.translate(classGen, methodGen);

		Type tleft = left_d.type();
		Instruction op = null;

		switch (operator_d) {
		case Operators.SL:
			op = tleft.SHL();
		break;
		case Operators.SR:
			op = tleft.SHR();
		break;
		case Operators.SS:
			op = tleft.USHR();
		break;
		default:
			Espresso.internalError();
		}

		il.append(op);
	}

}
