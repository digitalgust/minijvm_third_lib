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

package espresso;

import java.util.Vector;
import java.io.FileInputStream;

import espresso.parser.*;
import espresso.util.Type;
import espresso.util.Symbol;
import espresso.util.MethodType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.util.ExceptionStack;
import espresso.syntaxtree.CompilationUnitNode;

public class Espresso {

	/**
	  * Initial size of the symbol table. Other interesing
	  * primes are 211, 307, 401, 503, 601, 701, 809, 907.
	  */
	final static int TABLE_SIZE = 503;
	  
	/**
	  * A string vector that collects errors from the various compilation
	  * phases: parsing, type checking, etc. 
	  */
	Vector errors_d = new Vector();


        /**
 	  * A reference to the Exception stack, used for type checking
	  * thrown exceptions.
	  */
        ExceptionStack exceptionStack_d = null;

	/**
	  * A reference to a JavaParser object.
	  */
	JavaParser parser_d = null;
	
	/**
	  * A reference to the symbol table.
	  */
	SymbolTable symbolTable_d = null;

	/**
	  * A reference to the symbol table.
	  */
	JavaImportManager importMgr_d = null;

	/** 
	  * A pointer to the AST root node.
	  */
	CompilationUnitNode root_d = null;

	/** 
	  * Keep the name of the type that is being compiled.
	  */
	Symbol currentClass_d = null;

	/** 
	  * Keep the name of the source file that is being compiled.
	  */
	String fileName_d = null;

	/**
	  * A static reference to the main object.
	  */
	static Espresso instance_d = null;

	/**
	  * The only constructor.
	  */
	public Espresso() {
		if (instance_d == null) {
			instance_d = this;
		}
		else {
			internalError();
		}		
	}

