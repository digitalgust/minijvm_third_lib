/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 2/03/98
 *
 */

package espresso.syntaxtree;

import espresso.util.Type;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.util.Vector;

public class StatementExpListNode extends StatementNode {

	public Vector statementExps_d;

	public StatementExpListNode() {
		super();
		statementExps_d = new Vector(5);
	}
    
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		int n = statementExps_d.size();
		for (int i = 0; i < n; i++) {
			StatementExpNode node = (StatementExpNode) statementExps_d.elementAt(i);
			node.typeCheck(stable);
		}
		return Type.Void;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		int n = statementExps_d.size();
		for (int i = 0; i < n; i++) {
			StatementExpNode node = (StatementExpNode) statementExps_d.elementAt(i);
			node.translate(classGen, methodGen);
		}
	}	
	
}
