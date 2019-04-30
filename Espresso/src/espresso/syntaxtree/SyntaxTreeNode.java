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

public abstract class SyntaxTreeNode {

	public int line_d;
	public int column_d;			// Position in the source code

	public SyntaxTreeNode() {
		line_d = 0;
		column_d = 0;
	}

	public SyntaxTreeNode(int line, int column) {
		line_d = line;
		column_d = column;
	}

	public abstract Type typeCheck(SymbolTable stable) 
		throws TypeCheckError;

}

