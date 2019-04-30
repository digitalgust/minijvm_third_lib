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
import espresso.util.ErrorMsg;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

public class SynchronizedStatementNode extends StatementNode {

   	public BlockNode block_d;
   	public ExpressionNode expression_d;

	public SynchronizedStatementNode() {
		super();
	}
		
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		Type texp = expression_d.typeCheck(stable);
		if (texp.referenceType()) {
			block_d.typeCheck(stable);
			return Type.Void;
		}		
		throw new TypeCheckError(new ErrorMsg(ErrorMsg.SYNCHRON_ERR, this));
	}
	
}
