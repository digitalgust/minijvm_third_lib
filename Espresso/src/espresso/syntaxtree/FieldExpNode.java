/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 2/24/98
 *
 */

package espresso.syntaxtree;

import espresso.Espresso;
import espresso.util.Type;
import espresso.util.ArrayType;
import espresso.util.ClassType;
import espresso.util.BooleanType;
import espresso.util.ErrorMsg;
import espresso.util.NotConstant;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.AmbiguousName;
import espresso.util.TypeCheckError;

import espresso.classfile.Constants;
import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class FieldExpNode extends ExpressionNode {

	/**
	  * Name of the field being accessed.
	  */
	public Symbol name_d;
	
	/**
	  * A ref to the expression to the left of the dot. Must be of
	  * some ClassType.
	  */
	public ExpressionNode left_d;

	/**
	  * A reference to the AST node.
	  */	
	public FieldDeclarationNode node_d = null;
	
	/**
	  * The index in the fields vector where the name was found.
	  */
	public int index_d = -1;

	/**
	  * A ref to the AST node of the method where the field is being
	  * accessed. Null if called from a static initializer.
	  */
	public MethodDeclarationNode methodNode_d = null;

	public FieldExpNode(ExpressionNode left, Symbol name, MethodDeclarationNode mnode) {
		left_d = left;
		name_d = name;
		methodNode_d = mnode;
	}
	
	public FieldExpNode(ExpressionNode left, String name, MethodDeclarationNode mnode) {
		left_d = left;
		name_d = new Symbol(name);
		methodNode_d = mnode;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		try {
			typeCheck(stable);
		}
		catch (TypeCheckError e) {
			/* falls through */
		}

		if (node_d != null && node_d.isFinal()) {
			VariableDeclaratorNode node = 
				(VariableDeclaratorNode) node_d.fields_d.elementAt(index_d);
			if (node.init_d != null) {		// skip inits to null
				return node.init_d.evaluate(stable);
			}
		}

		throw new NotConstant(this);
	}		

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

		if (type_d != null) {
			return type_d;
		}
		
		Type tleft = left_d.typeCheck(stable);

		if (tleft instanceof ArrayType) {
			if (name_d.equals("length") && !leftValue_d) {
				type_d = Type.Int;
				return type_d;
			}
			tleft = Type.createClassType("java.lang.Object");
		}

		if (tleft instanceof ClassType) {
			try {
				ClassType ctype = (ClassType) tleft;
				Symbol fname = ctype.findField(name_d);
				
				if (fname != null) {
					node_d = stable.lookupField(fname);
					int n = node_d.fields_d.size();
					for (int i = 0; i < n; i++) {
						VariableDeclaratorNode var = 
								(VariableDeclaratorNode) node_d.fields_d.elementAt(i);
						if (fname.equals(var.name_d)) {
							index_d = i; type_d = var.type_d;
							break;
						}
					}
					
					/*
					 * Patch the AST if the field was finally found in a supertype.
					 * This is possible if the super class is defined later on.
					 */
					ClassType stype = new ClassType(fname.pathName());
					if (!ctype.identicalTo(stype)) {
						left_d = new SuperExpNode(stype.name());
						left_d.typeCheck(stable);				// sets type_d
					}
					
					if (node_d.isStatic() && left_d.thisOrSuperExp()) {
						SymbolExpNode snode = (SymbolExpNode) left_d;
						left_d = new TypeExpNode(snode.name());
						left_d.typeCheck(stable);				// updates type_d
					}
		
					/*
					 * Now we need to check if we're trying to access an instance field from
					 * a static method without a ref to an object.
					 */
					if (node_d.isInstance()) {
						if (methodNode_d != null && methodNode_d.isInstance()) {
							if (left_d.typeExp()) {
								throw new TypeCheckError(new ErrorMsg(ErrorMsg.NOTSTATC_ERR, 
									name_d.baseName(), this));
							}
						}
						else if (left_d.thisOrSuperExp()) {
							throw new TypeCheckError(new ErrorMsg(ErrorMsg.NOTINSTC_ERR, 
								name_d.baseName(), this));
						}
					}

					return type_d;
				}
				else {
					throw new TypeCheckError(new ErrorMsg(ErrorMsg.UNDEFMTH_ERR, 
						name_d.baseName(), ctype.name().toString(), this));				
				}
			}
			catch (AmbiguousName e) {
				/* falls through */			
			}		
		}

		throw new TypeCheckError(this);
	}

	public BranchHandle translateSynthesized(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		/*
		 * If the field is final and initialized with a literal then fetch
		 * the literal's value from the constant pool.
		 */
		if (node_d != null && node_d.isFinal()) {
			VariableDeclaratorNode node = 
				(VariableDeclaratorNode) node_d.fields_d.elementAt(index_d);

			if (node.init_d instanceof Literal) {
				node.init_d.translate(classGen, methodGen);
				return null;
			}
		}

		left_d.translate(classGen, methodGen);

		Type tleft = left_d.type(); 				
		if (tleft instanceof ArrayType) {
			il.append(new ARRAYLENGTH());				// access to length
		}
		else {
			ClassType ctype = (ClassType) tleft;		// must be a ClassType
			int index = cpg.addFieldref(ctype.name().toInternalString(), name_d.toString(),	
				type_d.toInternalString());

			boolean isStatic = node_d.isStatic();		
			if (isStatic && !left_d.typeExp()) {		// static access using an instance
				il.append(new POP());
			}
			
			if (!leftValue_d) {
				il.append(isStatic ? (Instruction) new GETSTATIC(index) : 
					(Instruction) new GETFIELD(index));
			}
		}

		return null;
	}	

	public void translate(ClassGen classGen, MethodGen methodGen) {
		translateSynthesized(classGen, methodGen);
		if (type_d instanceof BooleanType && !leftValue_d) {
			desynthesize(classGen, methodGen);
		}
	}

}
