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
import espresso.util.BooleanType;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public abstract class VariableExpNode extends ExpressionNode {

	public Symbol name_d;

	public VariableExpNode(Symbol name) {
		name_d = name;
	}

	public boolean variableExp() {
		return true;
	}
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		translateSynthesized(classGen, methodGen);
		if (type_d instanceof BooleanType) {
			desynthesize(classGen, methodGen);
		}
	}

	public BranchHandle translateSynthesized(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpGen = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		LocalVariableGen lv = methodGen.lookupLocal(name_d.baseName(2));
		il.append(type_d.LOAD(lv.getSlot()));
		return null;
	}	
	
}
