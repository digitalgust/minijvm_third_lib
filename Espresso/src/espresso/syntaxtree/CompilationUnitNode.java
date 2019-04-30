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

public class CompilationUnitNode extends SyntaxTreeNode {

	/**
	  * The name of the package to which the compilation unit
	  * belongs to. 
	  */
	public Symbol packName_d = null;
	
	/**
	  * A Symbol vector with all the import declarations defined 
	  * in this module. 
	  */
	public Vector imports_d;		

	/**
	  * A vector of type declarations (classes or interfaces)
	  * defined in this module.
	  */
	public Vector types_d;			

	final int NUMBER_TYPES = 10;
	final int NUMBER_IMPORTS = 10;

	public CompilationUnitNode() {
		types_d = new Vector(NUMBER_TYPES);
		imports_d = new Vector(NUMBER_IMPORTS);
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		TypeCheckError last = null;
		Vector errors = Espresso.errors();
		
		int n = types_d.size();
		for (int i = 0; i < n; i++) {	
			TypeDeclarationNode node = (TypeDeclarationNode) types_d.elementAt(i);
			try {
				Espresso.currentClass(node.name_d);			// record this name
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

	public void translate() {
		int n = types_d.size();
		for (int i = 0; i < n; i++) {
			TypeDeclarationNode node = (TypeDeclarationNode) types_d.elementAt(i);
			node.translate();
		}
	}

}

