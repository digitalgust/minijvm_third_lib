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
import espresso.util.BooleanType;
import espresso.util.MethodType;
import espresso.util.Operators;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.util.NotConstant;

import espresso.classfile.classgen.*;

import java.math.BigDecimal;
import java.util.Enumeration;

public class ExclusiveOrExpNode extends ExpressionNode {

	public ExpressionNode left_d;
	public ExpressionNode right_d;

	public ExclusiveOrExpNode(ExpressionNode left, ExpressionNode right) {
		left_d = left;
		right_d = right;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		BigDecimal result = null;

		if (type_d instanceof BooleanType) {
			BigDecimal left = left_d.evaluate(stable);
			BigDecimal right = right_d.evaluate(stable);
			result = left.add(right);
			return new BigDecimal(result.intValue() == 1 ? "1" : "0");
		}

		throw new NotConstant(this);
	}		
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

		Type tleft  = left_d.typeCheck(stable);
		Type tright = right_d.typeCheck(stable);

		MethodType ptype = lookupPrimop(stable, Operators.EXC_OR, 
			new MethodType(Type.Void, tleft, tright));		// defined in ExpressionNode
		
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
		translateSynthesized(classGen, methodGen);
		addFalseList(il.append(left_d.type().IFEQ(true)));
	}

	public BranchHandle translateSynthesized(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		BranchHandle gotol = left_d.translateSynthesized(classGen, methodGen);
		InstructionHandle patch = il.getEnd();
		BranchHandle gotor = right_d.translateSynthesized(classGen, methodGen);

		if (gotol != null) {
			gotol.setTargetNoCopy(patch.getNext());
		}
		patch = il.append(left_d.type().XOR());
		if (gotor != null) {
			gotor.setTargetNoCopy(patch);
		}
		return null;
	}

}
