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

import espresso.classfile.Constants;
import espresso.classfile.classgen.*;

public class VoidType extends PrimitiveType {

	public boolean identicalTo(Type other) {
		return (other instanceof VoidType);
	}

	public String toString() {
		return Type.VoidRep;
	}
	
	public int distanceTo(Type other) {
		return Integer.MIN_VALUE;
	}
	
	public boolean integralType() {
		return false;
	}
	
	public boolean floatingPointType() {
		return false;
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(Constants.T_VOID);
	}

	/*
	 * JVM instructions.
	 */
	public Instruction LOAD(int slot) {
		return null;
	}
	
	public Instruction STORE(int slot) {
		return null;
	}

	public Instruction RETURN() {
		return new RETURN();
	}

	public Instruction POP() {
		return new NOP();
	}

}
