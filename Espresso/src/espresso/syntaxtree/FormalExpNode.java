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
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class FormalExpNode extends VariableExpNode {

	public FormalExpNode(Symbol name) {
		super(name);
	}
	
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		FormalParameterNode formal = stable.lookupFormal(name_d);
		type_d = formal.type_d;
		return type_d;
	}

}
