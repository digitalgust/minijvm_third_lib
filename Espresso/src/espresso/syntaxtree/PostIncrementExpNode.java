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
import espresso.util.Symbol;
import espresso.util.ClassType;
import espresso.util.ErrorMsg;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class PostIncrementExpNode extends ExpressionNode {

	public ExpressionNode left_d;

	public PostIncrementExpNode(ExpressionNode left) {
		left_d = left;
		left_d.leftValue_d = true;
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		if (!left_d.leftValue()) {
			throw new TypeCheckError(new ErrorMsg(ErrorMsg.NOLVALUE_ERR));
		}

		Type tleft = left_d.typeCheck(stable);

		if (tleft.numericType()) {
			type_d = tleft;
			return type_d;
		}
		
		throw new TypeCheckError(this);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		BigDecimal one = new BigDecimal(1);
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		if (left_d.variableExp()) {					// local var or formal param
			VariableExpNode vnode = (VariableExpNode) left_d;
			LocalVariableGen lv = methodGen.lookupLocal(vnode.name_d.baseName(2));

			int index = lv.getSlot();
			if (type_d.integralType() && type_d.oneWord()) {
				if (!statementExp_d) {
					il.append(type_d.LOAD(index));
				}
				il.append(new IINC(index, 1));
			}
			else {
				il.append(type_d.LOAD(index));
				if (!statementExp_d) {
					il.append(type_d.DUP());
				}
				il.append(type_d.PUSH(cpg, one));
				il.append(type_d.ADD());		
				il.append(type_d.STORE(index));			
			}				
		}
		else if (left_d instanceof FieldExpNode) {
			left_d.translate(classGen, methodGen);
			left_d.leftValue_d = false;				// right value translation
			left_d.translate(classGen, methodGen);

			FieldExpNode fnode = (FieldExpNode) left_d;
			ClassType ctype = (ClassType) fnode.left_d.type();
			int index = cpg.addFieldref(ctype.name().toInternalString(), 
				fnode.name_d.toString(), type_d.toInternalString());

			if (!statementExp_d) {
				il.append(fnode.node_d.isStatic() ? (Instruction) type_d.DUP() :
					(Instruction) type_d.DUPX1());
			}

			il.append(type_d.PUSH(cpg, one));
			il.append(type_d.ADD());
			il.append(fnode.node_d.isStatic() ? (Instruction) new PUTSTATIC(index) : 
				(Instruction) new PUTFIELD(index));			
		}
		else if (left_d instanceof ArrayExpNode) {
			left_d.translate(classGen, methodGen);
			left_d.leftValue_d = false;				// right value translation
			left_d.translate(classGen, methodGen);
		
			if (!statementExp_d) {
				il.append(type_d.DUPX2());
			}

			il.append(type_d.PUSH(cpg, one));
			il.append(type_d.ADD());
			il.append(type_d.ASTORE());
		}
		else {
			Espresso.internalError();
		}
	}		
			
}
