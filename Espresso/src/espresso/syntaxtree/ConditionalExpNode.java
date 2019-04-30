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
import espresso.util.BooleanType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.classfile.classgen.*;

import java.util.Enumeration;


public class ConditionalExpNode extends ExpressionNode {

	public ExpressionNode test_d;
	public ExpressionNode trueCase_d;
	public ExpressionNode falseCase_d;

	public ConditionalExpNode(ExpressionNode test, ExpressionNode trueCase, 
		ExpressionNode falseCase) {
		super();
		
		test_d = test;
		trueCase_d = trueCase;
		falseCase_d = falseCase;
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type ttest = test_d.typeCheck(stable);

		if (ttest instanceof BooleanType) {
			Type ttrue = trueCase_d.typeCheck(stable);
			Type tfalse = falseCase_d.typeCheck(stable);
	
			// TODO -> integral types (and literals) need to be treated specially
					
			if (ttrue.distanceTo(tfalse) >= 0) {
				type_d = tfalse;
				return type_d;
			}
			else if (tfalse.distanceTo(ttrue) >= 0) {
				type_d = ttrue;
				return type_d;
			}				
		}
	
		throw new TypeCheckError(this);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		test_d.translate(classGen, methodGen);
				
		InstructionHandle truec = il.getEnd();
		trueCase_d.translate(classGen, methodGen);	

		BranchHandle gotot = il.append(new GOTO(null));
		falseCase_d.translate(classGen, methodGen);	
		gotot.setTarget(il.append(new NOP()));

		backPatch(test_d.trueList_d, truec.getNext());
		backPatch(test_d.falseList_d, gotot.getNext());
	}
	
}
