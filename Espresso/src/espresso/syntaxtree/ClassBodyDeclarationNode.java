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
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.Constants;
import espresso.classfile.classgen.ClassGen;

public abstract class ClassBodyDeclarationNode extends SyntaxTreeNode {

	public Type type_d;
	public int accessFlags_d;

	ClassBodyDeclarationNode() {
		accessFlags_d = 0x0;
	}

	public abstract void encodeType();

	public abstract Type typeCheck(SymbolTable stable) 
		throws TypeCheckError;

	public abstract void translate(ClassGen classGen);	

	public Type type() {
		return type_d;
	}
	
	public boolean isFinal() {
		return ((accessFlags_d & Constants.ACC_FINAL) != 0);
	}
	
	public boolean isStatic() {
		return ((accessFlags_d & Constants.ACC_STATIC) != 0);
	}
	
	public boolean isPublic() {
		return ((accessFlags_d & Constants.ACC_PUBLIC) != 0);
	}
	
	public boolean isPrivate() {
		return ((accessFlags_d & Constants.ACC_PRIVATE) != 0);
	}
	
	public boolean isProtected() {
		return ((accessFlags_d & Constants.ACC_PROTECTED) != 0);
	}
	
	public boolean isInstance() {
		return !isStatic();
	}

}

