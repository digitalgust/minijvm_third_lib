/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 3/22/98
 *
 */

package espresso.util;

public class InternalError extends Exception {

	String msg_d = new String("Oops ...");
	
	public InternalError() {
	}

	public InternalError(String msg) {
		msg_d = msg;
	}

	public String toString() {
		return msg_d;
	}

}
