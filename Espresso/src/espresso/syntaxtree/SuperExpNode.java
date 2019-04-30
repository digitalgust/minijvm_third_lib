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
import espresso.util.ClassType;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class SuperExpNode extends SymbolExpNode {

	public SuperExpNode(Symbol name) {
		super(name);
	}

	public SuperExpNode(String name) {
		super(name);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		il.append(new ALOAD(0));		
	}

}
