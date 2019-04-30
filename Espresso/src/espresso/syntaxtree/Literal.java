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

public abstract class Literal extends ExpressionNode {

	public abstract Type typeCheck(SymbolTable stable)
		throws TypeCheckError;
	
	public abstract int addToConstantPool(ClassGen classGen);
	
}
