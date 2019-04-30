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

package espresso.util;

import java.lang.Integer;

public class IntegerStream {

	int counter_d = 0;

	public IntegerStream() {
	}
		
	public IntegerStream(int start) {
		counter_d = start;
	}
	
	public void rewind() {
		counter_d = 0;
	}
	
	public Integer next() {
		return new Integer(counter_d++);
	}
	
	public Integer last() {
		return new Integer(counter_d - 1);
	}
	
}
