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

import espresso.util.Type;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class EmptyStatementNode extends StatementNode {

	public EmptyStatementNode() {
		super();
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		return Type.Void;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		InstructionList il = methodGen.getInstructionList();
		il.append(new NOP());
	}

}
