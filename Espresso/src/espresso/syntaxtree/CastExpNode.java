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
import espresso.util.ClassType;
import espresso.util.Symbol;
import espresso.util.NotConstant;
import espresso.util.BooleanType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class CastExpNode extends ExpressionNode {

	public ExpressionNode left_d;

	public CastExpNode(ExpressionNode left, Type type) {
		left_d = left;
		type_d = type;		// use inherited field
	}
		
	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		return left_d.evaluate(stable);
	}		

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type tleft  = left_d.typeCheck(stable);
		
		if (tleft.primitiveType()) {
			if (tleft instanceof BooleanType) {
				if (type_d instanceof BooleanType) {
					return type_d;
				}
			}
			else if (!(type_d instanceof BooleanType)) {
				return type_d;
			}
		}
		else if (tleft.relatedTo(type_d)) {
			return type_d;
		}
		
		throw new TypeCheckError(this);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		ConstantPoolGen cpg = classGen.getConstantPool();

		Type tleft = left_d.type();
		BigDecimal value = left_d.evaluateExp();

		if (value != null) {
			il.append(type_d.PUSH(cpg, value));			// avoid a cast for constants
		}
		else {
			left_d.translate(classGen, methodGen);

			if (!type_d.identicalTo(tleft)) {
				if (type_d.referenceType()) {
					int index = cpg.addClass(type_d instanceof ClassType ?
						((ClassType) type_d).name().toString() : type_d.toString());
					il.append(new CHECKCAST(index));		
				}
				else {
					il.append(tleft.CAST(type_d));
				}
			}
		}	
	}
		
}
