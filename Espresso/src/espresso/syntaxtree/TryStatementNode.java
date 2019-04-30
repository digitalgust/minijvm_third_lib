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

import espresso.Espresso;

import espresso.classfile.classgen.ATHROW;
import espresso.classfile.classgen.BranchHandle;
import espresso.classfile.classgen.BranchInstruction;
import espresso.classfile.classgen.ClassGen;
import espresso.classfile.classgen.ClassGenType;
import espresso.classfile.classgen.GOTO;
import espresso.classfile.classgen.InstructionHandle;
import espresso.classfile.classgen.InstructionList;
import espresso.classfile.classgen.JSR;
import espresso.classfile.classgen.LocalVariableGen;
import espresso.classfile.classgen.MethodGen;
import espresso.classfile.classgen.NOP;
import espresso.classfile.classgen.RET;

import espresso.util.ClassType;
import espresso.util.ReferenceType;
import espresso.util.ErrorMsg;
import espresso.util.Symbol;
import espresso.util.SymbolTable;
import espresso.util.Type;
import espresso.util.ExceptionStack;
import espresso.util.TypeCheckError;
import espresso.util.VoidType;

import java.util.Vector;
import java.util.Stack;
import java.util.Hashtable;

public class TryStatementNode extends StatementNode {

    public Vector formals_d;				// array of FormalParameterNode
    public Vector catchBlocks_d;			// array of BlockNode
    public BlockNode tryBlock_d;
    public BlockNode finallyBlock_d = null;

    private static Stack finally_backpatch_stack = new Stack();

    public TryStatementNode() {
		super();
		formals_d = new Vector(2);
		catchBlocks_d = new Vector(2);
    }

    private static int temporary_id = 0;
    
    static private int temporary_slot(ClassGen classGen, MethodGen methodGen,
			       ClassGenType classGenType) 
	{
	    return methodGen.addLocalVariable(methodGen.toString() + ":temp:" + temporary_id++,
			classGenType, null, null).getSlot();
    }

    public Type typeCheck(SymbolTable stable) throws TypeCheckError {
		int num_catches = catchBlocks_d.size();

		Vector errors = Espresso.errors();
		TypeCheckError last = null;

		// This should move to parser... see comment there
		if (num_catches == 0 && finallyBlock_d == null) {
	    	throw new TypeCheckError(this);
	    }

		// Get the exception stack
		ExceptionStack es = Espresso.exceptionStack();
		es.pushLevel();

		// Make sure each class is actually throwable
		for (int i = 0; i < num_catches; i++) {
	    	LocalVarDeclarationNode node = 
				(LocalVarDeclarationNode) formals_d.elementAt(i);

		    Type t = (Type) node.type_d;
	    	if (!t.throwable()) {	    
				if (last != null) {
				    errors.addElement(last.toString());
				}
				last = new TypeCheckError(new ErrorMsg(ErrorMsg.UNCTCHBL_ERR,
					  t.name().toString(), node));
		    }
		}

		// Add the caught types to the exception stack
		if (formals_d != null) {
		    es.catches(formals_d);
		}
		
		try {
			tryBlock_d.typeCheck(stable);
	    }
		catch (TypeCheckError e) {
			if (last != null) {
			    errors.addElement(last.toString());
			}
			last = e; 	
	    }

		// Are we trying to catch something that can't be thrown ...
		Vector ut = es.unthrown();
		if (ut.size() != 0) {
		    for (int i = 0; i < ut.size(); i++) {
				SyntaxTreeNode catch_node = this;
				ClassType ut_class = (ClassType) ut.elementAt(i);

				if (ut_class.superTypeOf(Type.Throwable) ||
				    ut_class.superTypeOf(Type.RuntimeException))
				    continue;

				// Where did we try to catch it?
				for (int j = 0; j < num_catches; j++) {
					LocalVarDeclarationNode node = 
						(LocalVarDeclarationNode) formals_d.elementAt(j);

					ClassType t = (ClassType) node.type_d;
					if (t.identicalTo(ut_class)) {
						catch_node = node;
						break;
					}
				}

				if (last != null) {
					errors.addElement(last.toString());
					last = new TypeCheckError(new ErrorMsg(ErrorMsg.UNTHROWN_ERR,
						  ut_class.name().toString(), catch_node));
				}
			}
		}
				  
		// Get the uncaught exceptions and pop the level ...
		Hashtable uc = es.uncaught();
		es.popLevel();

		// ... adding these exceptions to the current block
		es.add_throws(uc);

		for (int i = 0; i < num_catches; ++i) {
			LocalVarDeclarationNode local_d = 
				(LocalVarDeclarationNode) formals_d.elementAt(i);
			BlockNode catchBlock_d = (BlockNode) catchBlocks_d.elementAt(i);

			try {
				local_d.typeCheck(stable);
				catchBlock_d.typeCheck(stable);
		    } 
			catch(TypeCheckError e) {
				if (last != null) 
					errors.addElement(last.toString());
			
				last = e; 
		    }
	    }

		if (finallyBlock_d != null) {
			try {
				finallyBlock_d.typeCheck(stable);
		    }
			catch(TypeCheckError e) {
				if (last != null) 
					errors.addElement(last.toString());
			
				last = e; 
		    }
	    }
	
		if(last != null)
			throw last;

		return Type.Void;
    }

