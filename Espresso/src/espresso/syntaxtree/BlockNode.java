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
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.util.Vector;

public class BlockNode extends StatementNode {

    public Vector stmts_d;

	public BlockNode() {
		super();
		stmts_d = new Vector(5);
	}

	public void addStatement(StatementNode stmt ) {
		stmts_d.addElement(stmt);	
	}
		
	public void reset() {
		stmts_d.removeAllElements();
	}
	
	public int numberOfStatments() {
		return stmts_d.size();
	}	

	public StatementNode statementAt(int n) {
		return (StatementNode) stmts_d.elementAt(n);
	}
				
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		TypeCheckError last = null;
		Vector errors = Espresso.errors();
		
		int n = stmts_d.size();
		for (int i = 0; i < n; i++) {	
			StatementNode node = (StatementNode) stmts_d.elementAt(i);
			try {
				node.typeCheck(stable);		
			}
			catch (TypeCheckError e) {
				if (last != null) {
					errors.addElement(last.toString());
				}
				last = e;		// last exception raised
			}
		}

		if (last != null) {
			throw last;			// rethrow the last exception
		} 
		
		return Type.Void;
	}
	
	public void translate(ClassGen classGen, MethodGen methodGen) {
		int n = stmts_d.size();
		for (int i = 0; i < n; i++) {	
			StatementNode node = (StatementNode) stmts_d.elementAt(i);
			node.translate(classGen, methodGen);
		}
	}
}
