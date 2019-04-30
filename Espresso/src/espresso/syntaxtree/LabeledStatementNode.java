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
import espresso.util.Type;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.classfile.classgen.*;

public class LabeledStatementNode extends StatementNode {

    public Symbol label_d;
	public StatementNode statement_d;
	public InstructionList breakList_d;		// list of branchhandles added by break
	public InstructionList continueList_d;	// list of branchhandles added by continue
	public InstructionHandle labeledNOP_d;

	public LabeledStatementNode() {
		super();
	}

	/**
	  * Add an instruction to the break list for backpatching.
	  */
	public void addBreak(InstructionHandle ih) {
		if (breakList_d == null) {
			breakList_d = new InstructionList();
		}
		BranchInstruction in = (BranchInstruction) ih.getInstruction();				
		breakList_d.append(in);
		return;
	}
		
	/**
	  * Add an instruction to the continue list for backpatching.
	  */
	public void addContinue(InstructionHandle ih) {
		if (continueList_d == null) {
			continueList_d = new InstructionList();
		}
		BranchInstruction in = (BranchInstruction) ih.getInstruction();		
		continueList_d.append(in);
		return;
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		statement_d.typeCheck(stable);
		return Type.Void;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();

		labeledNOP_d = il.append(new NOP());
		statement_d.translate(classGen, methodGen);
		
		if (breakList_d != null) {
			InstructionHandle breakDest = il.append(new NOP());
			ExpressionNode.backPatch(breakList_d, breakDest);
		}
		
		if (continueList_d != null) {
			Symbol update = new Symbol(label_d, "for");
			LabeledStatementNode node = Espresso.symbolTable().lookupLabel(update);
			if (node != null) {
				labeledNOP_d = node.labeledNOP_d;			
			}
			ExpressionNode.backPatch(continueList_d, labeledNOP_d);
		}
	}	
}
