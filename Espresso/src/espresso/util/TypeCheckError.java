/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 3/7/98
 *
 */

package espresso.util;

import espresso.util.ErrorMsg;
import espresso.syntaxtree.SyntaxTreeNode;

public class TypeCheckError extends Exception {

	ErrorMsg error_d = null;
	SyntaxTreeNode node_d = null;
	
	/**
	  * This constructor is deprecated. From now on TypeCheckError must
	  * be constructed using error messages.
	  */
	public TypeCheckError(SyntaxTreeNode node) {
		super();
		node_d = node;
	}

	public TypeCheckError(ErrorMsg error) {
		super();
		error_d = error;
	}
	
	public String toString() {
		String result;

		if(error_d != null) 
		    {
			result = error_d.toString();
		    }
		else if(node_d != null) 
		    {
			result = "Type check error at line " + node_d.line_d + " (" + 
			    node_d.toString() + ")";
		    }
		else
		    {
			result = "Type check error (no line information)";
		    }

		return result;
	}

}
