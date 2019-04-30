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
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class NullLiteral extends Literal {

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		type_d = Type.Null;
		return type_d;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		il.append(new ACONST_NULL());
	}
	
	public int addToConstantPool(ClassGen classGen) {
		return -1;		// not a valid index in CP
	}

}
