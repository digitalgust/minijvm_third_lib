/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 4/12/98
 *
 */

package espresso.util;

public class AmbiguousName extends Exception {

	Symbol name_d;
	
	public AmbiguousName(Symbol name) {
		super();
		name_d = name;
	}
	
	public String toString() {
		String lstring = "Ambiguous reference to "; // + name_d.toString();
		return lstring;
	}

}