	/**
	  *	Define entries in the symbol table for all the primops. This entries
	  * will be used by the type checker. The entries in the symbol table
	  * must be sorted from the widest type (e.g, double) to the narrowest 
	  * (e.g, int).
	  */
	private void defineInitEnv() {

		MethodType I_I = new MethodType(Type.Int, Type.Int);
		MethodType L_L = new MethodType(Type.Long, Type.Long);
		MethodType F_F = new MethodType(Type.Float, Type.Float);
		MethodType D_D = new MethodType(Type.Double, Type.Double);

		MethodType II_I = new MethodType(Type.Int, Type.Int, Type.Int);
		MethodType LI_L = new MethodType(Type.Long, Type.Long, Type.Int);
		MethodType JJ_J = new MethodType(Type.Long, Type.Long, Type.Long);
		MethodType FF_F = new MethodType(Type.Float, Type.Float, Type.Float);
		MethodType DD_D = new MethodType(Type.Double, Type.Double, Type.Double);

		MethodType II_Z = new MethodType(Type.Boolean, Type.Int, Type.Int);
		MethodType JJ_Z = new MethodType(Type.Boolean, Type.Long, Type.Long);
		MethodType FF_Z = new MethodType(Type.Boolean, Type.Float, Type.Float);
		MethodType DD_Z = new MethodType(Type.Boolean, Type.Double, Type.Double);
		MethodType ZZ_Z = new MethodType(Type.Boolean, Type.Boolean, Type.Boolean);
		
		// Insert entries for +, -, *, /, % with type (DD)D	
		symbolTable_d.addPrimop(new Symbol("+"),  DD_D);
		symbolTable_d.addPrimop(new Symbol("-"),  DD_D);
		symbolTable_d.addPrimop(new Symbol("*"),  DD_D);
		symbolTable_d.addPrimop(new Symbol("/"),  DD_D);
		symbolTable_d.addPrimop(new Symbol("%"),  DD_D);
		symbolTable_d.addPrimop(new Symbol("+="), DD_D);
		symbolTable_d.addPrimop(new Symbol("-="), DD_D);
		symbolTable_d.addPrimop(new Symbol("*="), DD_D);
		symbolTable_d.addPrimop(new Symbol("/="), DD_D);
		symbolTable_d.addPrimop(new Symbol("%="), DD_D);

		// Insert entries for +, -, *, /, % with type (FF)F
		symbolTable_d.addPrimop(new Symbol("+"),  FF_F);
		symbolTable_d.addPrimop(new Symbol("-"),  FF_F);
		symbolTable_d.addPrimop(new Symbol("*"),  FF_F);
		symbolTable_d.addPrimop(new Symbol("/"),  FF_F);
		symbolTable_d.addPrimop(new Symbol("%"),  FF_F);
		symbolTable_d.addPrimop(new Symbol("+="), FF_F);
		symbolTable_d.addPrimop(new Symbol("-="), FF_F);
		symbolTable_d.addPrimop(new Symbol("*="), FF_F);
		symbolTable_d.addPrimop(new Symbol("/="), FF_F);
		symbolTable_d.addPrimop(new Symbol("%="), FF_F);
		
		// Insert entries for +, -, *, /, %, &, |, ^ with type (JJ)J
		symbolTable_d.addPrimop(new Symbol("+"),  JJ_J);
		symbolTable_d.addPrimop(new Symbol("-"),  JJ_J);
		symbolTable_d.addPrimop(new Symbol("*"),  JJ_J);
		symbolTable_d.addPrimop(new Symbol("/"),  JJ_J);
		symbolTable_d.addPrimop(new Symbol("%"),  JJ_J);
		symbolTable_d.addPrimop(new Symbol("&"),  JJ_J);
		symbolTable_d.addPrimop(new Symbol("|"),  JJ_J);
		symbolTable_d.addPrimop(new Symbol("^"),  JJ_J);
		symbolTable_d.addPrimop(new Symbol("+="), JJ_J);
		symbolTable_d.addPrimop(new Symbol("-="), JJ_J);
		symbolTable_d.addPrimop(new Symbol("*="), JJ_J);
		symbolTable_d.addPrimop(new Symbol("/="), JJ_J);
		symbolTable_d.addPrimop(new Symbol("%="), JJ_J);
		symbolTable_d.addPrimop(new Symbol("&="), JJ_J);
		symbolTable_d.addPrimop(new Symbol("|="), JJ_J);
		symbolTable_d.addPrimop(new Symbol("^="), JJ_J);

		// Insert entries for +, -, *, /, %, &, |, ^ with type (II)I
		symbolTable_d.addPrimop(new Symbol("+"),  II_I);
		symbolTable_d.addPrimop(new Symbol("-"),  II_I);
		symbolTable_d.addPrimop(new Symbol("*"),  II_I);
		symbolTable_d.addPrimop(new Symbol("/"),  II_I);
		symbolTable_d.addPrimop(new Symbol("%"),  II_I);
		symbolTable_d.addPrimop(new Symbol("&"),  II_I);
		symbolTable_d.addPrimop(new Symbol("|"),  II_I);
		symbolTable_d.addPrimop(new Symbol("^"),  II_I);
		symbolTable_d.addPrimop(new Symbol("+="), II_I);
		symbolTable_d.addPrimop(new Symbol("-="), II_I);
		symbolTable_d.addPrimop(new Symbol("*="), II_I);
		symbolTable_d.addPrimop(new Symbol("/="), II_I);
		symbolTable_d.addPrimop(new Symbol("%="), II_I);
		symbolTable_d.addPrimop(new Symbol("&="), II_I);
		symbolTable_d.addPrimop(new Symbol("|="), II_I);
		symbolTable_d.addPrimop(new Symbol("^="), II_I);

		// Insert entries for ==, !=, &, |, ^ with type (ZZ)Z
		symbolTable_d.addPrimop(new Symbol("=="), ZZ_Z);
		symbolTable_d.addPrimop(new Symbol("!="), ZZ_Z);
		symbolTable_d.addPrimop(new Symbol("&"),  ZZ_Z);
		symbolTable_d.addPrimop(new Symbol("|"),  ZZ_Z);
		symbolTable_d.addPrimop(new Symbol("^"),  ZZ_Z);
		symbolTable_d.addPrimop(new Symbol("&="), ZZ_Z);
		symbolTable_d.addPrimop(new Symbol("|="), ZZ_Z);
		symbolTable_d.addPrimop(new Symbol("^="), ZZ_Z);
		
		// Insert entries for ==, !=, <, <=, >, >= with type (DD)Z
		symbolTable_d.addPrimop(new Symbol("=="), DD_Z);
		symbolTable_d.addPrimop(new Symbol("!="), DD_Z);
		symbolTable_d.addPrimop(new Symbol("<"),  DD_Z);
		symbolTable_d.addPrimop(new Symbol("<="), DD_Z);
		symbolTable_d.addPrimop(new Symbol(">"),  DD_Z);
		symbolTable_d.addPrimop(new Symbol(">="), DD_Z);

		// Insert entries for ==, !=, <, <=, >, >= with type (FF)Z
		symbolTable_d.addPrimop(new Symbol("=="), FF_Z);
		symbolTable_d.addPrimop(new Symbol("!="), FF_Z);
		symbolTable_d.addPrimop(new Symbol("<"),  FF_Z);
		symbolTable_d.addPrimop(new Symbol("<="), FF_Z);
		symbolTable_d.addPrimop(new Symbol(">"),  FF_Z);
		symbolTable_d.addPrimop(new Symbol(">="), FF_Z);

		// Insert entries for ==, != with type (JJ)Z
		symbolTable_d.addPrimop(new Symbol("=="), JJ_Z);
		symbolTable_d.addPrimop(new Symbol("!="), JJ_Z);
		symbolTable_d.addPrimop(new Symbol("<"),  JJ_Z);
		symbolTable_d.addPrimop(new Symbol("<="), JJ_Z);
		symbolTable_d.addPrimop(new Symbol(">"),  JJ_Z);
		symbolTable_d.addPrimop(new Symbol(">="), JJ_Z);

		// Insert entries for ==, != with type (II)Z
		symbolTable_d.addPrimop(new Symbol("=="), II_Z);
		symbolTable_d.addPrimop(new Symbol("!="), II_Z);
		symbolTable_d.addPrimop(new Symbol("<"),  II_Z);
		symbolTable_d.addPrimop(new Symbol("<="), II_Z);
		symbolTable_d.addPrimop(new Symbol(">"),  II_Z);
		symbolTable_d.addPrimop(new Symbol(">="), II_Z);

		// Insert entries for <<, >>, >>> with types (LI)L  
		symbolTable_d.addPrimop(new Symbol("<<"),   LI_L);
		symbolTable_d.addPrimop(new Symbol(">>"),   LI_L);
		symbolTable_d.addPrimop(new Symbol(">>>"),  LI_L);
		symbolTable_d.addPrimop(new Symbol("<<="),  LI_L);
		symbolTable_d.addPrimop(new Symbol(">>="),  LI_L);
		symbolTable_d.addPrimop(new Symbol(">>>="), LI_L);
		
		// Insert entries for <<, >>, >>> with types (II)I
		symbolTable_d.addPrimop(new Symbol("<<"),   II_I);
		symbolTable_d.addPrimop(new Symbol(">>"),   II_I);
		symbolTable_d.addPrimop(new Symbol(">>>"),  II_I);
		symbolTable_d.addPrimop(new Symbol("<<="),  II_I);
		symbolTable_d.addPrimop(new Symbol(">>="),  II_I);
		symbolTable_d.addPrimop(new Symbol(">>>="), II_I);

		// Insert entries for unary - and + with type (D)D
		symbolTable_d.addPrimop(new Symbol("u+"),  D_D);
		symbolTable_d.addPrimop(new Symbol("u-"),  D_D);

		// Insert entries for unary - and + with type (F)F
		symbolTable_d.addPrimop(new Symbol("u+"),  F_F);
		symbolTable_d.addPrimop(new Symbol("u-"),  F_F);

		// Insert entries for ~, unary - and + with type (L)L
		symbolTable_d.addPrimop(new Symbol("~"),   L_L);
		symbolTable_d.addPrimop(new Symbol("u+"),  L_L);
		symbolTable_d.addPrimop(new Symbol("u-"),  L_L);

		// Insert entries for ~, unary - and + with type (I)I
		symbolTable_d.addPrimop(new Symbol("~"),   I_I);
		symbolTable_d.addPrimop(new Symbol("u+"),  I_I);
		symbolTable_d.addPrimop(new Symbol("u-"),  I_I);

	}
	
