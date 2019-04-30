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
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.classfile.classgen.*;

public class InstanceOfExpNode extends ExpressionNode {

	public Type ctype_d;
	public ExpressionNode left_d;

	public InstanceOfExpNode(ExpressionNode left, Type ctype) {
		left_d = left;
		ctype_d = ctype;
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type tleft = left_d.typeCheck(stable);
		
		if (ctype_d.referenceType() && ctype_d.relatedTo(tleft)) {
			type_d = Type.Boolean;
			return type_d;
		}

		throw new TypeCheckError(this);
	}
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		left_d.translate(classGen, methodGen);

		int index = cpg.addClass(ctype_d instanceof ClassType ?
			((ClassType) ctype_d).name().toString() : ctype_d.toString());
		il.append(new INSTANCEOF(index));		
		addFalseList(il.append(new IFEQ(null)));
	}

}
