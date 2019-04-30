/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 2/02/98
 *
 */

package espresso.syntaxtree;

import espresso.Espresso;
import espresso.syntaxtree.SwitchLabelNode;
import espresso.util.NotConstant;
import espresso.util.Type;
import espresso.util.VoidType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.util.Vector;
import java.math.BigDecimal;

public class SwitchStatementNode extends StatementNode {

    public Vector labels_d;					// array of SwitchLabelNode
    public ExpressionNode expression_d;

	public SwitchStatementNode() {
		super();
		labels_d = new Vector(5);
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type texp = expression_d.typeCheck(stable);
		
		if (texp.integralType()) {
			int n = labels_d.size();
			for (int i = 0; i < n; i++) {
				SwitchLabelNode label = (SwitchLabelNode) labels_d.elementAt(i);
				Type tlabel = label.typeCheck(stable);
				
				if (!(tlabel instanceof VoidType) && tlabel.distanceTo(texp) < 0) {		// can assign ?
					label.expression_d = new CastExpNode(label.expression_d, texp);
				}
			}		
			return Type.Void;
		}
		
		throw new TypeCheckError(this);
	}

	private SwitchLabelNode getDefault() {
		SwitchLabelNode label;
		int n = labels_d.size();
		for (int i =0; i < n; i++) {
			label = (SwitchLabelNode) labels_d.elementAt(i);
			if (label.expression_d == null) {
				labels_d.removeElement(label);
				return label;
			}
		}
		return null;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpGen = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();
		SwitchLabelNode dlabel = getDefault();
		int n = labels_d.size();
		expression_d.translate(classGen, methodGen);
		InstructionHandle endexp = il.getEnd();
		int [] cases = new int[n];
		InstructionHandle [] targets = new InstructionHandle[n];
		InstructionHandle target = null;
		SwitchLabelNode label;
		for (int i =0; i < n; i++) {
			label = (SwitchLabelNode) labels_d.elementAt(i);
			if (label.expression_d != null) {
				BigDecimal bd = label.expression_d.evaluateExp();
				if (bd == null) {					
					Espresso.internalError();
				} 
				cases[i] = bd.intValue();
				if (label.statements_d == null ||  label.statements_d.size() == 0) {
					continue;
				}
				targets[i] = il.getEnd();
				int m = label.statements_d.size();
				for (int j =0; j < m ; j++) {
					((StatementNode)label.statements_d.elementAt(j)).translate(classGen, methodGen);
				}
				targets[i] = targets[i].getNext();				
			}
		}
		if (dlabel == null || dlabel.statements_d == null || dlabel.statements_d.size() == 0) {
			target = il.append(new NOP());
		}
		else {
			target = il.getEnd();
			int m = dlabel.statements_d.size();
			for (int j =0; j < m ; j++) {
				((StatementNode)dlabel.statements_d.elementAt(j)).translate(classGen, methodGen);
			}
			target = target.getNext();				
		}
		
		InstructionHandle patch = target;
		// Check for empty statements;
		for (int l = targets.length-1; l >= 0; l--) {
			if (targets[l] == null) {
				targets[l] = patch;
			}
			else {
				patch = targets[l];
			}
		}
		il.insert(endexp.getNext().getInstruction(),new SWITCH(cases, targets, target));
	}
}
