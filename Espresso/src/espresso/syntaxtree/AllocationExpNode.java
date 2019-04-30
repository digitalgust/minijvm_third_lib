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

import espresso.util.Type;
import espresso.util.IntType;
import espresso.util.NullType;
import espresso.util.PrimitiveType;
import espresso.util.ClassType;
import espresso.util.ArrayType;
import espresso.util.MethodType;
import espresso.util.MethodDesc;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.util.Vector;
import java.math.BigDecimal;

public class AllocationExpNode extends ExpressionNode {

	/**
	  * Type of elements allocated. The final type is determined by 
	  * typeCheck() and set in the corresponding base class field.
	  */
	public Type etype_d;
	
	/**
	  * An ExpressionNode vector for each dimension or for each 
	  * actual parameter of the class constructor. Empty expressions 
	  * in array allocations like a[4][][] are filled with refs to 
	  * the NullLiteral.
	  */
	public Vector args_d;

	/**
	  * The number of non-empty dimensions being specified. This is
	  * calculated by the typeCheck() method.
	  */
	public int dims_d = 0;
	  
    /**
	  * This flag determines if the parameters in the args_d vector 
	  * correspond to an array allocation or a constructor call.
	  */
	public boolean array_d;

    /**
	  * A pointer to the constructor's declaration node.
	  */ 
	public MethodDeclarationNode mnode_d = null;

	/**
	  * Here we keep the type name to which the invoked constructor belongs.
	  */
	public Symbol className_d;
	
	public AllocationExpNode(Type type, Vector args, boolean array) {
		etype_d = type;
		args_d = args;
		array_d = array;
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
			if (otype instanceof PrimitiveType && !ntype.identicalTo(otype)) {
				oexp = new CastExpNode(oexp, ntype);
			}
			nargst.addElement(oexp);
		}
		args_d = nargst;
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
		Vector largs = typeCheckArgs(stable);

		if (array_d) {
			Type ltype = Type.Null;
			boolean seenNull = false;

			// The dimension expressions should be integer types.
			
			int n = largs.size();
			for (int i = 0; i < n; i++) {	
				Type atype = (Type) largs.elementAt(i);
				
				/*
				 * if we encounter a null entry, that means no dimension was given for that
				 * index. Parser takes care of the case, that first index is always defined.
				 */
				if (atype instanceof NullType) {
					seenNull = true;
					continue;
				}
				
				dims_d++;		// count the number of non-null dims
				
				/*
				 * If index > 0, check whether have a non-null type following some null
				 * type, in which case, we also have an error. 
				 */
				if (i > 0 && seenNull) {
					throw new TypeCheckError(this);
				}
				
				/*
				 * Unary promotions is applicable. Index expressions can't be of type long.
				 */
				if (atype.integralType() && atype.oneWord()) {
					if (!(atype instanceof IntType)) {
						ExpressionNode node = (ExpressionNode) args_d.elementAt(i);
						args_d.setElementAt(new CastExpNode(node, Type.Int), i);
					}
				} 
				else {
					throw new TypeCheckError(this);
				} 
			}

			int realDims = args_d.size();			
			type_d = new ArrayType(etype_d, realDims);
			className_d = (realDims == 1 && etype_d instanceof ClassType) ? 
					((ClassType) etype_d).name() : new Symbol(type_d.toString());
		}
		else {				
			MethodType mtype = new MethodType(Type.Void, largs);
			ClassType ctype = (ClassType) etype_d;
			MethodDesc mdesc = new MethodDesc(new Symbol("<init>"), mtype, this);
			mdesc = ctype.findMethod(mdesc);

			if (mdesc == null)  {
				throw new TypeCheckError(this);
			}
			else {
				className_d = mdesc.className();
				Vector methods = stable.lookupMethod(new Symbol(className_d, mdesc.methodName()));
				mnode_d = (MethodDeclarationNode) methods.elementAt(mdesc.methodIndex());
				MethodType dtype = (MethodType) mnode_d.type_d;
				type_d = etype_d;
				castArguments(dtype.argsType());			
				typeCheckArgs(stable);
			}
		}
		
		return type_d;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		if (array_d) {			// array allocation
			int realDims = args_d.size();
			
			for (int i = 0; i < dims_d; i++) {
				ExpressionNode node = (ExpressionNode) args_d.elementAt(i);
				node.translate(classGen, methodGen);
			}

			int index = 0;
			if (etype_d.referenceType() || realDims > 1) {
				index = cpg.addClass(className_d.toInternalString());
			}
				
			if (realDims == 1) {
				il.append(etype_d.NEWARRAY(index));
			} else {
				il.append(new MULTIANEWARRAY(index, (short) dims_d));
			}
		}
		else {
			// Add a class entry to the CP and create the instance
			int index = cpg.addClass(className_d.toInternalString());
			il.append(new NEW(index));
			
			if (!statementExp_d) {
				il.append(new DUP());
			}

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

			index = cpg.addMethodref(className_d.toInternalString(), "<init>",
				mnode_d.type_d.toInternalString());
			il.append(new INVOKESPECIAL(index));

			if (last != null) {
				last.setTargetNoCopy(il.getEnd());
			}
		}
	}
	
}
