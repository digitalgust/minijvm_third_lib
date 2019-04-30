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
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.TypeCheckError;

import espresso.classfile.classgen.*;

import java.util.Vector;

public class ConstructorDeclarationNode extends MethodDeclarationNode {

	public ConstructorInvocationNode consInvocation_d;
	public BlockNode fieldInit_d;
    			
	public ConstructorDeclarationNode() {
		name_d = new Symbol("<init>");
		fieldInit_d = new BlockNode();
		type_d = Type.Void;
	}

	public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		if (consInvocation_d != null) {
			consInvocation_d.typeCheck(stable);
		}
		if (block_d != null) {
			block_d.typeCheck(stable);
		}
		fieldInit_d.typeCheck(stable);
		return Type.Void;
	}

	public void translate( ClassGen classGen ) {
		ConstantPoolGen cpGen = classGen.getConstantPool();
		InstructionList insList = new InstructionList();

		MethodGen method = new MethodGen(accessFlags_d, returnType(), argsType(), 
			argsNames(), name_d.baseName(), name_d.pathName(), insList, cpGen); 

		consInvocation_d.translate (classGen, method);
		fieldInit_d.translate(classGen, method);
		block_d.translate(classGen, method);
		InstructionList il = method.getInstructionList();
		il.append(new RETURN()); // temporary

		/*
		 * Remove the NOPs from the instruction stream and add 
		 * the method to the class.
		 */
		method.removeNOPs();
		classGen.addMethod(method.getMethod(10));
	}	
}
