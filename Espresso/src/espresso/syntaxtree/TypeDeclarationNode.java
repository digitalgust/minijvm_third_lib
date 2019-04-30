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

import espresso.Espresso;
import espresso.util.Type;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import java.util.Vector;

public abstract class TypeDeclarationNode extends SyntaxTreeNode {

	public Symbol name_d;
	public Vector body_d;
	public int accessFlags_d;
	public boolean rooted_d;			// false if superstuff not loaded (TODO -> remove this ?)
	public Vector interfaces_d;			// null if no interfaces implemented
	
	final static int NUMBER_FIELDS = 10;
	final static int NUMBER_INTERFACES = 3;

	TypeDeclarationNode() {
		body_d = new Vector(NUMBER_FIELDS);
		interfaces_d = new Vector(NUMBER_INTERFACES);
		rooted_d = false;	
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		TypeCheckError last = null;
		Vector errors = Espresso.errors();
		
		int n = body_d.size();
		for (int i = 0; i < n; i++) {	
			ClassBodyDeclarationNode node = (ClassBodyDeclarationNode) 
					body_d.elementAt(i);
			try {
				node.typeCheck(stable);		
			}
			catch (TypeCheckError e) {
				if (last != null) {
					errors.addElement(last.toString());
				}
				last = e;		// last exception raised
			}
		}

		if (last != null) {
			throw last;			// rethrow the last exception
		} 
		
		return Type.Void;
	}

	public abstract void translate();

}
