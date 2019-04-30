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
import espresso.util.Operators;
import espresso.util.NotConstant;
import espresso.util.MethodType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class MultiplicativeExpNode extends ExpressionNode {

	public int operator_d;
	public ExpressionNode left_d;
	public ExpressionNode right_d;

	public MultiplicativeExpNode(ExpressionNode left, ExpressionNode right, 
		int operator) 
	{
		left_d = left;
		right_d = right;
		operator_d = operator;
	}
		
	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		BigDecimal result = null;
		
		switch (operator_d) {
			case Operators.TS:
				result = left_d.evaluate(stable);
				result = result.multiply(right_d.evaluate(stable));
			break;
			case Operators.DV:
				result = left_d.evaluate(stable);
				result = result.divide(right_d.evaluate(stable), BigDecimal.ROUND_CEILING);
			break;
			case Operators.MD:
				BigDecimal vleft = left_d.evaluate(stable);
				BigDecimal vright = right_d.evaluate(stable);

				// Avoid loosing precision by converting longs to doubles
								
				if (vleft.scale() > 0 || vright.scale() > 0) {
					double vl = vleft.doubleValue();
					double vr = vright.doubleValue();
					result = new BigDecimal(vl % vr);
				}
				else {
					long vl = vleft.longValue();
					long vr = vright.longValue();
					result = new BigDecimal(vl % vr);
				}
			break;
			default:
				Espresso.internalError();
		}

		return result;
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

		Type tleft  = left_d.typeCheck(stable);
		Type tright = right_d.typeCheck(stable);

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
		Instruction in = null;

		left_d.translate(classGen, methodGen);
		right_d.translate(classGen, methodGen);
		
		switch (operator_d) {
		case Operators.TS:
			in = type_d.MUL();
		break;
		case Operators.DV:
			in = type_d.DIV();
		break;
		case Operators.MD:
			in = type_d.REM();
		break;
		default:
			Espresso.internalError();
		}		

		InstructionList il = methodGen.getInstructionList();
		il.append(in);
	}	
	
}
