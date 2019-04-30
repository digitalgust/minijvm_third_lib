/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Santiago M. Pericas
 * Date: 2/24/98
 *
 */

package espresso.syntaxtree;

import espresso.util.Type;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

public class SymbolExpNode extends ExpressionNode {

	public Symbol name_d;

	protected SymbolExpNode(Symbol name) {
		name_d = name;
	}

	protected SymbolExpNode(String name) {
		name_d = new Symbol(name);
	}

	protected SymbolExpNode(Symbol prefix, Symbol suffix) {
		name_d = new Symbol(prefix, suffix);
	}

	protected SymbolExpNode(String prefix, Symbol suffix) {
		name_d = new Symbol(prefix, suffix);
	}

	protected SymbolExpNode(Symbol prefix, String suffix) {
		name_d = new Symbol(prefix, suffix);
	}

	public Symbol name() {
		return name_d;
	}

	public Symbol toSymbol() {
		return name_d;
	}
	
	public String toString() {
		return name_d.toString();
	}
			
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		type_d = Type.createClassType(name_d);
		return type_d;
	}

}
