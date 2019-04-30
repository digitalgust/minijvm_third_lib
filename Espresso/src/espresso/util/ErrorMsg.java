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

import espresso.syntaxtree.SyntaxTreeNode;

import java.lang.String;
import java.lang.Integer;
import java.text.MessageFormat;

public class ErrorMsg {

	int code_d;
	int line_d, column_d;
	Object[] params_d = null;
	
	public static final int CLASSDEF_ERR = 0;
	public static final int INTERDEF_ERR = 1;
	public static final int FIELDDEF_ERR = 2;
	public static final int LOCALDEF_ERR = 3;
	public static final int AMBITYPE_ERR = 4;
	public static final int NOTCLASS_ERR = 5;
	public static final int DUPMETHD_ERR = 6;
	public static final int EXCEPTNS_ERR = 7;
	public static final int SYNCHRON_ERR = 8;
	public static final int NOLVALUE_ERR = 9;
	public static final int BREAKNIL_ERR = 10;
	public static final int CONTINIL_ERR = 11;
	public static final int UNDEFLBL_ERR = 12;
	public static final int NOTASTME_ERR = 13;
	public static final int NOTSTATC_ERR = 14;
	public static final int NOTINSTC_ERR = 15;
	public static final int UNDEFVAR_ERR = 16;
	public static final int UNDEFMTH_ERR = 17;
	public static final int INVACONS_ERR = 18;
        public static final int TYINVMET_ERR = 19;
        public static final int UNCAUGHT_ERR = 20;
        public static final int UNCTCHBL_ERR = 21;
        public static final int UNTHROWN_ERR = 22;
        public static final int UNTHRWBL_ERR = 23;

	static final String messages_d[] = { 
		"class ''{0}'' already defined in this package.",
		"interface ''{0}'' already defined in this package.",
		"field ''{0}'' already definkked in this class.",
		"parameter or variable ''{0}'' already defined in this method.",
		"reference to type ''{0}'' is ambiguous.",
		"cannot find definition for type ''{0}'' (check CLASSPATH).",
		"method ''{0}'' is already defined in this type.",
		"sorry exceptions not implemented in this version.",
		"synchornized statements work only on instances of reference type.",
		"the target of the assignment is not a left value.",
		"break must be defined within a loop or a switch.",
		"continue must be defined within a loop.",
		"label ''{0}'' not defined.",
		"not a valid statement expression.",
		"can''t make static reference to field/method ''{0}''.",
		"can''t make instance reference to field/method ''{0}''.",
		"undefined variable ''{0}''.",
		"field/method ''{0}'' not defined in type ''{1}''.",
		"Invalid numeric constant {0}.",
		"can''t invoke method ''{0}'' on non string literal.",
		"exception ''{0}'' must be caught, or it must be declared in the throws clause of this method.",
		"class ''{0}'' cannot be caught. It must be a subclass of java.lang.Throwable.",
		"exception ''{0}'' is never thrown in the body of the corresponding try statement.",
		"class ''{0}'' cannot be thrown. It must be a subclass of java.lang.Throwable."
	 };

	String lineColumn() {
		String result = "";
		
		if (line_d != 0) {
			result = "Line " + Integer.toString(line_d) + ", ";
			if (column_d != 0) {
				result += "column " + Integer.toString(column_d) + ", ";
			}
		}
		
		return result;
	}
	
	public ErrorMsg(int code) {
		code_d = code;
		line_d = column_d = 0;
	}
	
	public ErrorMsg(int code, int line) {
		code_d = code;
		line_d = line;
		column_d = 0;
	}
	
	public ErrorMsg(int code, int line, int column) {
		code_d = code;
		line_d = line;
		column_d = column;
	}

	public ErrorMsg(int code, SyntaxTreeNode node) {
		code_d = code;
		line_d = node.line_d;
		column_d = node.column_d;
	}

	public ErrorMsg(int code, Object param1, SyntaxTreeNode node) {
		code_d = code;
		line_d = node.line_d;
		column_d = node.column_d;
		params_d = new Object[1];
		params_d[0] = param1;
	}

	public ErrorMsg(int code, Object param1, Object param2, SyntaxTreeNode node) {
		code_d = code;
		line_d = node.line_d;
		column_d = node.column_d;
		params_d = new Object[2];
		params_d[0] = param1;
		params_d[1] = param2;
	}

	/**
	  * This version of toString() uses the params_d instance variable
	  * to format the message.
	  */
	public String toString() {
		String suffix;

		if(params_d == null)
		    {
			suffix = new String(messages_d[code_d]);
		    }
		else
		    {
			suffix = MessageFormat.format(messages_d[code_d], params_d);
		    }

		return (lineColumn() + suffix);
	}
	
	public String toString(Object obj) {
		Object params[] = new Object[1];
		params[0] = obj.toString();
		String suffix = MessageFormat.format(messages_d[code_d], params);
		return (lineColumn() + suffix);
	}
	
	public String toString(Object obj0, Object obj1) {
		Object params[] = new Object[2];
		params[0] = obj0.toString();
		params[1] = obj1.toString();
		String suffix = MessageFormat.format(messages_d[code_d], params);
		return (lineColumn() + suffix);
	}
	
}

