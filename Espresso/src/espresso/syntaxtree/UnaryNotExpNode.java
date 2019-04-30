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

public class UnaryNotExpNode extends ExpressionNode {

	public ExpressionNode left_d;

	public UnaryNotExpNode(ExpressionNode left) {
		left_d = left;
	}

	public BigDecimal evaluate(SymbolTable stable) throws NotConstant {
		BigDecimal result = left_d.evaluate(stable);
		return new BigDecimal(result.intValue() == 1 ? "0" : "1");
	}		

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type ntype = left_d.typeCheck(stable);
		if (ntype.identicalTo(Type.Boolean)) {
			type_d = ntype;
		}
		else {
			throw new TypeCheckError(this);
		}
		return type_d;
	}

	/**
	  * Translation:	not b
	  *
	  *			   b	[truelist := <falselist>, falselist := <truelist>]
	  *			   goto <falselist> 	
	  */
	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		left_d.translate(classGen, methodGen);
		trueList_d = left_d.falseList_d;
		BranchHandle gotof = il.append(new GOTO(null));
		falseList_d = left_d.addTrueList(gotof);
	}
	
}
