/**
 *
 * Copyright (C) 1998 Boston University
 *
 * Use of this file and the system it is part of is constrained by the
 * file COPYRIGHT in the root directory of this system.  You may, however,
 * make any modifications you wish to this file.
 *
 * Author: Karl Doerig
 * Date: 3/03/98
 *
 */
package espresso.parser;

import espresso.Espresso;
import espresso.util.*;
import espresso.classfile.*;
import espresso.syntaxtree.*;
import espresso.classfile.javaclass.*;

import java.io.*;
import java.util.zip.*;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Stack;
import java.util.Hashtable;
import java.util.StringTokenizer;

public class JavaImportManager {
     
	/**
	  * The name of the default qualified import.
	  */
	final static String DEFAULT_IMPORT = "java.lang.";

	/**
	  * Root class in Java.
	  */
	final static String JAVA_ROOT = "java.lang.Object";

	/**
	  * Vector keeping the directories and zip or jar file paths.
	  */
	Vector classpath_d;

	/**
	  * Reference to the parser's symboltable.
	  */
	SymbolTable	symbolTable_d;
	
	/**
	  * Hashtable to keep track of already loaded class files.
	  */
	Hashtable loadedClasses_d;

	/**
	  * Hashtable to keep track of class names, that exist, and that
	  * are not ambiguous.
	  */
	Hashtable seenClasses_d;

	/**
	  * Hashtable to keep track of class names, that definitely don't
	  * exist as fully qualified names.
	  */
	Hashtable notFoundClasses_d;
		
	/**
	  * Vector holding all qualified imports.
	  */
	Vector qualifiedImports_d;

	/**
	  * Constructor taking the parser's symbol table as parameter. The 
	  * symbol table is used by the ImportManager to insert entries of 
	  * imported classes and their fields and methods.
	  */
	public JavaImportManager(SymbolTable symbol) {
	    classpath_d = new Vector();
	    qualifiedImports_d = new Vector();
	    qualifiedImports_d.addElement(DEFAULT_IMPORT);
	    loadedClasses_d = new Hashtable(101, 0.75f);
	    seenClasses_d = new Hashtable(101, 0.75f);
	    notFoundClasses_d = new Hashtable(101, 0.75f);
		symbolTable_d = symbol;
		setUpClassPath();
	}

