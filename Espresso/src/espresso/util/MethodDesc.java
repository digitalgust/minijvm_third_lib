/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 4/15/98
 *
 * This class used used during typeChecking of Methodinvocation
 * to pass the information collected so far up the type hierarchy.
 *
 */

package espresso.util;

import espresso.syntaxtree.SyntaxTreeNode;

import java.util.Vector;
  
public class MethodDesc {

	/**
      * Signature of method we try to match (input).
	  */
	MethodType methodType_d;	

	/**
   	  * Unqualified method name (input).
	  */	
	Symbol methodName_d;

	/**
	  * Class/type where we currently try to match (output).
	  */	
	Symbol className_d;

	/** 
	  * Candidate type for method.
	  */
	MethodType candidateType_d;

	/**
	  * Best distance we encountered so far (input/output).
	  */
	int distance_d;

	/**
	  * Index into method table of current best match (output).
	  */
	int methodIndex_d;

	/**
      * Counter of ambiguous occurencies.
      */
	int acount_d;

	/** 
      * Node from which findMethod was called initially.
	  */
	SyntaxTreeNode node_d;
	
	public MethodDesc(Symbol methodName, MethodType methodType, SyntaxTreeNode node) {
		methodName_d = methodName;
		methodType_d = methodType;
		node_d = node;
		methodIndex_d = -1;
		distance_d = Integer.MIN_VALUE;
		acount_d = 0;
	}

	public Symbol className() {
		return className_d;
	}

	public Symbol methodName() {
		return methodName_d;
	}
	
	public MethodType methodType() {
		return methodType_d;
	}
	
	public int distance() {
		return distance_d;
	}

	public int methodIndex() {
		return methodIndex_d;
	}

	public int acount() {
		return acount_d;
	}

	public SyntaxTreeNode node() {
		return node_d;
	}

	public MethodType candidateType() {
		return candidateType_d;
	}
	
	public void setClassName(Symbol className) {
		className_d = className;
	}

	public void setDistance(int distance) {
		distance_d = distance;
	}

	public void setMethodIndex(int mindex) {
		methodIndex_d = mindex;
	}
	
	public void setAcount(int acount) {
		acount_d = acount;
	}
	
	public void setCandidateType(MethodType mtype) {
		candidateType_d = mtype;
	}

}
