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
import espresso.util.IntType;
import espresso.util.ArrayType;
import espresso.util.BooleanType;
import espresso.util.Symbol;
import espresso.util.ErrorMsg;
import espresso.util.ClassType;
import espresso.util.MethodType;
import espresso.util.Operators;
import espresso.util.SymbolTable;
import espresso.util.IntegralType;
import espresso.util.PrimitiveType;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.math.BigDecimal;
import java.util.Enumeration;

public class AssignmentNode extends ExpressionNode {

	public int operator_d = Operators.UNDEFINED;

	public ExpressionNode left_d;
	public ExpressionNode right_d;
		
	/**
	  * Instruction to be back patched. Used for code generation.
	  */
	private BranchHandle gotor_d = null;

	/**
	  * Ref to the left value of the assignemnt. Needed when a cast 
	  * expression is inserted by the type checker.
	  */
	private ExpressionNode realLeft_d = null;

	/**
	  * This pointer is only set if my parent node is also an assignment.
	  * Needed to insert dups in the instruction list.
	  */
    public AssignmentNode parent_d = null;

	public AssignmentNode() {
		super();
	}
		
	public AssignmentNode(ExpressionNode left, ExpressionNode right) {
		super();
		left_d = left;
		right_d = right;
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

		if (type_d != null) {
			return type_d;
		}
	
		if (!left_d.leftValue()) {
			type_d = Type.Void;			// avoid type checking again
			throw new TypeCheckError(new ErrorMsg(ErrorMsg.NOLVALUE_ERR));
		}
		
		Type tleft = left_d.typeCheck(stable);
		Type tright = right_d.typeCheck(stable);

		if(tright == null)
		    {
			System.out.println("jef - help!");
			System.out.println("jef - " + right_d);
		    }

		switch (operator_d) {
		case Operators.EQ:
			int distance = tright.distanceTo(tleft);

			/*
			 * Add an implicit coercion when assigning constant expressions to 
			 * integral types narrower than int. For example 's = 0' should 
			 * type check if s is short, byte or char.
			 */	
			if (distance < 0) {
				if (tleft.integralType() && tright instanceof IntType) { 
					BigDecimal value = right_d.evaluateExp();
					if (value != null) {
						IntegralType itype = (IntegralType) tleft;
						if (value.scale() > 0 || !itype.inRange(value.longValue())) {
							type_d = Type.Void;			// avoid type checking again
							throw new TypeCheckError(this);
						}
						else {
							right_d = new CastExpNode(right_d, tleft);
							tright = right_d.typeCheck(stable);
						}
					}
				} 
				else {
					type_d = Type.Void;
					throw new TypeCheckError(this);
				}
			}
			else {		// distance >= 0
				/*
				 * If tleft is primitive we need a widening cast.
				 */
				if (tleft.primitiveType() && distance > 0) {
					right_d = new CastExpNode(right_d, tleft);
					tright = right_d.typeCheck(stable);
				}
			}
				
			/*
			 * If part of a chain of assignments, make sure to add a cast
			 * if my parent's left value is wider than myself.
			 */
			if (parent_d != null) {
				Type ptype = parent_d.left_d.type();
				if (tleft.distanceTo(ptype) > 0) {
					parent_d.right_d = new CastExpNode(this, ptype);
				}
			}		
			
			type_d = tleft;
			return type_d;
		
		case Operators.PS_EQ: 
		case Operators.MS_EQ:
		case Operators.TS_EQ: 
		case Operators.DV_EQ:
		case Operators.MD_EQ: 
		case Operators.OR_EQ:
		case Operators.AND_EQ: 
		case Operators.XOR_EQ:
		case Operators.SL_EQ: 
		case Operators.SR_EQ:
		case Operators.SS_EQ:
			tright = right_d.typeCheck(stable);
			
			MethodType ptype = lookupPrimop(stable, operator_d, new MethodType(Type.Void, 
				tleft, tright));		// defined in ExpressionNode
		
			if (ptype != null) {
				/*
				 * Add the appropriate casts to the AST. The resulting AST might look 
				 * like "(double) s += 1.2" if s is of type int for example. Notice that
				 * that the final type of the operation needs to be casted back to the 
				 * type of the lhs.
				 */				 	
				Type arg1 = (Type) ptype.argsType().elementAt(0);
				if (!arg1.identicalTo(tleft)) {
					left_d = new CastExpNode(left_d, arg1);
				}
		
				Type arg2 = (Type) ptype.argsType().elementAt(1);
				if (!arg2.identicalTo(tright)) {
					right_d = new CastExpNode(right_d, arg1);				
				}
				type_d = ptype.resultType();
				return tleft;		
			}			
		break;
		default:
			Espresso.internalError();
		break;		
		}
		
		type_d = Type.Void;			// avoid type checking again
		throw new TypeCheckError(this);
	}

