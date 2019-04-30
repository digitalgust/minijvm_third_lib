/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 1/28/98
 *
 */

package espresso.syntaxtree;

import espresso.util.Symbol;
import espresso.util.Type;
import espresso.util.PrimitiveType;
import espresso.util.ClassType;
import espresso.util.MethodType;
import espresso.util.MethodDesc;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.util.Vector;
import java.math.BigDecimal;

public class ConstructorInvocationNode extends ExpressionNode {

	/**
	  * Vector containing invocation arguments.
	  */
	public Vector args_d = null;

	/**
	  * True if "this" was used, false if "super" was used.
	  */
	public boolean invocationThis_d;
 
    /**
	  * Name of this class or super class.
	  */
	public Symbol name_d;

	/**
	  * A ref to the AST node.
	  */
	public MethodDeclarationNode node_d = null; 

	/**
	  * Here we keep the type name to which the invoked constructor belongs.
	  */
	public Symbol className_d;
			
	public ConstructorInvocationNode() {
		invocationThis_d = false;
	}

	public ConstructorInvocationNode(Symbol name) {
		name_d = name;
		invocationThis_d = false;
	}

	private void castArguments(Vector argsType) {
		if (args_d != null) {
			Vector nargst = new Vector(5);
			Type otype = null, ntype = null;

			int n = args_d.size();
			for (int i = 0; i < n; i++) {
				ExpressionNode oexp = (ExpressionNode) args_d.elementAt(i);
				otype = oexp.type_d;
				ntype = (Type) argsType.elementAt(i);			
				if ((otype instanceof PrimitiveType) && (!ntype.identicalTo(otype))) {
					oexp = new CastExpNode(oexp, ntype);
				}
				nargst.addElement(oexp);
			}
			args_d = nargst;
		}
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
	
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

		// Type check the args first
		Vector largs = typeCheckArgs(stable);

		// Create method type from args										
		ClassType ctype = new ClassType(name_d);
		MethodType mtype = new MethodType(Type.Void, largs);
		MethodDesc mdesc = new MethodDesc(new Symbol("<init>"), mtype, this);

		mdesc = ctype.findMethod(mdesc);
		if (mdesc == null) {
			throw new TypeCheckError(this);
		}
		else {
			int consIndex = mdesc.methodIndex();
			className_d = mdesc.className();
			Vector methods = stable.lookupMethod(new Symbol(className_d, 
				mdesc.methodName()));
			node_d = (MethodDeclarationNode) methods.elementAt(consIndex);
			type_d = node_d.type_d;
			castArguments(((MethodType)type_d).argsType());			
			typeCheckArgs(stable);
		}

		return Type.Void;
	}
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		il.append(new ALOAD(0));			// push this

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

		int index = cpg.addMethodref(className_d.toInternalString(), "<init>", 
			type_d.toInternalString());
		il.append(new INVOKESPECIAL(index));

		if (last != null) {
			last.setTargetNoCopy(il.getEnd());
		}
	}

}
