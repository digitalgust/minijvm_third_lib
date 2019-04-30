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
import espresso.util.NotConstant;
import espresso.util.Operators;
import espresso.util.MethodType;
import espresso.util.ClassType;
import espresso.util.SymbolTable;
import espresso.util.Symbol;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.util.Vector;
import java.math.BigDecimal;

public class AdditiveExpNode extends ExpressionNode {

	public int operator_d;
	public ExpressionNode left_d;
	public ExpressionNode right_d;
	// null if leftmost AdditiveExp, otherwise next AdditiveExp to the left
	// is used in case of string concatenation
	public ExpressionNode parent_d;

	public AdditiveExpNode(ExpressionNode left, ExpressionNode right, 
		int operator) 
	{
		left_d = left;
		right_d = right;
		operator_d = operator;
		parent_d = null;
	}
	
	public Type stringType() {
		return new ClassType("java.lang.String");
	}

	public boolean operationPlus() {
		return operator_d == Operators.PS;
	}
	
	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		BigDecimal result = null;
		
		switch (operator_d) {
		case Operators.PS:
			result = left_d.evaluate(stable);
			result = result.add(right_d.evaluate(stable));
		break;
		case Operators.MS:
			result = left_d.evaluate(stable);
			result = result.subtract(right_d.evaluate(stable));
		break;
		default:
			Espresso.internalError();
		}

		return result;
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

		Type tleft  = left_d.typeCheck(stable);
		Type tright = right_d.typeCheck(stable);
		
		// if either of the two types is void, we have a TC error
		if (tleft.identicalTo(Type.Void) ||
			tright.identicalTo(Type.Void) ) {
				throw new TypeCheckError(this);
		}
		// if operator is +, we need to check, whether at least one the type determined
		// above is java.lang.String. If that is the case, then we have string concatenation
		
		if ( operationPlus() ) {
			if (tleft.identicalTo(stringType() ) ) {
				type_d = tleft;
				return type_d;		
			}
			if 	(tright.identicalTo(stringType() ) ) {
				type_d = tright;
				return type_d;		
			}		
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

	public void translateStringConcat(ClassGen classGen, MethodGen methodGen) {

		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();
		BranchHandle goto1 = null;
		InstructionHandle target1 = null;
		BranchHandle goto2 = null;
		InstructionHandle target2 = null;

		// if we are the top node, we first need to add a new Stringbuffer
		if ( parent_d == null ) {
			Symbol className = new Symbol("java.lang.StringBuffer");
			int index = cpg.addClass(className.toInternalString());
			il.append(new NEW(index));
			il.append(new DUP());
		}
		if ( left_d instanceof NullLiteral ) {
			il.append(new PUSH(cpg, "null"));			
		}
		else if (left_d.type_d.identicalTo( Type.Boolean ) ) {
			goto1 = left_d.translateSynthesized(classGen, methodGen);
			target1 = il.getEnd();
		}
		else {
			left_d.translate(classGen, methodGen);
		}
		if (!(left_d instanceof AdditiveExpNode) ||
			(left_d instanceof AdditiveExpNode && 
			  !left_d.type_d.identicalTo(stringType() ))) {
			il.append(left_d.type_d.VALUEOF(cpg));
		}
		// fix jump if left needed
		if ( goto1 != null ) {
			goto1.setTargetNoCopy(target1.getNext());
		}
		
		if ( right_d instanceof NullLiteral ) {
			il.append(new PUSH(cpg, "null"));			
		}
		else if (right_d.type_d.identicalTo( Type.Boolean ) ) {
			goto2 = right_d.translateSynthesized(classGen, methodGen);
			target2 = il.getEnd();
		}

		else {
			right_d.translate(classGen, methodGen);
		}
		il.append(right_d.type_d.APPENDSTB(cpg));	

		// fix jump for right if needed
		if ( goto2 != null ) {
			goto2.setTargetNoCopy(target2.getNext());
		}

		if ( parent_d == null ) {
			MethodType mtype = new MethodType(new ClassType("java.lang.String"), new Vector());
			int index = cpg.addMethodref(new Symbol("java.lang.StringBuffer").toInternalString(), "toString", 
			mtype.toInternalString());
			il.append(new INVOKEVIRTUAL(index));

			
		}
			
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {

		if ( operationPlus() && type_d.identicalTo(stringType() ) ) {
			translateStringConcat(classGen, methodGen);
			return;
		}
				
		Instruction in = null;

		left_d.translate(classGen, methodGen);
		right_d.translate(classGen, methodGen);
		
		switch (operator_d) {
		case Operators.PS:
			in = type_d.ADD();
		break;
		case Operators.MS:
			in = type_d.SUB();
		break;
		default:
			Espresso.internalError();
		}		

		InstructionList il = methodGen.getInstructionList();
		il.append(in);
	}	

}
