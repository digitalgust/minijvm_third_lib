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

import espresso.Espresso;
import espresso.parser.JavaImportManager;
import espresso.syntaxtree.TypeDeclarationNode;
import espresso.syntaxtree.ClassDeclarationNode;
import espresso.syntaxtree.InterfaceDeclarationNode;
import espresso.syntaxtree.MethodDeclarationNode;

import espresso.classfile.Constants;
import espresso.classfile.classgen.*;

import java.util.Vector;

public class ClassType extends ReferenceType {

	Symbol name_d;
	TypeDeclarationNode node_d = null;

	Vector interfaces_d = null;
	ClassType superType_d = null;
	
	static Symbol root_d = new Symbol("java.lang.Object");
	static ClassType rootType_d = Type.createClassType(root_d);

	public ClassType(String name) {
		this(new Symbol(name));
	}

	public ClassType(Symbol name) {
		name_d = name;
	}

        public Symbol name() {
	        return name_d;
	}
	
	/**
	 * Make sure that this type is accessible using the symbol table.
	 * Note that the import manager caches read requests.
	 */
	void readClassFile() {
		SymbolTable stable = Espresso.symbolTable();

		// Check if loaded or defined in this CU
		node_d = stable.lookupType(name_d);

		if (node_d == null) {
			JavaImportManager jim = Espresso.importMgr();
			if (jim.readClassFile(name_d, false)) {
				node_d = stable.lookupType(name_d);
			}
		}
	}
	
	public boolean identicalTo(Type other) {
		boolean result = false;
		if (other instanceof ClassType) {
			ClassType ctype = (ClassType) other;
			result = name_d.equals(ctype.name_d);
		}
		return result;
	}

	public boolean isTopType() {
		readClassFile();					// Load classes on demand
		
		boolean result = false;
		if (node_d instanceof ClassDeclarationNode) {
			ClassDeclarationNode cnode_d = (ClassDeclarationNode) node_d;
			result = cnode_d.name_d.equals(root_d);
		}
		return result;
	}
	
	public ClassType superType() {

		if (superType_d == null) {
			SymbolTable stable = Espresso.symbolTable();
			node_d = stable.lookupType(name_d);

			if (node_d == null) {
				readClassFile();						// Load classes on demand
				node_d = stable.lookupType(name_d);
			}
								
			if (node_d instanceof ClassDeclarationNode) {
				ClassDeclarationNode cnode = (ClassDeclarationNode) node_d;
				superType_d = Type.createClassType(cnode.superName_d);
			}
			else if (node_d instanceof InterfaceDeclarationNode) {
				superType_d = rootType_d;
			}
		}

		return superType_d;
	}

	public Vector superInterfaces() {

		if (interfaces_d == null) {	
			SymbolTable stable = Espresso.symbolTable();
			TypeDeclarationNode node = stable.lookupType(name_d);

			if (node == null) {
				readClassFile();					// Load classes on demand
				node = stable.lookupType(name_d);
			}
					
			if (node != null) {
				interfaces_d = new Vector();		
				Vector interfaces = node.interfaces_d;
			
				int n = interfaces.size();
				for (int i = 0; i < n; i++) {
					Type type = Type.createClassType((Symbol) interfaces.elementAt(i));
					interfaces_d.addElement(type);				
				}
			}			
		}
		
		return interfaces_d;
	}
		
	public boolean subTypeOf(Type other) {		
	
		if (identicalTo(other)) {
			return true;
		}

		if (isTopType()) {
			return false;
		}
		
		if (superType().subTypeOf(other)) {
			return true;
		}
			
		Vector interfaces = superInterfaces();	
		if (interfaces != null) {
			int n = interfaces.size();
			for (int i = 0; i < n; i++) {
				Type itype = (Type) interfaces.elementAt(i);
				if (itype.subTypeOf(other)) {
					return true;
				}
			}
		}
		
		return false;
	}
	
	public boolean superTypeOf(Type other) {
		return other.subTypeOf(this);
	}

	public int distanceTo(Type other) {
		
		if (!(other instanceof ClassType)) {
			return Integer.MIN_VALUE;
		}

		if (identicalTo(other)) {
			return 0;
		}

		if (isTopType()) {
			return Integer.MIN_VALUE;
		}
		
		int distance = superType().distanceTo(other);			
		if (distance >= 0) {
			return distance + 1;
		}
			
		Vector interfaces = superInterfaces();
		if (interfaces != null) {
			int n = interfaces.size();
			for (int i = 0; i < n; i++) {
				Type itype = (Type) interfaces.elementAt(i);
				distance = itype.distanceTo(other);
				if (distance >= 0) {
					return distance + 1;
				}
			}
		}

		return Integer.MIN_VALUE;	
	}

	/** 
	  * Search for a field in this type or in any of its super types
	  * (both classes and interfaces). Returns a fully qualified name.
	  */
	public Symbol findField(Symbol fname) throws AmbiguousName {
		readClassFile();					// Load classes on demand

		Symbol qname = new Symbol(name_d, fname);
		SymbolTable stable = Espresso.symbolTable();
		
		if (stable.lookupField(qname) != null) {
			return qname;
		}
		
		if (isTopType()) {
			return null;
		}

		// Search for the name in my super class 
		
		ClassType superType = superType();
		qname = (superType != null) ? superType.findField(fname) : null;
			
		// Check for ambiguities by looking in the interfaces
		
		Vector interfaces = superInterfaces();
		if (interfaces != null) {
			int n = interfaces.size();
			for (int i = 0; i < n; i++) {
				ClassType ctype = (ClassType) interfaces.elementAt(i);
				Symbol iname = ctype.findField(fname);
				if (iname != null) {
					if (qname == null) {
						return iname;
					}
					else {
						throw new AmbiguousName(fname);
					}
				}
			}
		}

		return qname;
	}

