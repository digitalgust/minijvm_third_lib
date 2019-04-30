/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: K. Doerig
 * Date: 4/15/98
 *
 */

package espresso.util;

public abstract class FloatingPointType extends PrimitiveType {

	public boolean integralType() {
		return false;
	}
	
	public boolean floatingPointType() {
		return true;
	}	

}