	/**
	  * Espresso's main loop.
	  */
	public void execute(String args[]) {
		System.out.println("Espresso v0.4 - Copyright (C) 1998 Boston University");
		
		if (args.length == 1) {
			try {
				FileInputStream file = new FileInputStream(args[0]);
				fileName_d = args[0];

				// Create intances of the different members
				
				exceptionStack_d = new ExceptionStack();

				symbolTable_d 	= new SymbolTable(TABLE_SIZE, 0.75f);
				importMgr_d 	= new JavaImportManager(symbolTable_d);
				parser_d       	= new JavaParser(file, importMgr_d, symbolTable_d, errors_d);

				// Fill the symbol table with the init env
				defineInitEnv();

				// Start compiling the program
				
				root_d = parser_d.parseProgram();
				if (root_d != null) {
					// System.out.println("\n" + symbolTable_d.toString() + "\n");
					
					// Type check the AST and if success, generate code
					try {
						root_d.typeCheck(symbolTable_d);
						root_d.translate();
					} 
					catch (TypeCheckError e) {
						errors_d.addElement(e.toString());		// last exception raised
						printErrors();
					}				
				}
				else {
					printErrors();
				}
			} 
			catch (ParseException e) {
                            e.printStackTrace();
				System.out.println("Errors:");
				System.out.println("  Syntax error.");
				System.exit(1);
			} 
			catch (java.io.FileNotFoundException e) {
				System.out.println("Errors:");
				System.out.println("  File '" + args[0] + "' not found.");
				System.exit(1);
			}
		} 
		else {
			System.out.println("usage: espresso file.java");
			System.exit(1);
		}
	
	}

