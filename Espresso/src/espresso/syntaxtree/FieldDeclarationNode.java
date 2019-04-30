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
import espresso.util.ArrayType;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;
import espresso.syntaxtree.VariableDeclaratorNode;

import espresso.classfile.javaclass.Field;
import espresso.classfile.javaclass.Attribute;
import espresso.classfile.javaclass.ConstantValue;
import espresso.classfile.classgen.ClassGen;
import espresso.classfile.classgen.ConstantPoolGen;

import java.util.Vector;

public class FieldDeclarationNode extends ClassBodyDeclarationNode {

	/**
	  *	A Symbol vector with the fields declared. The type of the
	  * elements is VariableDeclaratorNode.
	  */
	public Vector fields_d;			
	
	static final int NUMBER_FIELDS = 3;
	
	public FieldDeclarationNode() {
		super();
		fields_d = new Vector(NUMBER_FIELDS);
	}
		
	/**
	  * Move ['s from the field name to the type.
	  */
	public void encodeType() {
		VariableDeclaratorNode var;

		int n = fields_d.size();
		for (int i = 0; i < n; i++) {
			var = (VariableDeclaratorNode) fields_d.elementAt(i);
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
		/*
		 * Type checking is done in the constructors where the initialization
		 * code is moved after parsing. Except when the field is final and
		 * initialized with a literal.
		 */

		int n = fields_d.size();
		for (int i = 0; i < n; i++) {
			VariableDeclaratorNode node = 
				(VariableDeclaratorNode) fields_d.elementAt(i);

			if (isFinal() && node.init_d instanceof Literal) {
				node.typeCheck(stable);
			}
		}

		return Type.Void;
	}

	public void translate(ClassGen classGen) {
		ConstantPoolGen cpGen = classGen.getConstantPool();

		int n = fields_d.size();
		for (int i = 0; i < n; i++) {
			VariableDeclaratorNode node = 
				(VariableDeclaratorNode) fields_d.elementAt(i);

			Attribute attribs[] = null;

			if (isFinal() && node.init_d instanceof Literal) {
				Literal literal = (Literal) node.init_d;
				int constValue = literal.addToConstantPool(classGen);

				if (constValue > 0) {		// skip inits to null
					attribs = new Attribute[1];
					attribs[0] = new ConstantValue(cpGen.addUtf8("ConstantValue"),
						2, constValue, cpGen.getConstantPool());
				}
			}

			classGen.addField(new Field(accessFlags_d, 
				cpGen.addUtf8(node.name_d.baseName()),
				cpGen.addUtf8(node.type_d.toInternalString()),
				attribs, 
				cpGen.getConstantPool()));
		}
	}			

}
