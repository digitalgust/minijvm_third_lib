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

public class PackageExpNode extends SymbolExpNode {

	public PackageExpNode(Symbol name) {
		super(name);
	}

	public PackageExpNode(String name) {
		super(name);
	}

	public PackageExpNode(String prefix, Symbol suffix) {
		super(prefix, suffix);
	}

	public PackageExpNode(Symbol prefix, String suffix) {
		super(prefix, suffix);
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		throw new TypeCheckError(this);
	}
		
}
