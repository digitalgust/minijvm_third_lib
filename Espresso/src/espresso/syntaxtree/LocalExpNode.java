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

public class LocalExpNode extends VariableExpNode {

	public LocalExpNode(Symbol name) {
		super(name);
	}
	
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		LocalVarDeclarationNode node = stable.lookupLocal(name_d); 
		int n = node.locals_d.size();
		for ( int i =0; i < n; i++ ) {
			VariableDeclaratorNode var = 
					(VariableDeclaratorNode) node.locals_d.elementAt(i);

			if (name_d.equals(var.name_d) ) {
			    type_d = var.type_d;
			    break;
			}
		}

		if(type_d == null)
		    throw new TypeCheckError(this);

		return type_d;
	}

}
