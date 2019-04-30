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

import espresso.util.Symbol;
import espresso.syntaxtree.SyntaxTreeNode;
import espresso.syntaxtree.CompilationUnitNode;
import espresso.syntaxtree.FormalParameterNode;
import espresso.syntaxtree.TypeDeclarationNode;
import espresso.syntaxtree.FieldDeclarationNode;
import espresso.syntaxtree.LabeledStatementNode;
import espresso.syntaxtree.MethodDeclarationNode;
import espresso.syntaxtree.LocalVarDeclarationNode;
import espresso.syntaxtree.ConstructorDeclarationNode;

import java.util.Vector;
import java.util.Hashtable;

public class SymbolTable extends Hashtable {

	static final int NUMBER_METHODS = 3;
	
	public SymbolTable() {
		super();
	}
	
	public SymbolTable(int size, float factor) {
		super(size, factor);
	}
	
	/**
	  * Adds a package declaration to the symbol table. The name should 
	  * be fully qualified.
	  */
	public CompilationUnitNode addPackage(Symbol name, CompilationUnitNode node) {
		return (CompilationUnitNode) put(name, node);
	}
	
	/**
	  * Looks up a package declaration in the symbol table. If the name 
	  * is not present or does not correspond to a package null is returned.
	  */
	public CompilationUnitNode lookupPackage(Symbol name) {
		Object obj = get(name);
		return (obj instanceof CompilationUnitNode) ? (CompilationUnitNode) obj 
			: null;
	}

	/**
	  * Adds a class or an interface name to the symbol table. Names should 
	  * be fully qualified. 
	  */
	public TypeDeclarationNode addType(Symbol name, TypeDeclarationNode node) {
		Object obj = put(name, node);
		return (obj instanceof TypeDeclarationNode) ? null : (TypeDeclarationNode) obj;
	}
	
	/**
	  * Looks up a class or interface declaration in the symbol table. If 
	  * the name is not present or does not correspond to a class or an
	  * interface then null is returned.
	  */
	public TypeDeclarationNode lookupType(Symbol name) {
		Object obj = get(name);
		return (obj instanceof TypeDeclarationNode) ? (TypeDeclarationNode) obj 
			: null;
	}

	/**
	  * Adds a field name to the symbol table. Names should be fully 
	  * qualified. A forward declaration may be resolved.
	  */
	public FieldDeclarationNode addField(Symbol name, FieldDeclarationNode node) {
		Object obj = put(name, node);
		return (obj instanceof FieldDeclarationNode) ? null : (FieldDeclarationNode) obj;
	}
	
	/**
	  * Looks up a field declaration in the symbol table. If the name
	  * is not present or does not correspond to a field, null is returned.
	  */
	public FieldDeclarationNode lookupField(Symbol name) {
		Object obj = get(name);
		return (obj instanceof FieldDeclarationNode) ? (FieldDeclarationNode) obj 
			: null;
	}

	/**
	  * Adds a method name to the symbol table. Names should be fully 
	  * qualified. A key is built by appending "()" to the name. A vector 
	  * is kept for each name (method overloading).
	  */
	public Vector addMethod(Symbol name, MethodDeclarationNode node) {
		Symbol mname = new Symbol(name, "()");
		Vector methods = (Vector) get(mname);

		if (methods == null) {
			methods = new Vector(NUMBER_METHODS);
		}
		methods.addElement(node);

		return (Vector) put(mname, methods);
	}
	
	/**
	  * Looks up a method declaration in the symbol table. A key is built by 
	  * appending "()" to the name. If the name is not present or does not 
	  * correspond to a method, null is returned.
	  */
	public Vector lookupMethod(Symbol name) {
		Symbol mname = new Symbol(name, "()");
		Object obj = get(mname);
		return (obj instanceof Vector) ? (Vector) obj : null;
	}

	/**
	  * Adds a formal param to the symbol table. A name must be qualified
	  * with the method (that also includes package and class) and a 
	  * unique index identifying outermost scope.
	  */
	public FormalParameterNode addFormal(Symbol name, 
		FormalParameterNode node) {
		return (FormalParameterNode) put(name, node);
	}
	
	/**
	  * Looks up a formal parameter in the symbol table. If the name is
	  * not present or does not correspond to a field, null is returned.
	  */
	public FormalParameterNode lookupFormal(Symbol name) {
		Object obj = get(name);
		return (obj instanceof FormalParameterNode) ? (FormalParameterNode) obj 
			: null;
	}

	/**
	  * Adds a local var to the symbol table. A name must be qualified
	  * with the method (that also includes package and class) and a 
	  * unique index identifying the scope.
	  */
	public LocalVarDeclarationNode addLocal(Symbol name, 
		LocalVarDeclarationNode node) {
		return (LocalVarDeclarationNode) put(name, node);
	}
	
	/**
	  * Looks up a local var in the symbol table. If the name is
	  * not present or does not correspond to a field, null is returned.
	  */
	public LocalVarDeclarationNode lookupLocal(Symbol name) {
		Object obj = get(name);
		return (obj instanceof LocalVarDeclarationNode) ? 
			(LocalVarDeclarationNode) obj : null;
	}

	/**
	  * Adds a primop name to the symbol table. A vector is kept for 
	  * each name (overloading).
	  */
	public Vector addPrimop(Symbol name, MethodType mtype) {
		Vector methods = (Vector) get(name);

		if (methods == null) {
			methods = new Vector(NUMBER_METHODS);
		}
		methods.addElement(mtype);

		return (Vector) put(name, methods);
	}
	
	/**
	  * Looks up a primop in the symbol table. If the name is not present 
	  * or does not correspond to a primop, null is returned.
	  */
	public Vector lookupPrimop(Symbol name) {
		Object obj = get(name);
		return (obj instanceof Vector) ? (Vector) obj : null;
	}

	/**
	  * Adds a statement label to the symbol table. Appends a ":" to avoid
	  * name clashes.
	  */
	public LabeledStatementNode addLabel(Symbol name, LabeledStatementNode node) {
		Symbol sname = new Symbol(name, ":");
		return (LabeledStatementNode) put(sname, node);
	}
	
	/**
	  * Looks up a statement label in the symbol table. Appends ":" to the
	  * name before searching.
	  */
	public LabeledStatementNode lookupLabel(Symbol name) {
		Symbol mname = new Symbol(name, ":");
		Object obj = get(mname);
		return (obj instanceof LabeledStatementNode) ? (LabeledStatementNode) obj : null;
	}

}
