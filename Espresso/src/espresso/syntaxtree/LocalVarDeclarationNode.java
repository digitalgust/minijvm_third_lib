/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 1/23/98
 *
 */

package espresso.syntaxtree;

import espresso.Espresso;
import espresso.util.Type;
import espresso.util.BooleanType;
import espresso.util.ArrayType;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.classfile.classgen.*;

import java.util.Enumeration;
import java.util.Vector;
import java.math.BigDecimal;

public class LocalVarDeclarationNode extends StatementNode {

	public Type type_d;				// type of the declaration
	public Vector locals_d;			// vector of local VariableDeclaratorNodes
		
	static final int NUMBER_LOCALS = 3;
	
	public LocalVarDeclarationNode() {
		super();
		locals_d = new Vector(NUMBER_LOCALS);
	}
		
	/**
	  * Move ['s from the field name to the type.
	  */
	public void encodeType() {
		VariableDeclaratorNode var;

		int n = locals_d.size();
		for (int i = 0; i < n; i++) {
			var = (VariableDeclaratorNode) locals_d.elementAt(i);
			String name = var.name_d.toString();

			int dims = 0;
			int last = name.length() - 1;
			while (last > 0 && name.charAt(last) == '[') {
				dims++; last--;
			}

			if (dims > 0) {
				Type baseType = type_d;
				
				if (type_d instanceof ArrayType) {
					ArrayType atype = (ArrayType) type_d;
					dims += atype.dims();
					baseType = atype.baseType();
				}		

				var.name_d = new Symbol(name.substring(0, last + 1));
				var.type_d = new ArrayType(baseType, dims);
			} else {
				var.type_d = type_d;
			}
		}
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		int n = locals_d.size();
		for (int i = 0; i < n; i++) {
			VariableDeclaratorNode node = 
				(VariableDeclaratorNode) locals_d.elementAt(i);
			node.typeCheck(stable);		
		}
		return Type.Void;
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();

		int n = locals_d.size();
		for (int i = 0; i < n; i++) {
			VariableDeclaratorNode node = 
				(VariableDeclaratorNode) locals_d.elementAt(i);
			LocalVariableGen lv = methodGen.addLocalVariable(node.name_d.baseName(2), 
				node.type_d.toClassFileType(), null, null);

			BranchHandle gototc = null;
			ExpressionNode init = node.init_d;
			if (init != null) {
				if (init instanceof ArrayInitializerNode) {
					Espresso.notYetImplemented();		// TODO
				}
				else {
					BigDecimal value = init.evaluateExp();
					if (value != null) {
						il.append(init.type().PUSH(cpg, value));
					}
					else {
						gototc = init.translateSynthesized(classGen, methodGen);
					}

					il.append(node.type_d.STORE(lv.getSlot()));
				
					if (gototc != null) {
						gototc.setTargetNoCopy(il.getEnd());
					}
				}
			}
		}

	}

}
