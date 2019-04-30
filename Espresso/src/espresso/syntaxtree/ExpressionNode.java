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
import espresso.util.MethodType;
import espresso.util.BooleanType;
import espresso.util.Operators;
import espresso.util.SymbolTable;
import espresso.util.NotConstant;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.util.Vector;
import java.math.BigDecimal;
import java.util.Enumeration;

public abstract class ExpressionNode extends SyntaxTreeNode {

	public Type type_d = null;
	
	public InstructionList trueList_d;
	public InstructionList falseList_d;

	/*
	 * Set to true by the parser if this expression is to the left
	 * of an assignment operator.
	 */
	public boolean leftValue_d = false;

	/*
	 * This flag is set to true by the parser if the parent of this
	 * expression is a statement expression.
	 */
	public boolean statementExp_d = false;
	
	public abstract Type typeCheck(SymbolTable stable)
		throws TypeCheckError;

	public Type type() {
		return type_d;
	}
		
	public BigDecimal evaluateExp() {
		SymbolTable stable = Espresso.symbolTable();
		try {
			return evaluate(stable);	
		}
		catch (NotConstant e) {
			return null;
		}
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		throw new NotConstant(this);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		Espresso.notYetImplemented();
	}	

	/**
	  * First translate the expression and then synthesize it. This method
	  * is redefined in those expressions that already synthesize their
	  * result to avoid synthesize, de-synthesize, synthesize sequences.
	  */
	public BranchHandle translateSynthesized(ClassGen classGen, MethodGen methodGen) {
		translate(classGen, methodGen);
		return synthesize(classGen, methodGen);
	}

	/** 
	  * Synthesize a boolean expression, i.e., either push a 0 or 1 onto the 
	  * operand stack for the next statement to succeed. Returns the handle
	  * of the instruction to be backpatched.
	  */
	public BranchHandle synthesize(ClassGen classGen, MethodGen methodGen) {
		BranchHandle gototc = null;

		if (type_d instanceof BooleanType) {
			ConstantPoolGen cpg = classGen.getConstantPool();
			InstructionList il = methodGen.getInstructionList();

			BigDecimal value = evaluateExp();
			if (value != null) {
				il.append(new PUSH(cpg, value.intValue()));
				backPatch(trueList_d, il.getEnd());
				backPatch(falseList_d, il.getEnd());
			}
			else if (!leftValue()) {
				backPatch(trueList_d, il.append(new ICONST(1)));

				if (falseList_d != null) {
					gototc = il.append(new GOTO(null));
					backPatch(falseList_d, il.append(new ICONST(0)));
				}
			}
		}

		return gototc;
	}

	public void desynthesize(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		addFalseList(il.append(new IFEQ(null)));
	}

	public MethodType lookupPrimop(SymbolTable stable, int op, MethodType ctype) {
		MethodType result = null;
		
		Vector primop = stable.lookupPrimop(Operators.toSymbol(op));		
		if (primop != null) {
			int minIndex = Integer.MAX_VALUE;
			int minDistance = Integer.MAX_VALUE;
			
			/*
			 * Find the type for this primop always keeping the lowest index.
			 * This index refers to the primop version with the widest type.
			 */
			int n = primop.size();
			for (int i = 0; i < n; i++) {
				MethodType ptype = (MethodType) primop.elementAt(i);
				int distance = ctype.distanceTo(ptype);
				if (distance < minDistance && distance >= 0) {
					minDistance = distance;
					minIndex = i;
					result = ptype;
				}
			}		
		}	
		return result;
	}	

	/**
	  * Determines if the expression is an access to a local or a formal param.
	  * Redefined in the VariableExpNode class.
	  */
	public boolean variableExp() {
		return false;
	}

	/**
	  * Determines if the expression is an lvalue. TODO -> check if it is final.
	  */
	public boolean leftValue() {
		return (this instanceof FieldExpNode || this instanceof LocalExpNode
			|| this instanceof FormalExpNode || this instanceof ArrayExpNode);
	}

	/**
	  * Determines if the expression refers to a type.
	  */
	public boolean typeExp() {
		return (this instanceof TypeExpNode);
	}

	/**
	  * Determines if the expression is 'this'.
	  */
	public boolean thisExp() {
		return (this instanceof ThisExpNode);
	}

	/**
	  * Determines if the expression is 'super'.
	  */
	public boolean superExp() {
		return (this instanceof SuperExpNode);
	}

	/**
	  * Determines if the expression refers to this or super.
	  */
	public boolean thisOrSuperExp() {
		return (this instanceof ThisExpNode || this instanceof SuperExpNode);
	}
			
	/**
	  * Backpatch a true or a false list.
	  */
	public static void backPatch(InstructionList list, InstructionHandle target) {
		if (list != null) {
			for (Enumeration e = list.elements(); e.hasMoreElements(); ) {
				BranchHandle bh = (BranchHandle) e.nextElement();
				bh.setTargetNoCopy(target);
			}
			list.dispose();			// avoid backpatching twice
		}
	}

	/**
	  * Add an instruction to the true list for backpatching.
	  */
	public InstructionList addTrueList(InstructionHandle ih) {
		if (trueList_d == null) {
			trueList_d = new InstructionList();
		}
		BranchInstruction in = (BranchInstruction) ih.getInstruction();				
		trueList_d.append(in);
		return trueList_d;
	}
		
	/**
	  * Add an instruction to the false list for backpatching.
	  */
	public InstructionList addFalseList(InstructionHandle ih) {
		if (falseList_d == null) {
			falseList_d = new InstructionList();
		}
		BranchInstruction in = (BranchInstruction) ih.getInstruction();		
		falseList_d.append(in);
		return falseList_d;
	}

	/**
	  * Append a list to the true list.
	  */
	public InstructionList appendToTrueList(InstructionList right) {
		if (trueList_d == null) {
			trueList_d = right;
		}
		else if (right != null) {
			trueList_d.append(right);
		}
		return trueList_d;
	}

	/**
	  * Append a list to the false list.
	  */
	public InstructionList appendToFalseList(InstructionList right) {
		if (falseList_d == null) {
			falseList_d = right;
		}
		else if (right != null) {
			falseList_d.append(right);
		}
		return falseList_d;
	}

}