	/** 
	  * Search for a method in this type or in any of its super types
	  * Returns the closest unique occurrence of a method type, that is
	  * most specific, or null if no match was established
	  * If the most specific method type is not unique, than we have 2 cases :
	  * - if the 2 methods are defined in the same class, than an ambiguous
	  *   type error is thrown
	  * - if the 2 mthods are defined in different classes, than we have 2 sub cases
	  *		- if the 2 method types are identical, than the one closer to the
	  *		  leaf of the type hierarchy overrides the one higher up
	  *		- if the 2 method types are not identical, then a ambiguous type error is thrown
	  * If a more specific method type is encountered higher up in the type hierarchy,
	  * then this also results in an ambiguous type error
	  */

	public MethodDesc findMethod(MethodDesc mdesc) throws TypeCheckError {
		readClassFile();					// Load classes on demand

		MethodType mtype = mdesc.methodType();
		int distance = mdesc.distance();
		int nargs = mtype.argsCount(); // need new method in methodType
		int mindex = mdesc.methodIndex();
		Symbol classname = mdesc.className();
		MethodType candType = null;
		// counter for entries with same distance										
		int acount = mdesc.acount();	
		
		Symbol qmname = new Symbol(name_d, mdesc.methodName());
		SymbolTable stable = Espresso.symbolTable();

		Vector methods = stable.lookupMethod(qmname);
		if (methods != null) {
			int n = methods.size();
			for (int i =0; i < n; i++) {
				MethodDeclarationNode mnode = 
						(MethodDeclarationNode) methods.elementAt(i);
				if (((MethodType) mnode.type_d).argsCount() != nargs) {
					continue;
				}
				if (mdesc.candidateType() != null && 
					 mnode.type_d.identicalTo(mdesc.candidateType())) {
					continue;
				}
				// determine distance between signatures
				int cdistance = mtype.distanceTo(mnode.type_d);
				if (cdistance >= 0) {
					// found a match with shorter distance
					// remember method index, restart counting for ambiguous 
					if (distance < 0 || cdistance < distance) {			
						distance = cdistance;
						mindex = i;							
						acount = 1;	
						classname = name_d;
						candType = 	(MethodType) mnode.type_d;	
					}
					else if (cdistance == distance) {
					// we found another match with the same distance
						acount++;							
					}
				}
			}
		}
		// Check for mbiguities
		if (acount > 1) {
			throw new TypeCheckError(mdesc.node());
		}
		// check whether we found a more specific match superclass
		if ((mdesc.distance() >= 0) && (distance < mdesc.distance()))  {
			throw new TypeCheckError(mdesc.node());
		} 
		// we found a perfect match
		if (distance == 0) {
				mdesc.setMethodIndex(mindex);
				mdesc.setClassName(classname);	
				return mdesc;
		}

		if (isTopType()) {
			// if we reached Object, and had no match, return null
			if (distance < 0) {
				return null;
			}
			// Otherwise, return whatever we got
			else {
				// return whatever is current
				mdesc.setMethodIndex(mindex);
				mdesc.setClassName(classname);	
				return mdesc;
			}
		}
		else {
			// search further up
			mdesc.setClassName(classname);
			mdesc.setMethodIndex(mindex);
			mdesc.setDistance(distance);
			mdesc.setAcount(acount);
			if (candType != null) {
			   mdesc.setCandidateType(candType);
			}
			return  superType().findMethod(mdesc);
		}
	}

        public boolean throwable() {
	    return subTypeOf(Type.Throwable);
	}

	public String toString() {
		return "L" + name_d.toString() + ";";
	}	

	/**
	  * Map an Espresso type to a ClassGen one.
	  */
	public ClassGenType toClassFileType() {
		return new ClassGenType(name_d.toInternalString());
	}

	public Instruction IFEQ(boolean tozero) {
		return tozero ? (Instruction) new IFNULL(null) : 
						(Instruction) new IF_ACMPEQ(null);	
	}

	public Instruction IFNE(boolean tozero) {
		return tozero ? (Instruction) new IFNONNULL(null) : 
						(Instruction) new IF_ACMPNE(null);	
	}

	public InstructionList VALUEOF(ConstantPoolGen cpg) {
		Symbol mstring = new Symbol("java.lang.String");
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");

		InstructionList il = new InstructionList();
		if ( !name_d.equals( mstring ) ) {
			Symbol mname = new Symbol("valueOf");
			MethodType mtype = new MethodType(new ClassType(mstring),
												  new ClassType("java.lang.Object"));
			int index = cpg.addMethodref(mstring.toInternalString(), 
			   							mname.toString(), mtype.toInternalString());
			il.append(new INVOKESTATIC(index));				
		}
		MethodType mtype = new MethodType(Type.Void,new ClassType("java.lang.String"));
		int index = cpg.addMethodref(mbuffer.toInternalString(), "<init>", 
		mtype.toInternalString());
		il.append(new INVOKESPECIAL(index));
		return il;			
	}
	
	public Instruction APPENDSTB(ConstantPoolGen cpg) {
		Symbol mstring = new Symbol("java.lang.String");
		Symbol mbuffer = new Symbol("java.lang.StringBuffer");

		MethodType mtype;
		if ( name_d.equals(mstring) ) {
				mtype = new MethodType(new ClassType(mbuffer), new ClassType(mstring));
		}
		else {
			mtype = new MethodType(new ClassType(mbuffer), new ClassType("java.lang.Object"));
		}
		int index = cpg.addMethodref(mbuffer.toInternalString(), "append", 
		mtype.toInternalString());
		return new INVOKEVIRTUAL(index);	
	}
}
