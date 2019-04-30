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

import java.util.Vector;

public class MethodType extends Type {

	Type resultType_d;	
	Vector argsType_d;
	
	public MethodType(Type resultType, Vector argsType) {
		argsType_d = argsType;
		resultType_d = resultType;
	}

	public MethodType(Type resultType, Type arg1) {
		argsType_d = new Vector();
		argsType_d.addElement(arg1);
		resultType_d = resultType;
	}

	public MethodType(Type resultType, Type arg1, Type arg2) {
		argsType_d = new Vector();
		argsType_d.addElement(arg1);
		argsType_d.addElement(arg2);
		resultType_d = resultType;
	}

	public MethodType(Type resultType, Type arg1, Type arg2, Type arg3) {
		argsType_d = new Vector();
		argsType_d.addElement(arg1);
		argsType_d.addElement(arg2);
		argsType_d.addElement(arg3);
		resultType_d = resultType;
	}

	public boolean identicalTo(Type other) {
		return identicalTo(other, false);	
	}

	public boolean identicalTo(Type other, boolean modret) {
		boolean result = false;

		if (other instanceof MethodType) {
			// Compare method types modulo return types
			MethodType temp = (MethodType) other;
			result = (modret) ? true : resultType_d.identicalTo(temp.resultType_d);	
				
			if (result) {
				int len = argsCount();
				result = (len == temp.argsCount());
				for (int i = 0; i < len && result; i++) {
					Type arg1 = (Type) argsType_d.elementAt(i);
					Type arg2 = (Type) temp.argsType_d.elementAt(i);
					result = arg1.identicalTo(arg2);
				}
			}
		}

		return result;	
	}
	
	public boolean subTypeOf(Type other) {
		return identicalTo(other);
	}
	
	public boolean superTypeOf(Type other) {
		return identicalTo(other);
	}

	public int distanceTo(Type other) {
		int result = Integer.MIN_VALUE;
		
		if (other instanceof MethodType) {
			MethodType mtype = (MethodType) other;
			
			int len = argsType_d.size();
			if (len == mtype.argsType_d.size()) {
				result = 0;
				for (int i = 0; i < len; i++) {
					Type arg1 = (Type) argsType_d.elementAt(i);
					Type arg2 = (Type) mtype.argsType_d.elementAt(i);
					int temp = arg1.distanceTo(arg2);
					if (temp == Integer.MIN_VALUE) {
						result = temp;
						break;
					}
					else {
						result += arg1.distanceTo(arg2);
					}
				}
			}
		}
		
		return result;
	}

	public Type resultType() {
		return resultType_d;
	}
		
	public Vector argsType() {
		return argsType_d;
	}

	public int argsCount() {
		return (argsType_d == null) ? 0 : argsType_d.size();
	}

	public boolean referenceType() {
		return false;
	}

	public boolean primitiveType() {
		return false;
	}

	public boolean numericType() {
		return false;
	}

	public boolean integralType() {
		return false;
	}
	
	public boolean floatingPointType() {
		return false;
	}

	public String toString() {
		StringBuffer result = new StringBuffer();

		result.append("(");
		int len = argsType_d.size();
		for (int i = 0; i < len; i++) {
			Type arg = (Type) argsType_d.elementAt(i);
			result.append(arg.toString());
		}
		result.append(")");
		result.append(resultType_d.toString());

		return result.toString();
	}

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return null;		// should never be called !!
	}

}
