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

public class CharacterLiteral extends Literal {

	public Character value_d;
	
	public CharacterLiteral(Character value) {
		value_d = value;
	}
	
	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		return new BigDecimal(value_d.charValue());
	}		

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
	    type_d = Type.Char;
		return type_d;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();
		il.append(new PUSH(cpg, value_d.charValue()));
	}
	
	public int addToConstantPool(ClassGen classGen) {
		ConstantPoolGen cpGen = classGen.getConstantPool();
		return cpGen.addInteger(value_d.charValue());
	}

}
