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

import espresso.util.Type;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.util.NotConstant;
import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class BooleanLiteral extends Literal {

	public Boolean value_d;
	
	public BooleanLiteral(Boolean value) {
		value_d = value;
	}
	
	public BooleanLiteral(BigDecimal value) {
		BigDecimal trueValue = new BigDecimal("1");
		value_d = new Boolean(value.equals(trueValue));
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		type_d = Type.Boolean;
		return type_d;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		return new BigDecimal(value_d.booleanValue() ? "1" : "0");
	}		

	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		if (!value_d.booleanValue()) {
			addFalseList(il.append(new GOTO(null)));
		}
	}
	
	public BranchHandle translateSynthesized(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		il.append(new ICONST(value_d.booleanValue() ? 1 : 0));
		return null;
	}

	public int addToConstantPool(ClassGen classGen) {
		ConstantPoolGen cpGen = classGen.getConstantPool();
		return cpGen.addInteger(value_d.booleanValue() ? 1 : 0);
	}

}
