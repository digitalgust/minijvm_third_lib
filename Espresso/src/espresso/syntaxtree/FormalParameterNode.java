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
import espresso.util.ArrayType;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

public class FormalParameterNode extends SyntaxTreeNode {

	public Type type_d;
	public Symbol name_d;
	
	/**
	  * Move the ['s defined on the method name to the type. 
	  */
	public void encodeType() {
		String name = name_d.toString();

		int dims = 0;
		int last = name.length() - 1;
		while (last > 0 && name.charAt(last) == '[') {
			dims++; last--;	
		}

		if (dims > 0) {
			Type baseType = type_d;
				
			if (type_d instanceof ArrayType) {
				ArrayType atype = (ArrayType) type_d;
				dims += atype.dims();
				baseType = atype.baseType();
			}		

			type_d = new ArrayType(baseType, dims);
			name_d = new Symbol(name.substring(0, last + 1));
		}
		
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		return Type.Void;
	}

}
