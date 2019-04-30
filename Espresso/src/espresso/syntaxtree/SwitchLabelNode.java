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
import espresso.util.VoidType;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import java.util.Vector;
import java.math.BigDecimal;

public class SwitchLabelNode extends StatementNode {

	public Vector statements_d;
    public ExpressionNode expression_d;		// null if default label

	public SwitchLabelNode() {
		super();
		statements_d = new Vector(5);
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type texp = Type.Void;			

		if (expression_d != null) {			// default label
			texp = expression_d.typeCheck(stable);
		}
		
		if (texp.integralType()) {
			BigDecimal bign = expression_d.evaluateExp();
			if (bign == null) {				// must be constant
				throw new TypeCheckError(this);
			}
		}
		else if (!(texp instanceof VoidType)) {
			throw new TypeCheckError(this);		
		}

		int n = statements_d.size();
		for (int i = 0; i < n; i++) {
			StatementNode stmt = (StatementNode) statements_d.elementAt(i);
			stmt.typeCheck(stable);
		}

		return texp;
	}

}
