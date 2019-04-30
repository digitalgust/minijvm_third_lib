/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 2/27/98
 *
 */

package espresso.syntaxtree;

import espresso.util.Type;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class TypeExpNode extends SymbolExpNode {

	public TypeExpNode(Symbol name) {
		super(name);
	}

	public TypeExpNode(String name) {
		super(name);
	}

	public TypeExpNode(Symbol prefix, Symbol suffix) {
		super(prefix, suffix);
	}

	public TypeExpNode(String prefix, Symbol suffix) {
		super(prefix, suffix);
	}

	public TypeExpNode(Symbol prefix, String suffix) {
		super(prefix, suffix);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
	}

}
