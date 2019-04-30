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

import java.math.BigDecimal;

public class BooleanType extends PrimitiveType {

	public boolean identicalTo(Type other) {
		return (other instanceof BooleanType);
	}

	public int distanceTo(Type other) {
		return (other instanceof BooleanType) ? 0 : Integer.MIN_VALUE;		
	}
	
	public String toString() {
		return Type.BooleanRep;
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
		return new ClassGenType(Constants.T_BOOLEAN);
	}

	public Instruction LOAD(int slot) {
		return new ILOAD(slot);
	}
	
	public Instruction STORE(int slot) {
		return new ISTORE(slot);
	}

	public Instruction ALOAD() {
		return new BALOAD();
	}

	public Instruction ASTORE() {
		return new BASTORE();
	}
	 
	public Instruction IFEQ(boolean tozero) {
		return tozero ? (Instruction) new IFEQ(null) : 
						(Instruction) new IF_ICMPEQ(null);	
	}

	public Instruction IFNE(boolean tozero) {
		return tozero ? (Instruction) new IFNE(null) : 
						(Instruction) new IF_ICMPNE(null);	
	}

	public Instruction RETURN() {
		return new IRETURN();
	}
	
	public Instruction NEWARRAY(int index) {
		return new NEWARRAY(Constants.T_BOOLEAN);
	}

	public Instruction AND() {
		return new IAND();
	}

	public Instruction OR() {
		return new IOR();
	}

	public Instruction XOR() {
		return new IXOR();
	}

	public Instruction PUSH(ConstantPoolGen cpg, BigDecimal value) {
		PUSH push = new PUSH(cpg, value.intValue());	
		return push.getInstruction();
	}

}
