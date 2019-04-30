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

public class ConditionalOrExpNode extends ExpressionNode {

	public ExpressionNode left_d;
	public ExpressionNode right_d;

	public ConditionalOrExpNode(ExpressionNode left, ExpressionNode right) {
		left_d = left;
		right_d = right;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		BigDecimal result = left_d.evaluate(stable);
		return (result.intValue() == 1) ? result : right_d.evaluate(stable);
	}		

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type ltype = left_d.typeCheck(stable);
		Type rtype = right_d.typeCheck(stable);
		if (ltype.identicalTo(Type.Boolean) && ltype.identicalTo(rtype)) {
			type_d = ltype;
		}
		else {
			throw new TypeCheckError(this);
		}
		return type_d;
	}

	/**
	  * Translation:	b1 or b2
	  *
	  *			   b1	[truelist := 1, falselist := 2]
	  *			1: goto <truelist> 	
	  *			2: b2	
	  */
	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		left_d.translate(classGen, methodGen);
		InstructionHandle gotot = il.append(new GOTO(null));
		InstructionHandle rstart = il.getEnd();
		right_d.translate(classGen, methodGen);

		backPatch(left_d.trueList_d, gotot);
		backPatch(left_d.falseList_d, rstart.getNext());

		addTrueList(gotot);
		appendToTrueList(right_d.trueList_d);
		falseList_d = right_d.falseList_d;
	}
}

