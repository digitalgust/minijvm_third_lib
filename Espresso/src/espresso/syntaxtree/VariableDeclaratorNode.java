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
import espresso.util.IntType;
import espresso.util.IntegralType;
import espresso.util.PrimitiveType;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import java.math.BigDecimal;

public class VariableDeclaratorNode extends SyntaxTreeNode {

	public Symbol name_d;
	public Type type_d = null;
	public ExpressionNode init_d;
	public boolean field_d;			// true field, false local
	
	public VariableDeclaratorNode(boolean fieldFlag) {
		super();
		field_d = fieldFlag;
	}
	
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

	    if (init_d == null) {
		   return Type.Void;
		}
		
		Type tinit = init_d.typeCheck(stable);
		int distance = tinit.distanceTo(type_d);

		/*
		 * Add an implicit coercion when assigning constant expressions to 
		 * integral types narrower than int. For example 's = 0' should 
		 * type check if s is short, byte or char.
		 */	
		if (distance < 0) {
			if (type_d.integralType() && tinit instanceof IntType) { 
				BigDecimal value = init_d.evaluateExp();
				if (value != null) {
					IntegralType itype = (IntegralType) type_d;
					if (value.scale() > 0 || !itype.inRange(value.longValue())) {
						type_d = Type.Void;			// avoid type checking again
						throw new TypeCheckError(this);
					}
					else {
						init_d = new CastExpNode(init_d, type_d);
						tinit = init_d.typeCheck(stable);
					}
				}
			} 
			else {
				type_d = Type.Void;
				throw new TypeCheckError(this);
			}
		}
		else {		// distance >= 0
			/*
			 * If type_d is primitive we need a widening cast.
			 */
			if (type_d.primitiveType() && distance > 0) {
				init_d = new CastExpNode(init_d, type_d);
				tinit = init_d.typeCheck(stable);
			}
		}

		return Type.Void;
	}

}
