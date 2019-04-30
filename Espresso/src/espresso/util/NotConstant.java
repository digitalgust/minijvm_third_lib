/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 4/18/98
 *
 */

package espresso.util;

import espresso.syntaxtree.ExpressionNode;

public class NotConstant extends Exception {

	ExpressionNode node_d;
	
	public NotConstant(ExpressionNode node) {
		super();
		node_d = node;
	}
	
	public String toString() {
		String lstring = "at line " + node_d.line_d;
		return lstring;
	}

}
