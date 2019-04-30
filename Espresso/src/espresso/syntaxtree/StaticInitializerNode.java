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

import espresso.classfile.classgen.ClassGen;

public class StaticInitializerNode extends ClassBodyDeclarationNode {

	public BlockNode block_d;

	public void encodeType() {
	}
	
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	  /* don't have to call typeCheck here, because typeChecking is done
	     in <clinit>, where the content of the block gets copied to
		if (block_d != null) {
			block_d.typeCheck(stable);
		}
	  */
		
		return Type.Void;
	}

	public void translate( ClassGen classGen ) {
	}	
}
