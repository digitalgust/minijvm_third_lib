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

public class IntType extends IntegralType {

	public boolean identicalTo(Type other) {
		return (other instanceof IntType);
	}

	public int distanceTo(Type other) {
		int result = Integer.MIN_VALUE;
		
		if (other instanceof IntType) {
			result = 0;
		}
		else if (other instanceof LongType) {
			result = 1;
		}
		else if (other instanceof FloatType) {
			result = 2;
		}
		else if (other instanceof DoubleType) {
			result = 3;
		}
		
		return result;
	}

	public boolean inRange(long value) {
		return value >= Integer.MIN_VALUE && value <= Integer.MAX_VALUE;
	}


	public String toString() {
		return Type.IntRep;
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(Constants.T_INT);
	}

	public Instruction ALOAD() {
		return new IALOAD();
	}

	public Instruction ASTORE() {
		return new IASTORE();
	}

	public Instruction SHL() {
		return new ISHL();
	}

	public Instruction SHR() {
		return new ISHR();
	}

	public Instruction USHR() {
		return new IUSHR();
	}

	public Instruction IFGT(boolean tozero) {
		return tozero ? (Instruction) new IFGT(null) : 
						(Instruction) new IF_ICMPGT(null);	
	}

	public Instruction IFLT(boolean tozero) {
		return tozero ? (Instruction) new IFLT(null) : 
						(Instruction) new IF_ICMPLT(null);	
	}

	public Instruction IFGE(boolean tozero) {
		return tozero ? (Instruction) new IFGE(null) : 
						(Instruction) new IF_ICMPGE(null);	
	}

	public Instruction IFLE(boolean tozero) {
		return tozero ? (Instruction) new IFLE(null) : 
						(Instruction) new IF_ICMPLE(null);	
	}

	public Instruction IFEQ(boolean tozero) {
		return tozero ? (Instruction) new IFEQ(null) : 
						(Instruction) new IF_ICMPEQ(null);	
	}

	public Instruction IFNE(boolean tozero) {
		return tozero ? (Instruction) new IFNE(null) : 
						(Instruction) new IF_ICMPNE(null);	
	}

	public Instruction NEWARRAY(int index) {
		return new NEWARRAY(Constants.T_INT);
	}

	public InstructionList CAST(Type other) {
		Instruction op = null;
		InstructionList il = new InstructionList();
		
		if (other instanceof ByteType) {
			op = new I2B();
		}
		else if (other instanceof CharType) {
			op = new I2C();
		}
		else if (other instanceof ShortType) {
			op = new I2S();
		}
		else if (other instanceof LongType) {
			op = new I2L();
		}
		else if (other instanceof FloatType) {
			op = new I2F();
		}
		else if (other instanceof DoubleType) {
			op = new I2D();
		}
		else {
			op = new NOP();
		}

		il.append(op);
		return il;
	}

}