	/**
	  * Method to break the class path property string apart into distinct
	  * entries representing either directories containing class files, or
	  * path descriptions ending with a zip or a jar file
	  */
	private void setUpClassPath() {
		try {
			String path = System.getProperty("java.class.path");
// System.out.println("path " + path);
			Vector tempClasspath = split(path,File.pathSeparatorChar);
    
			for (Enumeration e = tempClasspath.elements(); e.hasMoreElements();) {
				String dirname = (String) e.nextElement();
// System.out.println(dirname);
				if (dirname.endsWith(".zip") || dirname.endsWith(".jar")) {
					try {
							ZipFile zipfile = new ZipFile(dirname);
							classpath_d.addElement(zipfile);
					} catch (IOException exc) {
							// ignore if non-existent
					}
					continue;
				}
                           
				File dir = new File(dirname);
				if (dir.exists() && dir.isDirectory()) {
					classpath_d.addElement(dir);
				}
				else {
// System.out.println (dirname + " in classpath is neither zipfile nor jarfile "
// 	+ " nor directory");
				}
			}
		} 
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
	 	}
	}

	/**
	  * The list of starred imports is maintained by the ImportManager. This 
	  * method is used to add an import path to the list of import path to be 
	  * used later to satisfy imports on demand. 
	  */
    public void addQualifiedImport(String qimport) {
		String imp = qimport.substring(0, qimport.lastIndexOf('*'));
		if (qimport.endsWith("*") && !qualifiedImports_d.contains(imp)) {
			qualifiedImports_d.addElement(imp);
			// System.out.println("qualified import added " + imp.toString());
		}
	}
	
    public void addQualifiedImport(Symbol qimport) {
    	addQualifiedImport(qimport.toString());
    }
    
	/**
	  * Returns the fully qualified name of simple name provided that such a 
	  * class exists in the packages defined by the starred imports (without
	  * ambiguity).
	  *
	  * @param simple Name respresenting a class or interface.
 	  * @return The fully qualified name.
	  * @throw Exception If multiple occurrences of simple_name were found.
	  */
    public String fullyQualifyName(String simple) throws Exception {
		String fullName = null;

		for (Enumeration e = qualifiedImports_d.elements(); e.hasMoreElements();) {
			String unit = (String) e.nextElement();

			if (existsClass(unit + simple)) {
				if (fullName == null) {
					fullName = unit + simple;
				} else {
					throw new Exception(simple + " was found as " + fullName 
						+ " and as " + unit+simple);
				}
			}

		}
		if (fullName != null) {
			cacheSeenClassName(new Symbol(fullName));			
		}
		return fullName;
    }

    public Symbol fullyQualifyName(Symbol simple) throws Exception {
  		Symbol fullName = null;
   		
    	try {
     		String fullString = fullyQualifyName(simple.toString());
    		if (fullString != null) {
    			fullName = new Symbol(fullString);
    		}
    	}
    	catch (Exception e) {
    		throw e;
    	}
   		return  fullName;
    }

	/**
	  * Checks whether fully qualified class represented by trueName
	  * was already loaded
	  */
    private boolean classLoaded(Symbol trueSymbol) {
    	return loadedClasses_d.containsKey(trueSymbol);
    }

	/**
	  * Remeber already loaded classes or interfaces
	  */
    private void cacheLoadedClassName(Symbol trueSymbol) {
		if (!classLoaded(trueSymbol)) {
			loadedClasses_d.put(trueSymbol, trueSymbol);
		} 
		cacheSeenClassName(trueSymbol);
    }

	/**
	  * Checks whether fully qualified class represented by trueName
	  * was found to uniquely exist
	  */
    private boolean classNameSeen(Symbol trueSymbol) {
    	return seenClasses_d.containsKey(trueSymbol);
    }

	/**
	  * Remember names of classes that were seen unambiguously
	  */
    private void cacheSeenClassName(Symbol trueSymbol) {
		if (!classNameSeen(trueSymbol)) {
			seenClasses_d.put(trueSymbol, trueSymbol);
		}  	
    }

	/**
	  * Checks whether fully qualified class represented by trueName
	  * was found to uniquely exist
	  */
    private boolean notFoundClassName(Symbol trueSymbol) {
    	return notFoundClasses_d.containsKey(trueSymbol);
    }

	/**
	  * Remember names of classes that were seen unambiguously
	  */
    private void cacheNotFoundClassName(Symbol trueSymbol) {
		if (!classNameSeen(trueSymbol)) {
			notFoundClasses_d.put(trueSymbol, trueSymbol);
		}  	
    }


	/**
	  * Convenience method using symbols instead of String
	  */
    public boolean existsClass(Symbol trueSymbol) {
    	return existsClass(trueSymbol.toString());
    }    	
 
	/**
	  * Checks whether fully qualified class represented by 
	  * truename exists in directories or zip/jar files defined 
	  * in CLASSPATH
	  */
	public boolean existsClass(String trueName) {

		if (classNameSeen(new Symbol(trueName))) {
			return true;
		}

        if (notFoundClassName(new Symbol(trueName))) {
        	return false;
        }
        			
		for (Enumeration e = classpath_d.elements(); e.hasMoreElements();) {
			Object unit = e.nextElement();
			if (unit instanceof ZipFile) {
				ZipFile zipfile = (ZipFile) unit;
				ZipEntry zipentry = zipfile.getEntry(canonicalize(trueName) + ".class");
				if (zipentry == null) {
					continue;
				}
                cacheSeenClassName(new Symbol(trueName));				
				return true;
			}
			File dir = (File) unit;
			File classfile = new File(dir, canonicalize(trueName) + ".class");

			if (classfile.exists()) {
			    cacheSeenClassName(new Symbol(trueName));	
				return true;
			}
               
		}
		cacheNotFoundClassName(new Symbol(trueName));
		return false;
	}
   	    
	/**
	  * Manages the loading of class files by initiating
	  * loading of class header information, loading of fields
	  * and loading of methods
	  * Triggers recursive loading for super classes and interfaces
	  */
   	public boolean readClassFile(Symbol symbol, boolean loadUpToRoot) {

	    String trueName = symbol.toString();
		if (classLoaded(symbol)) {
			// System.out.println("class already loaded " + trueName);
		    TypeDeclarationNode node = symbolTable_d.lookupType(symbol);
		    if (node != null) {
		    	if (loadUpToRoot && !node.rooted_d) {	
                    loadHierarchy(node,loadUpToRoot);
                }
                return true;
            }
            else {
				Espresso.internalError();            	
            }
		}
	
	    // System.out.println("loading classfile " + trueName);

		for (Enumeration e = classpath_d.elements(); e.hasMoreElements();) {
			Object unit = e.nextElement();
			if (unit instanceof ZipFile) {
				ZipFile zipfile = (ZipFile) unit;
				ZipEntry zipentry = zipfile.getEntry(canonicalize(trueName) + ".class");
				if (zipentry == null) {
					continue;
				}
				try {
			    	ClassParser parser = new ClassParser(new DataInputStream(
						new BufferedInputStream(zipfile.getInputStream(zipentry))), null);
			    	JavaClass jclass = parser.parse();
			    	cacheLoadedClassName(new Symbol(trueName));
					loadClassHeader(jclass, loadUpToRoot);
					loadFields(jclass);
					loadMethods(jclass);			    
					return true;
				}
				catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
			}
			File dir = (File) unit;
			File classfile = new File(dir,  canonicalize(trueName) + ".class");

			if (classfile.exists()) {
				try {
			    	ClassParser parser = new ClassParser(new DataInputStream(
						new BufferedInputStream(new FileInputStream(classfile))), null);
			    	JavaClass jclass = parser.parse();
			    	cacheLoadedClassName(new Symbol(trueName));	
					loadClassHeader(jclass, loadUpToRoot);
					loadFields(jclass);
					loadMethods(jclass);		    
					return true;
				}
				catch (Exception ex) {
					ex.printStackTrace();
					return false;
				}
			}
               
		}
		return false;
		
	}

	/**
	  * Manages loading of super classes and interfaces
	  */
	private void loadHierarchy(TypeDeclarationNode node, boolean loadUpToRoot) {

        // if type is java.lang.Object, we are at root anyway
		if (node.name_d.equals(JAVA_ROOT)) {
			node.rooted_d = true;
			return;
		}
		// if we don't need to load up to root, return 
	    if (!loadUpToRoot) {
	        return;
	    }

		// load super class
    	if (node instanceof ClassDeclarationNode) {
    		Symbol superSymbol = ((ClassDeclarationNode)node).superName_d; 
    		if (!node.rooted_d) {
    		   readClassFile(superSymbol, loadUpToRoot);
    		}
    	}
    	// load implemented or inhertited interfaces
    	
    	if (!node.rooted_d) {
    	    int size = node.interfaces_d.size();
			for (int i=0; i < size; i++) {
				Symbol inter = (Symbol) node.interfaces_d.elementAt(i);	    	    
				readClassFile(inter, loadUpToRoot);
			}				
    	
    	}    	
    	node.rooted_d = loadUpToRoot; 
		// set loaded status for superclass and implemented interfaces to whatever was requested

    }

	/**
	  * Manages storing of class or Interface data into symbol table
	  */
	private void loadClassHeader(JavaClass jclass, boolean loadUpToRoot) {
	    TypeDeclarationNode node = null;

	    int acc = jclass.getAccessFlags();
	    if ((acc & Constants.ACC_INTERFACE) == Constants.ACC_INTERFACE) {
	    	node = new InterfaceDeclarationNode();
	    }
	    else {
	    	node = new ClassDeclarationNode();
	    }
		node.accessFlags_d = jclass.getAccessFlags();
		node.name_d = new Symbol(decanonicalize(jclass.getClassName()));
		node.rooted_d = false; 		// set loaded status to false for now
								
		String superName = jclass.getSuperclassName();
		if (node instanceof ClassDeclarationNode) {
		    String dsuperName = decanonicalize(superName);
		    Symbol dsuperSymbol = new Symbol(dsuperName);
		    ((ClassDeclarationNode)node).superName_d = dsuperSymbol;
		}

		String[] interfaces = jclass.getInterfaceNames();
		if (interfaces != null) {
			for (int i = 0; i < interfaces.length; i++) {
				Symbol sinter = new Symbol(decanonicalize(interfaces[i]));

				/*
				 * Implemented interfaces defined in the same package or in
				 * 'java.lang' are not necessarily fully qualified but they
				 * need to be.
				 */
				if (!sinter.isQualified()) {
					// System.out.println("Not qualified " + sinter.toString());
					try {
						sinter = new Symbol(fullyQualifyName(sinter.toString()));
					}
					catch (Exception e) {
						ErrorMsg error = new ErrorMsg(ErrorMsg.AMBITYPE_ERR);
						System.err.println(error.toString(e.getMessage()));
						System.exit(1);			// abort execution !!
					}
				}

				node.interfaces_d.addElement(sinter);				
			}
		}
		symbolTable_d.addType(node.name_d, node);
	    loadHierarchy(node,loadUpToRoot);
	}

	/**
	  * Loads all class fields into symboltable
	  */
	private void loadFields(JavaClass jclass) {

		FieldDeclarationNode field_node;
		VariableDeclaratorNode var_node;		
		Field[]	fields = jclass.getFields();
		for(int i=0; i < fields.length; i++) {
			Field field = fields[i];			
	    	int acc = field.getAccessFlags();
	    	if ((acc & Constants.ACC_PRIVATE) == Constants.ACC_PRIVATE) {
	    		continue;
	    	}

			field_node = new FieldDeclarationNode();
			var_node = new VariableDeclaratorNode(true);			
			field_node.accessFlags_d = field.getAccessFlags(); 
			if ((acc & Constants.ACC_FINAL) ==  Constants.ACC_FINAL) {
				Attribute[] fattr = field.getAttributes();
				for (int j=0; j < fattr.length; j++) {
					Attribute lattr = fattr[j];
					if (lattr instanceof ConstantValue) {
						ConstantValue con = (ConstantValue) lattr;
						setInitializer(var_node, con);				
					}
				}
			}
			var_node.name_d = new Symbol(new Symbol(jclass.getClassName()), field.getName());
			var_node.type_d = Type.createType(decanonicalize(field.getSignature()));
			field_node.fields_d.addElement(var_node);
			if (var_node.type_d instanceof ArrayType) {
			   field_node.type_d = ((ArrayType) var_node.type_d).baseType();
			} 
			else {
			   field_node.type_d = var_node.type_d;
			}
			symbolTable_d.addField(var_node.name_d, field_node);
		}
	
	}

	/**
	  * Try set the initializer field
	  */
	private void setInitializer(VariableDeclaratorNode node, ConstantValue cons) {
		Constant c = cons.getConstantPool().getConstant(cons.getConstantValueIndex());
		String temp = cons.toString();

		switch (c.getTag()) {
			case Constants.CONSTANT_Long: 
			node.init_d = new LongLiteral(new Long(temp)); 
			node.init_d.type_d = Type.Long;
			break;

			case Constants.CONSTANT_Float: 
			try {
				node.init_d = new FloatLiteral(new Float(temp));
				node.init_d.type_d = Type.Float;
			}
			catch (NumberFormatException e) {
				float value = 0.0f;

				if (temp.equals("Infinity")) {
					value = Float.POSITIVE_INFINITY;
				}
				else if (temp.equals("-Infinity")) {
					value = Float.NEGATIVE_INFINITY;
				}
				else if (temp.equals("NaN")) {
					value = Float.NaN;
				}
				else {
					Espresso.internalError();
				}

				node.init_d = new FloatLiteral(new Float(value));
				node.init_d.type_d = Type.Float;
			}
			break;

			case Constants.CONSTANT_Double: 
			try {
				node.init_d = new DoubleLiteral(new Double(temp));
				node.init_d.type_d = Type.Double;
			}
			catch (NumberFormatException e) {
				double value = 0.0;

				if (temp.equals("Infinity")) {
					value = Double.POSITIVE_INFINITY;
				}
				else if (temp.equals("-Infinity")) {
					value = Double.NEGATIVE_INFINITY;
				}
				else if (temp.equals("NaN")) {
					value = Double.NaN;
				}
				else {
					Espresso.internalError();
				}

				node.init_d = new DoubleLiteral(new Double(value));
				node.init_d.type_d = Type.Double;
			}
			break;

			case Constants.CONSTANT_Integer: 
			node.init_d = new IntegerLiteral(new Integer(temp));
			node.init_d.type_d = Type.Int;  
			break;

			case Constants.CONSTANT_String: 
			node.init_d = new StringLiteral(temp);
			node.init_d.type_d = Type.createType(new Symbol("Ljava.lang.String;"));  
			break;

			default:
				Espresso.internalError();
		}
	}

	/**
	  * Loads all class methods into symboltable
	  */
	private void loadMethods(JavaClass jclass) {
		Method[] methods = jclass.getMethods();

		for(int i = 0; i < methods.length; i++) {
			Method method = methods[i];			
	    	int acc = method.getAccessFlags();
	    	if ((acc & Constants.ACC_PRIVATE) == Constants.ACC_PRIVATE) {
	    		continue;
	    	}

			String mname = method.getName();
			if (!mname.equals("<clinit>")) {	// ignore static initializers	
				MethodDeclarationNode node = mname.equals("<init>") ? 
					new ConstructorDeclarationNode() : new MethodDeclarationNode();
				node.accessFlags_d = acc;
				node.name_d = new Symbol(new Symbol(jclass.getClassName()), mname);
				node.type_d = Type.createType(decanonicalize(method.getSignature()));
				symbolTable_d.addMethod(node.name_d, node);
			}
		}
	}

	private void printFields(Field[] fields) {
		for(int i = 0; i < fields.length; i++) {
			System.out.println(fields[i]);
		}
	}
	
	private void printMethods(Method[] methods) {
		Attribute[] attributes;
		for(int i = 0; i < methods.length; i++) {
			System.out.println(methods[i]);

			attributes = methods[i].getAttributes();
			for(int j = 0; j < attributes.length; j++) {
				if(attributes[j].getTag() == Constants.ATTR_CODE)
					System.out.println(attributes[j]); 
			} 
		}
	}	

	private String join(Vector v,String joinString) {
		StringBuffer buffer = new StringBuffer();

		for (Enumeration e = v.elements(); e.hasMoreElements(); buffer.append(joinString)) {
	  		Object o =  e.nextElement();
          	if (o == null)  {
            	continue;
            }
          	buffer.append(o.toString());
		}
		buffer.setLength(buffer.length()-1);
		return buffer.toString();
	}

	private Stack split(String s,char delimiter) {
		Stack v = new Stack();
		StringBuffer buffer = new StringBuffer();
		buffer.append(delimiter);
		StringTokenizer tokenizer = new StringTokenizer(s,buffer.toString(),false);

		while (tokenizer.hasMoreTokens()) {
			v.addElement(tokenizer.nextToken());
		}
		return v;
	}
     
	public String canonicalize(String s) {
		return s.replace('.','/');
	}

	public String decanonicalize(String s) {
		return s.replace('/','.');
	}

}






