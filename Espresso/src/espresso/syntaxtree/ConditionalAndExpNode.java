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

import java.util.Enumeration;


public class ConditionalAndExpNode extends ExpressionNode {

	public ExpressionNode left_d;
	public ExpressionNode right_d;

	public ConditionalAndExpNode(ExpressionNode left, ExpressionNode right) {
		left_d = left;
		right_d = right;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		BigDecimal result = left_d.evaluate(stable);
		return (result.intValue() == 0) ? result : right_d.evaluate(stable);
	}		

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type ltype = left_d.typeCheck(stable);
		Type rtype = right_d.typeCheck(stable);

		if (ltype.identicalTo(Type.Boolean) && ltype.identicalTo(rtype)) {
			type_d = ltype;
			return type_d;
		}

		throw new TypeCheckError(this);
	}

	/**
	  * Translation:	b1 and b2
	  *
	  *			   b1
	  *			   b2	
	  */
	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		left_d.translate(classGen, methodGen);
		right_d.translate(classGen, methodGen);

		appendToTrueList(left_d.appendToTrueList(right_d.trueList_d));
		appendToFalseList(left_d.appendToFalseList(right_d.falseList_d));
	}
	
}
