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
import espresso.util.NotConstant;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.math.BigDecimal;

public class DoubleLiteral extends Literal {

	Double value_d;
	
	public DoubleLiteral(Double value) {
		value_d = value;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		return new BigDecimal(value_d.doubleValue());
	}		

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		type_d = Type.Double;
		return type_d;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();
		il.append(new PUSH(cpg, value_d.doubleValue()));
	}
	
	public int addToConstantPool(ClassGen classGen) {
		ConstantPoolGen cpGen = classGen.getConstantPool();
		return cpGen.addDouble(value_d.doubleValue());
	}

}
