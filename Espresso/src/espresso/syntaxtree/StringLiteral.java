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
import espresso.util.ClassType;
import espresso.util.SymbolTable;
import espresso.util.Symbol;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

public class StringLiteral extends Literal {

	public String value_d;
	
	public StringLiteral(String value) {
		value_d = value;
		encode();
	}
	
	public char charAt(int i) {
		return value_d.charAt(i);
	}
	
	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		type_d = Type.createType(new Symbol("Ljava.lang.String;"));
		return type_d;
	}

	/**
	  * This function encodes sequences like '\n' or '\166' into 
	  * the corresponding ascii char. (Untested !!)
	  */
	public void encode() {
		int length = value_d.length();
		StringBuffer buffer = new StringBuffer(length);
		
		buffer.setLength(length);
		
		int i, j;
		for (i = j = 0; i < length; i++, j++) {
			char ch = value_d.charAt(i);
			
			switch (ch) {
			case '\\':
				ch = value_d.charAt(++i);
				
				switch (ch) {
				case 'n':  buffer.setCharAt(j, '\n'); break;
				case 't':  buffer.setCharAt(j, '\t'); break;
				case 'b':  buffer.setCharAt(j, '\b'); break;
				case 'r':  buffer.setCharAt(j, '\r'); break;
				case 'f':  buffer.setCharAt(j, '\f'); break;
				case '"':  buffer.setCharAt(j, '"');  break;
				case '\\': buffer.setCharAt(j, '\\'); break;
				case '\'': buffer.setCharAt(j, '\''); break;
				case '0': case '1': case '2': case '3':
				case '4': case '5': case '6': case '7':
					int k = 1, octal = 0;

					octal = octal * 8 + (ch - '0');
					while (i < length - 1 && k++ < 3 && ch >= '0' && ch <= '7') {
						ch = value_d.charAt(++i);
						octal = octal * 8 + (ch - '0');
					}
					
					buffer.setCharAt(j, (char) octal);
				break;
				default:
					// throw an exception
				break;				
				}
			default:
				buffer.setCharAt(j, ch);
			}
		}

		if (length != 0) value_d = new String(buffer);
	}

	public void translate(ClassGen classGen, MethodGen methodGen) {
		ConstantPoolGen cpg = classGen.getConstantPool();
		InstructionList il = methodGen.getInstructionList();
		il.append(new PUSH(cpg, value_d));
	}

	public int addToConstantPool(ClassGen classGen) {
		ConstantPoolGen cpGen = classGen.getConstantPool();
		return cpGen.addString(value_d);
	}

}
