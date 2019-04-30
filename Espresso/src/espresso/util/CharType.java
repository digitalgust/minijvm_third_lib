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

public class CharType extends IntegralType {

	public boolean identicalTo(Type other) {
		return (other instanceof CharType);
	}

	public int distanceTo(Type other) {
		int result = Integer.MIN_VALUE;
		
		if (other instanceof CharType) {
			result = 0;
		}
		else if (other instanceof IntType) {
			result = 1;
		}
		else if (other instanceof LongType) {
			result = 2;
		}
		else if (other instanceof FloatType) {
			result = 3;
		}
		else if (other instanceof DoubleType) {
			result = 4;
		}
		
		return result;
	}

	public boolean inRange(long value) {
		return value >= Character.MIN_VALUE && value <= Character.MAX_VALUE;
	}

	public String toString() {
		return Type.CharRep;
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(Constants.T_CHAR);
	}

	/*
	 * JVM instructions.
	 */

	public Instruction ALOAD() {
		return new CALOAD();
	}

	public Instruction ASTORE() {
		return new CASTORE();
	}

	public Instruction NEWARRAY(int index) {
		return new NEWARRAY(Constants.T_CHAR);
	}

	public InstructionList CAST(Type other) {
		InstructionList il = new InstructionList();

		if (other instanceof CharType) {
			il.append(new NOP());
		} else { 
			il.append(Type.Int.CAST(other));
		}

		return il;
	}
	
}
