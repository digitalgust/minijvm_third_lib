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

import espresso.Espresso;
import espresso.util.Type;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.Constants;
import espresso.classfile.classgen.ClassGen;
import espresso.classfile.javaclass.JavaClass;

import java.util.Vector;

public class ClassDeclarationNode extends TypeDeclarationNode {

	public Symbol superName_d = null;
	
	public ClassDeclarationNode() {
		super();
		accessFlags_d = Constants.ACC_SUPER;
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {

		// Check access flags - TODO
		
		// Check the types of overriden methods - TODO

		// Type check methods and fields

		return super.typeCheck(stable);
	}

	public void translate() {

		String[] interfaces = null;
		int n = interfaces_d.size();
		if (n > 0) {
			interfaces = new String[n];
			for (int i = 0; i < n; i++) {
				interfaces[i] = ((Symbol)interfaces_d.elementAt(i)).toString();
			}
		}

		String fileName = Espresso.fileName();
		ClassGen classGen = new ClassGen(name_d.toInternalString(), 
			superName_d.toInternalString(), fileName, accessFlags_d, interfaces);

		n = body_d.size();
		for (int i = 0; i < n; i++) {
			ClassBodyDeclarationNode node = 
				(ClassBodyDeclarationNode) body_d.elementAt(i);
			node.translate(classGen);
		}
		
		JavaClass clazz = classGen.getJavaClass();
		try {
			int last = fileName.lastIndexOf('/');
			String pathName = fileName.substring(0, (last < 0) ? 0 : last + 1);
			clazz.dump(pathName + name_d.baseName() + ".class");
		}
		catch (java.io.IOException e) {
			System.out.println("Error writing class file");
		}	
	}
	
}
