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

public class ArrayType extends ReferenceType {

	int dims_d;
	Type baseType_d;
		
	static Type object 	 = Type.createClassType("java.lang.Object");
	static Type cloneable = Type.createClassType("java.lang.Cloneable");

	public ArrayType(Type baseType, int dims) {
		dims_d = dims;
		baseType_d = baseType;		
	}

	public int dims() {
		return dims_d;
	}

	public Type baseType() {
		return baseType_d;
	}
		
	public boolean identicalTo(Type other) {
		boolean result = false;	
		if (other instanceof ArrayType) {
			ArrayType temp = (ArrayType) other;
			result = (dims_d == temp.dims_d && 
				baseType_d.identicalTo(temp.baseType_d));
		}
		return result;
	}

	public boolean subTypeOf(Type other) {

		if (identicalTo(other)) {
			return true;
		}

		if (other instanceof ArrayType) {
			ArrayType atype = (ArrayType) other;
			return (dims_d == atype.dims_d && baseType_d.subTypeOf(atype.baseType_d));
		}
	
		return (other.identicalTo(object) || other.identicalTo(cloneable));
	}
	
	public boolean superTypeOf(Type other) {
		return other.subTypeOf(this);
	}

	/**
	  * The distance between array types is defined to be that  of the base
	  * types if positive or zero and the dimensions identical. In addition, 
	  * the distances  to  java.lang.Object and java.lang.Cloneable are big 
	  * enough  never to be  preferred  over another compatible array type. 
	  * Also  note that  java.lang.Cloneable is  preferred  over  java.lang
	  * .Object.
	  */
	public int distanceTo(Type other) {
		int result = Integer.MIN_VALUE;
		
		if (other instanceof ArrayType) {
			ArrayType atype = (ArrayType) other;
			if (dims_d == atype.dims_d) {
				if (baseType_d instanceof ClassType && atype.baseType_d instanceof ClassType) {
					result = baseType_d.distanceTo(atype.baseType_d);
				}
				else if (baseType_d.identicalTo(atype.baseType_d)) {
					result = 0;			// both primitive types
				}	
			}	
		}
		else if (other instanceof ClassType) {
			if (other.identicalTo(cloneable)) {
				result = Integer.MAX_VALUE / 4;
			}
			else if (other.identicalTo(object)) {
				result = Integer.MAX_VALUE / 2;
			}
		}
			
		return result;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < dims_d; i++) {
			result.append("[");
		}
		result.append(baseType_d.toString());
		return result.toString();
	}	

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(baseType_d.toClassFileType(), dims_d);
	}

	public InstructionList VALUEOF(ConstantPoolGen cpg) {
		Symbol mstring = new Symbol("java.lang.String");
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");

		InstructionList il = new InstructionList();
		Symbol mname = new Symbol("valueOf");
		MethodType mtype = new MethodType(new ClassType(mstring),
												  new ClassType("java.lang.Object"));
		int index = cpg.addMethodref(mstring.toInternalString(), 
			   							mname.toString(), mtype.toInternalString());
		il.append(new INVOKESTATIC(index));				

		mtype = new MethodType(Type.Void,new ClassType("java.lang.String"));
		index = cpg.addMethodref(mbuffer.toInternalString(), "<init>", 
		mtype.toInternalString());
		il.append(new INVOKESPECIAL(index));
		return il;			
	}
	
	public Instruction APPENDSTB(ConstantPoolGen cpg) {
		Symbol mstring = new Symbol("java.lang.String");
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");

		MethodType mtype = new MethodType(new ClassType(mbuffer), new ClassType("java.lang.Object"));
		int index = cpg.addMethodref(mbuffer.toInternalString(), "append", 
		mtype.toInternalString());
		return new INVOKEVIRTUAL(index);	
	}


}
