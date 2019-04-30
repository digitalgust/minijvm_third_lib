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

package espresso.syntaxtree;

import espresso.Espresso;

import espresso.util.Type;
import espresso.util.ErrorMsg;
import espresso.util.ClassType;
import espresso.util.VoidType;
import espresso.util.ArrayType;
import espresso.util.MethodType;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.ExceptionStack;
import espresso.util.TypeCheckError;

import espresso.classfile.Constants;
import espresso.classfile.classgen.*;
import espresso.classfile.javaclass.Method;
import espresso.classfile.javaclass.ConstantNameAndType;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;

public class MethodDeclarationNode extends ClassBodyDeclarationNode {

	public static int NUMBER_FORMALS = 3;
	
	public Symbol name_d;
	public Vector formals_d;			// vector of FormalParameterNodes
	public Vector exceptions_d;			// vector of Symbols (nullable)
	public BlockNode block_d;			// block nullable (in case of abstract)
	
	public Integer scope_d = null;		// id of the main scope

	public MethodDeclarationNode() {
	}

	/**
	  * Move the ['s defined on the method name to the type and encode 
	  * param types with return types. 
	  */
	public void encodeType() {
		String name = name_d.toString();

		int dims = 0;
		int last = name.length() - 1;
		while (last > 0 && name.charAt(last) == '[') {
			dims++; last--;	
		}

		Type resultType = type_d;
	
		if (dims > 0) {		
			Type baseType = resultType;
					
			if (type_d instanceof ArrayType) {
				ArrayType atype = (ArrayType) type_d;
				dims += atype.dims();
				baseType = atype.baseType();
			}		

			resultType = new ArrayType(baseType, dims);
			name_d = new Symbol(name.substring(0, last + 1));
		}

		Vector argsType = new Vector(NUMBER_FORMALS);

        int n = formals_d.size();    
        for (int i = 0; i < n; i++) {
            FormalParameterNode fml = (FormalParameterNode) formals_d.elementAt(i);
            argsType.addElement(fml.type_d);
        }
		
		type_d = new MethodType(resultType, argsType);
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

	    Vector errors = Espresso.errors();
	    TypeCheckError last = null;

	    ExceptionStack es = Espresso.exceptionStack();
	    es.clear();

	    if (exceptions_d != null) {
		for (int i = 0; i < exceptions_d.size(); i++) {

		    Type t = new ClassType
			((Symbol) exceptions_d.elementAt(i));

		    if (!t.throwable()) {
			if (last != null) 
			    errors.addElement(last.toString());
		
			last = new TypeCheckError
			    (new ErrorMsg(ErrorMsg.UNTHRWBL_ERR,
					  t.name(),
					  this));
		    }
		}

		es.catches(exceptions_d);
	    }

	    if (block_d != null) {
		block_d.typeCheck(stable);
	    }

	    Hashtable uncaught = es.uncaught();
	    for (Enumeration e = uncaught.keys(); e.hasMoreElements() ;) {
		ClassType t = (ClassType) e.nextElement();
		if ( !(t.subTypeOf(Type.Error) || 
		       t.subTypeOf(Type.RuntimeException) )) {

		    if (last != null)
			errors.addElement(last.toString());

		    last = new TypeCheckError
			(new ErrorMsg(ErrorMsg.UNCAUGHT_ERR,
				      t.name(),
				      (SyntaxTreeNode) uncaught.get(t)));
		}
	    }			

	    if (last != null)
		throw last;

	    /* We can complain if there are types in the throws clause which
	     * are not actually thrown, but this is overkill (JavaC doesn't
	     * even do this)
	     */
	    //Vector unthrown = es.unthrown();

		return Type.Void;
		

	}

	protected String[] argsNames() {
		if (formals_d == null || formals_d.size() == 0 ) {
			return null;
		}
		else {
			String [] formals = new String[formals_d.size()];
			int n = formals_d.size();
			for (int i = 0; i < n; i++ ) {
				FormalParameterNode fml = (FormalParameterNode) formals_d.elementAt(i);
				formals[i] = fml.name_d.baseName(2);		// preserve scope
			}
			return formals;
		}
	}
	
	protected ClassGenType[] argsType() {
		if (formals_d == null || formals_d.size() == 0) {
			return null;
		}
		else {
			ClassGenType[] formaltypes = new ClassGenType[formals_d.size()];
			int n = formals_d.size();
			for (int i = 0; i < n; i++) {
				FormalParameterNode fml = (FormalParameterNode) formals_d.elementAt(i);
				formaltypes[i] = fml.type_d.toClassFileType();
			}
			return formaltypes;
		}
	}
	
	protected ClassGenType returnType() {
		return ((MethodType) type_d).resultType().toClassFileType();
	}

	protected boolean isReturn(Instruction in) {
		return (in instanceof RETURN  || in instanceof IRETURN ||
				in instanceof LRETURN || in instanceof FRETURN ||
				in instanceof DRETURN || in instanceof ARETURN);
 	}
		
	public void translate(ClassGen classGen) {
		ConstantPoolGen cpGen = classGen.getConstantPool();
		Symbol type = new Symbol(name_d.pathName());
		SyntaxTreeNode node = Espresso.symbolTable().lookupType(type);

		if (block_d == null || node instanceof InterfaceDeclarationNode) {
			int nindex = cpGen.addNameAndType(name_d.baseName(), type_d.toInternalString());
			ConstantNameAndType cnat = (ConstantNameAndType) cpGen.getConstant(nindex);
			classGen.addMethod(new Method(accessFlags_d, cnat.getNameIndex(), 
				cnat.getSignatureIndex(), null, cpGen.getConstantPool())); 
		}
		else {
			InstructionList il = new InstructionList();
			MethodGen method = new MethodGen(accessFlags_d, returnType(), argsType(), argsNames(),
				name_d.baseName(), name_d.pathName(), il, cpGen); 

			block_d.translate(classGen, method);
					
			/*
			 * If the last instruction in the list is not a return and the result
			 * type of the method is void, we add an explicit return. This is the
			 * best we can do without flow analysis.
			 */	
			InstructionHandle ih = il.getEnd();
			if (ih == null || !isReturn(ih.getInstruction())) {
				MethodType mtype = (MethodType) type_d;
				if (mtype.resultType() instanceof VoidType) {
					il.append(new RETURN());
				}
			}

			/*
 			 * Remove the NOPs from the instruction stream and add 
 			 * the method to the class.
			 */
			method.removeNOPs();
			classGen.addMethod(method.getMethod(10));		// stack size = 10
		}
	}	
}
