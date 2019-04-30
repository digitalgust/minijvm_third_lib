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
import espresso.util.NotConstant;
import espresso.util.Operators;
import espresso.util.NullType;
import espresso.util.ClassType;
import espresso.util.Type;
import espresso.util.IntType;
import espresso.util.BooleanType;
import espresso.util.MethodType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.classfile.classgen.*;

import java.math.BigDecimal;
import java.util.Enumeration;

public class EqualityExpNode extends ExpressionNode {

	public int operator_d;
	public ExpressionNode left_d;
	public ExpressionNode right_d;

	public Type eqtype_d = null;			// type of the args

	public EqualityExpNode(ExpressionNode left, ExpressionNode right, 
		int operator) 
	{
		left_d = left;
		right_d = right;
		operator_d = operator;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		BigDecimal left = left_d.evaluate(stable);
		BigDecimal right = right_d.evaluate(stable);
		boolean equal = (operator_d == Operators.EQ_EQ);
		return new BigDecimal(left.equals(right) == equal ? "1" : "0");
	}		
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	
		Type tleft  = left_d.typeCheck(stable);
		Type tright = right_d.typeCheck(stable);

		if (tleft.referenceType() && tright.referenceType()) {
			if (tleft.relatedTo(tright)) {
				eqtype_d = tleft;				// a ref type
				type_d = Type.Boolean;
				return type_d;
			}
		}
		else {
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
				
				eqtype_d = (Type) ptype.argsType().elementAt(0);

				type_d = ptype.resultType();
				return type_d;
			}
		}
		
		throw new TypeCheckError(this);
	}

	private void swapOperands() {	
		ExpressionNode temp = left_d; 
		left_d = right_d; right_d = temp;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();		

		boolean tozero = false;
		Type tleft = left_d.type();	
		BranchInstruction ifFalse = null;
		BranchHandle gotol = null, gotor = null;

		if (eqtype_d.numericType()) {
			BigDecimal value = left_d.evaluateExp();

			if (value != null) {
				swapOperands();				
			} else {
				value = right_d.evaluateExp();
			}

			left_d.translate(classGen, methodGen);
		
			tozero = (value != null && tleft.oneWord() && value.intValue() == 0);
			if (!tozero) {
				right_d.translate(classGen, methodGen);
			}

			if (tleft.twoWords()) {
				il.append(tleft.CMP(false));
				tleft = Type.Int; tozero = true; 
			}
		}
		else if (eqtype_d.referenceType()) {
			if (tleft instanceof NullType) {
				swapOperands();
				tozero = true;
			}	
			else if (right_d.type_d instanceof NullType) {
				tozero = true;
			}

			left_d.translate(classGen, methodGen);

			if (!tozero) {
				right_d.translate(classGen, methodGen);
			}
		} 
		else if (eqtype_d instanceof BooleanType) {
			gotol = left_d.translateSynthesized(classGen, methodGen);
			InstructionHandle patch = il.getEnd();
			gotor =	right_d.translateSynthesized(classGen, methodGen);

			if (gotol != null) {
				gotol.setTargetNoCopy(patch.getNext());
			}
		}
		else {
			Espresso.internalError();
		}

		ifFalse = (BranchInstruction) ((operator_d == Operators.EQ_EQ) ? 
			tleft.IFNE(tozero) : tleft.IFEQ(tozero));	// negate operator

		addFalseList(il.append(ifFalse));

		if (gotor != null) {
			gotor.setTargetNoCopy(il.getEnd());
		}
	}	

}
