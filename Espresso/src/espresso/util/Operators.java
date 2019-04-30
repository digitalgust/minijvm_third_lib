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

public final class Operators {

	final static public int UNDEFINED = 0;
	
    final static public int EQ 		= 1;		// "="
    final static public int TS_EQ 	= 2;		// "*="
    final static public int DV_EQ 	= 3;		// "/="
    final static public int MD_EQ 	= 4;		// "%="
    final static public int PS_EQ 	= 5;		// "+="
    final static public int MS_EQ 	= 6;		// "-="
    final static public int SL_EQ 	= 7;		// "<<="
    final static public int SR_EQ 	= 8;		// ">>="
    final static public int SS_EQ 	= 9;		// ">>>="
    final static public int OR_EQ 	= 10;		// "|="
    final static public int AND_EQ 	= 11;		// "&="
    final static public int XOR_EQ 	= 12;		// "^="
    final static public int EQ_EQ  	= 13;		// "=="
    final static public int NEQ_EQ 	= 14;		// "!="
    final static public int LT		= 15;		// "<"
    final static public int GT		= 16;		// ">"
    final static public int LT_EQ	= 17;		// "<="
    final static public int GT_EQ	= 18;		// ">="
    final static public int SL		= 19;		// "<<"
    final static public int SR		= 20;		// ">>"
    final static public int SS		= 21;		// ">>>"
    final static public int PS		= 22;		// "+"
    final static public int MS		= 23;		// "-"
    final static public int TS		= 24;		// "*"
    final static public int MD		= 25;		// "%"
    final static public int DV		= 26;		// "/"
    final static public int AND  	= 27;		// "&"
    final static public int INC_OR	= 28;		// "|"
    final static public int EXC_OR	= 29;		// "^"
    final static public int NEG		= 30;		// "~"
    final static public int UN_PS	= 31;		// "u+"
    final static public int UN_MS	= 32;		// "u-"
    
	final static Symbol[] opKey_d = {
		new Symbol("<undefined>"),
		new Symbol("="),
    	new Symbol("*="), 
		new Symbol("/="),
		new Symbol("%="),
		new Symbol("+="),
		new Symbol("-="),
		new Symbol("<<="),
		new Symbol(">>="),
		new Symbol(">>>="),
		new Symbol("|="),
		new Symbol("&="),
		new Symbol("^="),
		new Symbol("=="),
		new Symbol("!="),
		new Symbol("<"),
		new Symbol(">"),
		new Symbol("<="),
		new Symbol(">="),
		new Symbol("<<"),
		new Symbol(">>"),
		new Symbol(">>>"),
		new Symbol("+"),
		new Symbol("-"),
		new Symbol("*"),
		new Symbol("%"),
   		new Symbol("/"),
   		new Symbol("&"),
   		new Symbol("|"),
   		new Symbol("^"),
   		new Symbol("~"),
   		new Symbol("u+"),
   		new Symbol("u-")
	};

    static public Symbol toSymbol(int op) {
		return opKey_d[op];    
    }

}
