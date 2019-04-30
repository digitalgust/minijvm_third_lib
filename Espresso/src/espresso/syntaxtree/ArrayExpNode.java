/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 2/24/98
 *
 */

package espresso.syntaxtree;

import espresso.util.Type;
import espresso.util.IntType;
import espresso.util.ArrayType;
import espresso.util.BooleanType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class ArrayExpNode extends ExpressionNode {

	public ExpressionNode left_d;
	public ExpressionNode right_d;

	public ArrayExpNode(ExpressionNode left, ExpressionNode right) {
		left_d = left;
		right_d = right;
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type tleft = left_d.typeCheck(stable);

		if (tleft instanceof ArrayType) {
			ArrayType atype = (ArrayType) tleft;
			Type tright = right_d.typeCheck(stable);

			if (tright.integralType() && tright.oneWord()) {
				if (!(tright instanceof IntType)) {
					right_d = new CastExpNode(right_d, Type.Int);
				}
				int dims = atype.dims() - 1;
				type_d = (dims != 0) ? new ArrayType(atype.baseType(), dims)
					: atype.baseType();
				return type_d;
			}
		}

		throw new TypeCheckError(this);
	}

	public BranchHandle translateSynthesized(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		left_d.translate(classGen, methodGen);		
		right_d.translate(classGen, methodGen);

		if (!leftValue_d) {
			il.append(type_d.ALOAD());
		}

		return null;
	}
		
	public void translate(ClassGen classGen, MethodGen methodGen) {
		translateSynthesized(classGen, methodGen);
		if (type_d instanceof BooleanType && !leftValue_d) {
			desynthesize(classGen, methodGen);
		}
	}

}