	/**
	 *	Aborts the execution of the compiler as a result of an 
	 *  unrecoverable error.
	 */
	public static void internalError() {
		System.err.println("Oops !! Internal error.");

		// Raise a null pointer exeception
		Espresso e = null; e.parser_d = null;

		System.exit(1);
	}

	/**
	 *	Aborts the execution of the compiler if something found
	 *  in the source file can't be compiled.
	 */
	public static void notYetImplemented() {
		System.err.println("Sorry, feature not yet implemented.");
		System.exit(1);
	}

	private void printErrors() {
		System.err.println("Errors:");
		int size = errors_d.size();
		for (int i = 0; i < size; i++) {
			System.err.println("  " + errors_d.elementAt(i));
		}
		System.exit(1);
	}
	
	public static Vector errors() {
		return instance_d.errors_d;
	}
	
        public static ExceptionStack exceptionStack() {
	        return instance_d.exceptionStack_d;
	}

	public static SymbolTable symbolTable() {
		return instance_d.symbolTable_d;
	}
	
	public static String fileName() {
	    return instance_d.fileName_d;
	}

	public static JavaImportManager importMgr() {
		return instance_d.importMgr_d;
	}

	public static Symbol currentPackage() {
		CompilationUnitNode root = instance_d.root_d;
		return (root != null) ? root.packName_d : null;	
	}
	
	public static void currentClass(Symbol name) {
		instance_d.currentClass_d = name;
	}

	public static Symbol currentClass() {
		return instance_d.currentClass_d;
	}

	/**
	  * Entry point.
	  */
	public static void main(String args[]) {
		Espresso espresso = new Espresso();
		espresso.execute(args);		
	}

}
