/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 2/03/98
 *
 */

package espresso.syntaxtree;

import espresso.Espresso;
import espresso.util.Type;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.ClassGen;
import espresso.classfile.classgen.MethodGen;

public abstract class StatementNode extends SyntaxTreeNode {

	public abstract Type typeCheck(SymbolTable stable) 
		throws TypeCheckError;


	public void translate(ClassGen classGen, MethodGen methodGen) {
		Espresso.notYetImplemented();
	}	
	
}
