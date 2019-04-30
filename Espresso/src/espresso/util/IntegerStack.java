/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 2/14/98
 *
 */

package espresso.util;

import java.util.Stack;
import java.lang.Integer;

public class IntegerStack extends Stack {

	public IntegerStack() {
		super();
	}
	
	public Integer peekInteger() {
		return (Integer) super.peek();
	}
	
	public Integer popInteger() {
		return (Integer) super.pop();
	}
	
	public Integer pushInteger(Integer val) {
		return (Integer) super.push(val);
	}		
	
}