	private void translateOp(ClassGen classGen, MethodGen methodGen) {
		Type trealLeft = realLeft_d.type();
		InstructionList il = methodGen.getInstructionList();

		/* 
		 * Compile a right value of left_d (including casts)
		 */
		BranchHandle gotol = null;
		if (operator_d != Operators.EQ) {
			realLeft_d.leftValue_d = false;			// right value translation
			gotol = left_d.translateSynthesized(classGen, methodGen);
		}

		/*
		 * Translate the right operand synthesizing it if needed. 
		 */
		InstructionHandle patch = il.getEnd();
		gotor_d = right_d.translateSynthesized(classGen, methodGen);

		if (gotol != null) {
			gotol.setTargetNoCopy(patch.getNext());
		}

		Instruction op = null;
		Type tleft = left_d.type();

		switch (operator_d) {
		case Operators.PS_EQ: 
			op = tleft.ADD();
		break;
		case Operators.MS_EQ:
			op = tleft.SUB();
		break;
		case Operators.TS_EQ: 
			op = tleft.MUL();
		break;
		case Operators.DV_EQ:
			op = tleft.DIV();
		break;
		case Operators.MD_EQ: 
			op = tleft.REM();
		break;
		case Operators.OR_EQ:
			op = tleft.OR();
		break;
		case Operators.AND_EQ: 
			op = tleft.AND();
		break;
		case Operators.XOR_EQ:
			op = tleft.XOR();
		break;
		case Operators.SL_EQ:
			op = tleft.SHL();
		break;
		case Operators.SR_EQ:
			op = tleft.SHR();
		break;
		case Operators.SS_EQ:
			op = tleft.USHR();
		break;
		default:
			/* Falls through - EQ case */
		}

		if (op != null) {
			patch = il.append(op);
			if (gotor_d != null) {
				gotor_d.setTargetNoCopy(patch);
				gotor_d = null;
			}
		}

		/*
		 * The result may need to be casted back to the type of realLeft_d.
		 * This should never be true when the operator is EQ.
		 */
		if (left_d != realLeft_d) {
			il.append(tleft.CAST(trealLeft));
		}
	}

	public BranchHandle translateSynthesized(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		/*
		 * A cast node may have been added by the type checker. Get a ref
		 * to the real left value in that case.
		 */
		realLeft_d = left_d;			
		if (realLeft_d instanceof CastExpNode) {
			realLeft_d = ((CastExpNode) realLeft_d).left_d;
		}

		/*
		 * If the left is an array or a field then its reference should be 
		 * pushed first.
		 */
		if (realLeft_d instanceof ArrayExpNode || realLeft_d instanceof FieldExpNode) {
			realLeft_d.translate(classGen, methodGen);
		}
		
		InstructionHandle falsec = null;
		Type trealLeft = realLeft_d.type();

		if (realLeft_d.variableExp()) {						// locals or params
			VariableExpNode vnode = (VariableExpNode) realLeft_d;
			LocalVariableGen lv = methodGen.lookupLocal(vnode.name_d.baseName(2));

			translateOp(classGen, methodGen);

			if (!statementExp_d) {
				falsec = il.append(trealLeft.DUP());
			}
			il.append(trealLeft.STORE(lv.getSlot()));
		}
		else if (realLeft_d instanceof FieldExpNode) {	
			FieldExpNode fnode = (FieldExpNode) realLeft_d;
			ClassType ctype = (ClassType) fnode.left_d.type();
			int index = cpg.addFieldref(ctype.name().toInternalString(), 
				fnode.name_d.toString(), fnode.type_d.toInternalString());

			translateOp(classGen, methodGen);

			if (!statementExp_d) {
				falsec = il.append(fnode.node_d.isStatic() ? 
					(Instruction) trealLeft.DUP() :
					(Instruction) trealLeft.DUPX1());
			}
			il.append(fnode.node_d.isStatic() ? (Instruction) new PUTSTATIC(index) : 
				(Instruction) new PUTFIELD(index));			
		}
		else if (realLeft_d instanceof ArrayExpNode) {	
			translateOp(classGen, methodGen);

			if (!statementExp_d) {
				falsec = il.append(trealLeft.DUPX2());
			}
			il.append(trealLeft.ASTORE());
		}
		else {
			Espresso.internalError();
		}

		/*
		 * If gotor_d was not backpatched yet, do it before returning.
	  	 */
		if (gotor_d != null) {
			gotor_d.setTargetNoCopy(falsec == null ? il.getEnd() : falsec);
		}

		return null;
	}	
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		translateSynthesized(classGen, methodGen);
		if (type_d instanceof BooleanType && !statementExp_d) {
			desynthesize(classGen, methodGen);
		}
	}

}
