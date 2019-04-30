/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Nick Eskelinen
 * Date: 05/01/1999
 *
 */

package espresso.util;

import espresso.syntaxtree.SyntaxTreeNode;
import espresso.syntaxtree.LocalVarDeclarationNode;

import espresso.util.Type;
import espresso.util.ClassType;
import espresso.util.ErrorMsg;
import espresso.util.TypeCheckError;

import java.util.Stack;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class ExceptionStack {

    public static final Type CLASS_THROWABLE = 
	new ClassType("java.lang.Throwable");

    public Stack caught_s = null;
    public Stack thrown_s = null;

    public Vector caught = null;
    public Hashtable thrown = null;

    public ExceptionStack() {
	clear();
    }

    public void clear() {
	caught_s = new Stack();
	thrown_s = new Stack();
	caught = new Vector(5);
	thrown = new Hashtable(13);
    }

    public void pushLevel() {

	caught_s.push(caught);
	thrown_s.push(thrown);

	caught = new Vector(5);
	thrown = new Hashtable(13);	

    }

    public void popLevel() {

	caught = (Vector) caught_s.pop();
	thrown = (Hashtable) thrown_s.pop();

    }

    public void catches(Vector exceps) {

	int n = exceps.size();
	for (int i = 0; i < n; i++) {
	    Type t;
	    Object obj = exceps.elementAt(i);

	    if (obj instanceof String)
		t = new ClassType((String) obj);
	    else if (obj instanceof Symbol)
		t = new ClassType((Symbol) obj);
	    else if (obj instanceof Type)
		t = (Type) obj;
	    else if (obj instanceof LocalVarDeclarationNode)
		t = ((LocalVarDeclarationNode) obj).type_d;
	    else
		throw new IllegalArgumentException
		    (obj.getClass() +
		     " Vector received, " + 
		     "Type Vector expected");

	    caught.addElement(t);
	}
    }

    public void add_throw(Type t, SyntaxTreeNode node) {
	thrown.put(t, node);
    }

    public void add_throws(Hashtable table) {
	for (Enumeration e = table.keys(); e.hasMoreElements(); ) {
	    Object excep = e.nextElement();
	    Object node = table.get(excep);
	    thrown.put(excep, node);
	}
    }

    public void add_throws(Vector exceps, SyntaxTreeNode node) {

	int n = exceps.size();

	for (int i = 0; i < n; i++) {
	    Object obj = exceps.elementAt(i);
	    Type excep;

	    if (obj instanceof String)
		excep = new ClassType((String) obj);
	    else if (obj instanceof Symbol)
		excep = new ClassType((Symbol) obj);
	    else if (obj instanceof Type)
		excep = (Type) obj;
	    else
		throw new IllegalArgumentException
		    (obj.getClass() +
		     " Vector received, String/Symbol/Type Vector expected");

	    //TODO: make sure excep extends "Throwable"

	    thrown.put(excep, node);
	}
    }

    public Hashtable uncaught() {

	Hashtable uc = new Hashtable(13);

	// for every unique exception thrown in this block
	for (Enumeration e = thrown.keys(); e.hasMoreElements() ;) {
	    Type thrown_d = (Type) e.nextElement();

	    // see if it is caught
	    int n = caught.size();
	    int i;
	    for (i = 0; i < n; i++) {
		Type caught_d = (Type) caught.elementAt(i);
		if (thrown_d.subTypeOf(caught_d))
		    break;
	    }

	    // nope .. add to uncaught table
	    if (i >= n)
		uc.put(thrown_d, thrown.get(thrown_d));
	}

	return uc;
    }

    public Vector unthrown() {

	Vector ut = (Vector) caught.clone();

	// for every Type thrown ...
	for (Enumeration e = thrown.keys(); 
	     e.hasMoreElements() && !ut.isEmpty() ;) {

	    Type thrown_d = (Type) e.nextElement();

	    // Remove every superclass of thrown_d from ut
	    for (int i = 0; i < ut.size(); ) {
		Type caught_d = (Type) ut.elementAt(i);
		if (caught_d.superTypeOf(thrown_d))
		    ut.removeElementAt(i);
		else
		    i++;
	    }

	}

	return ut;
    }
}
