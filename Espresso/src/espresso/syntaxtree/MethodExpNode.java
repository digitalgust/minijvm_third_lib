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
import espresso.util.MethodType;
import espresso.util.BooleanType;
import espresso.util.PrimitiveType;
import espresso.util.Symbol;
import espresso.util.ErrorMsg;
import espresso.util.MethodDesc;
import espresso.util.SymbolTable;
import espresso.util.ExceptionStack;
import espresso.util.TypeCheckError;
import espresso.classfile.Constants;

import espresso.classfile.classgen.*;

import java.util.Vector;
import java.math.BigDecimal;

public class MethodExpNode extends ExpressionNode {

	/**
	  * The list of arguments.
	  */
	public Vector args_d;

	/**
	  * This should always be a FieldExpNode.
	  */	
	public ExpressionNode left_d;

	/**
	  * The type name to which the invoked method belongs.
	  */
	public Symbol className_d;

	/**
	  * A ref to the AST node of the method where the call takes place.
	  * Not applicable if the call takes place in <clinit>.
	  */
	public MethodDeclarationNode caller_d = null;

	/**
	  * A ref to the AST node of the called method.
	  */
	public MethodDeclarationNode callee_d = null; 

	/**
	  * Set to true if the callee is a private method or a method
	  * called via super.
	  */
	boolean special_d = false;

	public MethodExpNode(ExpressionNode left, Vector args, 
		MethodDeclarationNode caller) 
	{
		left_d = left;
		caller_d = caller;
		args_d = (args == null) ? new Vector(5) : args;
	}
		
	private Vector typeCheckArgs(SymbolTable stable) throws TypeCheckError {
		Vector largs = new Vector(5);
		if (args_d != null) {
			int n = args_d.size();
			for (int i = 0; i < n; i++) {
				Type ltype = ((ExpressionNode) args_d.elementAt(i)).typeCheck(stable);
				largs.addElement(ltype);
			}
		}
		return largs;
	}

	private void castArguments(Vector argsType) {
		if (args_d == null) {
			return;
		} 	

		Type otype = null;
		Type ntype = null;
		int n = args_d.size();
		Vector nargst = new Vector(5);

		for (int i = 0; i < n; i++) {
			ExpressionNode oexp = (ExpressionNode) args_d.elementAt(i);
			otype = oexp.type_d;
			ntype = (Type) argsType.elementAt(i);			
			if (otype.primitiveType() && !ntype.identicalTo(otype)) {
				oexp = new CastExpNode(oexp, ntype);
			}
			nargst.addElement(oexp);
		}

		args_d = nargst;
	}	

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

		Vector largs = typeCheckArgs(stable);

		// Create method type from the args
		MethodType mtype = new MethodType(Type.Void, largs);
		FieldExpNode fexp = (FieldExpNode) left_d;
		Type ptype = fexp.left_d.typeCheck(stable);

		ClassType ctype = null;
		if (ptype instanceof ClassType) {
			ctype = (ClassType) ptype;
		}
		else if (ptype instanceof ArrayType) {
			// if it is an array, we try to find the method in object
			ctype = Type.createClassType("java.lang.Object");
		}
		else {
			throw new TypeCheckError(this);
		}

		MethodDesc mdesc = new MethodDesc(fexp.name_d, mtype, this);
		mdesc = ctype.findMethod(mdesc);
		if (mdesc == null) {
			throw new TypeCheckError(new ErrorMsg(ErrorMsg.UNDEFMTH_ERR, 
				fexp.name_d.baseName(), ctype.name().toString(), this));				
		}
		else {
			className_d = mdesc.className();
			Vector methods = stable.lookupMethod(new Symbol(className_d, mdesc.methodName()));
			callee_d = (MethodDeclarationNode) methods.elementAt(mdesc.methodIndex());			

			mtype = (MethodType) callee_d.type_d;
			castArguments(mtype.argsType());			

			/*
			 * If the parser has guessed wrong, replace the this or super expression
			 * node in the associated field expression by a type node.
			 */			
			if (callee_d.isStatic() && fexp.left_d.thisOrSuperExp()) {
				SymbolExpNode snode = (SymbolExpNode) fexp.left_d;
				fexp.left_d = new TypeExpNode(snode.name());
				fexp.left_d.typeCheck(stable);			// updates type_d
			}
		}

		/*
		 * Now we need to check if we're trying to call an instance method from
		 * a static one without a ref to an object.
		 */
		if (callee_d.isInstance()) {
			if (caller_d != null && caller_d.isInstance()) {		// caller_d null if <clinit>
				if (fexp.left_d.typeExp()) {
					throw new TypeCheckError(new ErrorMsg(ErrorMsg.NOTSTATC_ERR, 
						callee_d.name_d.baseName(), this));
				}
			}
			else if (fexp.left_d.thisOrSuperExp()) {
				throw new TypeCheckError(new ErrorMsg(ErrorMsg.NOTINSTC_ERR, 
					callee_d.name_d.baseName(), this));
			}
		}

		/*
		 * Set the special flag if calling a private method or via super.
		 */
		special_d = (fexp.left_d.superExp() || callee_d.isPrivate());

		/* Add the exceptions that this method throws to the 
		 * exception stack
		 */
		Vector thrown = callee_d.exceptions_d;
		if (thrown != null) {
		    ExceptionStack es = Espresso.exceptionStack();
		    es.add_throws(callee_d.exceptions_d, this);
		}

		type_d = mtype.resultType();
		return type_d;
	}

	public BranchHandle translateSynthesized(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		FieldExpNode fexp = (FieldExpNode) left_d;
		fexp.left_d.translate(classGen, methodGen);

		BranchHandle last = null;
		int n = (args_d != null) ? args_d.size() : 0;

		for (int i = 0; i < n; i++) {
			BranchHandle next = null;
			InstructionHandle start = il.getEnd();
			ExpressionNode expression = (ExpressionNode) args_d.elementAt(i);

			BigDecimal value = expression.evaluateExp();
			if (value != null) {
				il.append(expression.type().PUSH(cpg, value));
			}
			else {
				next = expression.translateSynthesized(classGen, methodGen);
			}
			
			if (last != null) {
				last.setTargetNoCopy(start.getNext());
			}
			last = next;
		}

		InstructionHandle pop = null;
		boolean isStatic = callee_d.isStatic();	

		if (isStatic && !fexp.left_d.typeExp()) {		// static access using an instance
			pop = il.append(new POP());		
		}
		
		int index = cpg.addMethodref(className_d.toInternalString(), 
			fexp.name_d.toString(),	callee_d.type().toInternalString());

		il.append(isStatic ? (Instruction) new INVOKESTATIC(index) : 
			special_d ? (Instruction) new INVOKESPECIAL(index) :
			(Instruction) new INVOKEVIRTUAL(index));

		if (last != null) {
			last.setTargetNoCopy(pop == null ? il.getEnd() : pop);
		}

		return null;
	}	
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		translateSynthesized(classGen, methodGen);
		if (type_d instanceof BooleanType) {
			desynthesize(classGen, methodGen);
		}
	}

}
