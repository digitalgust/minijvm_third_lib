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
import espresso.util.MethodType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class RelationalExpNode extends ExpressionNode {

	public int operator_d;
	public ExpressionNode left_d;
	public ExpressionNode right_d;

	public RelationalExpNode(ExpressionNode left, ExpressionNode right, int operator) {
		left_d = left;
		right_d = right;
		operator_d = operator;
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

	private void notOperator() {
		switch (operator_d) {
		case Operators.LT:
			operator_d = Operators.GT_EQ;
		break;
		case Operators.GT:
			operator_d = Operators.LT_EQ;
		break;
		case Operators.LT_EQ:
			operator_d = Operators.GT;
		break;
		case Operators.GT_EQ:
			operator_d = Operators.LT;
		break;		
		default:
			Espresso.internalError();
		}
	}

	private void swapOperands() {	
		ExpressionNode temp = left_d; left_d  = right_d; right_d = temp;
		notOperator();	
	}
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		BranchInstruction ifFalse = null;
		BigDecimal value = left_d.evaluateExp();
		InstructionList il = methodGen.getInstructionList();

		if (value != null) {
			swapOperands();			// left is a constant exp
		} else {
			value = right_d.evaluateExp();
		}
				
		left_d.translate(classGen, methodGen);
		
		Type tleft = left_d.type();	
		boolean tozero = (value != null && tleft.oneWord() && value.intValue() == 0);
		if (!tozero) {
			right_d.translate(classGen, methodGen);
		}

		notOperator();				// true list falls through
	
		if (tleft.twoWords()) {
			boolean less = (operator_d == Operators.LT || operator_d == Operators.LT_EQ);
			il.append(tleft.CMP(less));		// less is used to choose DCMPG or DCMPL
			tleft = Type.Int; tozero = true; 
		}

		switch (operator_d) {
		case Operators.GT:
			ifFalse = (BranchInstruction) tleft.IFGT(tozero);
		break;
		case Operators.LT:
			ifFalse = (BranchInstruction) tleft.IFLT(tozero);
		break;
		case Operators.GT_EQ:
			ifFalse = (BranchInstruction) tleft.IFGE(tozero);
		break;
		case Operators.LT_EQ:
			ifFalse = (BranchInstruction) tleft.IFLE(tozero);
		break;
		default:
			Espresso.internalError();
		}

		addFalseList(il.append(ifFalse));
	}
	
}
