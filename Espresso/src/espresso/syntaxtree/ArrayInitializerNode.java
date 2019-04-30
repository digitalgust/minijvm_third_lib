/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 1/23/98
 *
 */

package espresso.syntaxtree;

import java.util.Vector;

import espresso.util.Type;
import espresso.util.ArrayType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

public class ArrayInitializerNode extends ExpressionNode {

	public Vector arrinitializer_d;	// contains list of ExpressionNodes used to initialize
							    	// array
	

	public ArrayInitializerNode(Vector arrinitializer) {
		super();
		arrinitializer_d = arrinitializer;
	}
		
	public void encodeType() {
	}


	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		ExpressionNode node = null;
		Type atype = null;
		for (int i =0; i < arrinitializer_d.size(); i++) {
			node = (ExpressionNode) arrinitializer_d.elementAt(i);
			if (node instanceof ArrayInitializerNode) {
				ArrayType btype = (ArrayType) node.typeCheck(stable);
				atype = new ArrayType(btype.baseType(), btype.dims()+1);
			}
			else {
				Type btype = node.typeCheck(stable);
				atype = new ArrayType(btype,1);
			}	
			if (type_d == null) {
				type_d = atype;
			}
			else {
				if (!atype.identicalTo(type_d)) {
					throw new TypeCheckError(this);
				}
			}
		}
		return type_d;
	}

}