    public void translate(ClassGen classGen, MethodGen methodGen)
    {
	InstructionList il = methodGen.getInstructionList();

	InstructionList finally_wrapper_list = new InstructionList();
	InstructionList finally_actual_list = new InstructionList();
	if(finallyBlock_d != null)
	    finally_backpatch_stack.push(finally_actual_list);
	
	InstructionHandle try_begin = il.append(new NOP());
	tryBlock_d.translate(classGen, methodGen);
	translateBackpatch(classGen, methodGen, new GOTO(null), finally_wrapper_list);
	InstructionHandle try_end = il.append(new NOP());

	int num_catches = catchBlocks_d.size();
	for(int i = 0; i < num_catches; ++i)
	    {
		LocalVarDeclarationNode local_d = (LocalVarDeclarationNode) formals_d.elementAt(i);

		local_d.translate(classGen, methodGen);

		VariableDeclaratorNode var_d = (VariableDeclaratorNode) local_d.locals_d.elementAt(0);

		String lv_name = var_d.name_d.baseName(2);

		int lv_slot = methodGen.lookupLocal(lv_name).getSlot();
		
		InstructionHandle catch_begin = il.append(var_d.type_d.STORE(lv_slot));
		BlockNode catchBlock_d = (BlockNode) catchBlocks_d.elementAt(i);
		catchBlock_d.translate(classGen, methodGen);
		translateBackpatch(classGen, methodGen, new GOTO(null), finally_wrapper_list);
	
		methodGen.addExceptionHandler(try_begin, try_end, 
					      catch_begin,
					      ((ClassType) local_d.type_d).name().toString());
	    }

	if(finallyBlock_d != null)
	    {
		BranchHandle do_finally = translateBackpatch(classGen, 
							     methodGen, 
							     new JSR(null), 
							     finally_actual_list);
		ExpressionNode.backPatch(finally_wrapper_list, do_finally);

		finally_backpatch_stack.pop();
		BranchHandle goto_end = il.append(new GOTO(null));

		int default_handler_lv_slot = temporary_slot(classGen, methodGen, new ClassGenType("java.lang.Throwable"));
		ClassType default_handler_classType = new ClassType("java.lang.Throwable");
		InstructionHandle catch_begin = il.append(default_handler_classType.STORE(default_handler_lv_slot));
		translateBackpatch(classGen, methodGen, new JSR(null), finally_actual_list);
		il.append(default_handler_classType.LOAD(default_handler_lv_slot));
		il.append(new ATHROW());

		methodGen.addExceptionHandler(try_begin, do_finally, 
					      catch_begin,
					      0);
		int return_lv_slot = temporary_slot(classGen, methodGen, new ClassGenType("foo"));
		InstructionHandle finally_begin = il.append(default_handler_classType.STORE(return_lv_slot));
		finallyBlock_d.translate(classGen, methodGen);
		il.append(new RET(return_lv_slot));

		ExpressionNode.backPatch(finally_actual_list, finally_begin);

		goto_end.setTargetNoCopy(il.append(new NOP()));
	    }
	else
	    {
		ExpressionNode.backPatch(finally_wrapper_list, il.append(new NOP()));
	    }
    }

    private BranchHandle translateBackpatch(ClassGen classGen,
					    MethodGen methodGen,
					    BranchInstruction bi,
					    InstructionList backpatch_list)
    {
	BranchHandle bh = methodGen.getInstructionList().append(bi);
	backpatch_list.append(bi);

	return bh;
    }

    public static void translateFinally(ClassGen classGen, 
					MethodGen methodGen)
    {
	InstructionList il = methodGen.getInstructionList();

	int n = finally_backpatch_stack.size();
	for(int i = n - 1; i >= 0; --i)
	    {
		InstructionList backpatch_list = 
		    (InstructionList) finally_backpatch_stack.elementAt(i);

		BranchInstruction jsr = new JSR(null);
		il.append(jsr);
		backpatch_list.append(jsr);
	    }
    }

    public static void translateFinally(ClassGen classGen, 
					MethodGen methodGen,
					Type type_d)
    {
	if(finally_backpatch_stack.empty())
	    return;

	if(type_d instanceof VoidType == false)
	    {
		InstructionList il = methodGen.getInstructionList();

		int temp_slot = temporary_slot(classGen, 
					       methodGen, 
					       type_d.toClassFileType());
		
		il.append(type_d.STORE(temp_slot));
		translateFinally(classGen, methodGen);
		il.append(type_d.LOAD(temp_slot));
	    }
	else
	    {
		translateFinally(classGen, methodGen);
	    }
    }
}
