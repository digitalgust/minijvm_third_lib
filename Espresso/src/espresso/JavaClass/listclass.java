
import java.io.*;
import espresso.classfile.javaclass.*;
import espresso.classfile.Constants;

/**
 * Read class file(s) and display its contents.
 *
 * @version 970819
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 */
public class listclass implements Constants
{

  public static void main(String argv[])
  { 
    String[]    file_name = new String[argv.length];
    int         files=0;
    ClassParser parser=null;
    JavaClass   java_class;
    boolean     code=false, constants=false;
    String      zip_file=null;

    try {
      /* Parse command line arguments.
       */
      for(int i=0; i < argv.length; i++) {
	if(argv[i].charAt(0) == '-') {  // command line switch
	  if(argv[i].equals("-constants"))
	    constants=true;
	  else if(argv[i].equals("-code"))
	    code=true;
	  else if(argv[i].equals("-zip"))
	    zip_file = argv[++i];
	}
	else { // add file name to list
	  file_name[files++] = argv[i];
	}
      }
	
      if(files == 0) {
	System.err.println("dumpclass [-constants|-code|-zip] classfile ...");
      }
      else {
	InputStream in;
	
	for(int i=0; i < files; i++) {
	  if(zip_file == null)
	    parser     = new ClassParser(file_name[i]); // Create parser object
	  else
	    parser     = new ClassParser(zip_file, file_name[i]); // Create parser object
	  java_class = parser.parse();                // Initiate the parsing

	  System.out.println(java_class);             // Dump the contents

	  if(constants) // Dump the constant pool ?
	    System.out.println(java_class.getConstantPool());

	  if(code) // Dump the method code ?
	    printCode(java_class.getMethods());
	}
      }	  
    } catch(Exception e) {
      e.printStackTrace();
    }
  }        
  /**
   * Dump the disassembled code of all methods in the class.
   */
  public static void printCode(Method[] methods)
  {
    Attribute[] attributes;
    for(int i=0; i < methods.length; i++) {
      System.out.println(methods[i]); // public static void main(String argv[])

      attributes = methods[i].getAttributes();


      for(int j=0; j < attributes.length; j++) { // Code is an attribute in 
	if(attributes[j] == null)
	  throw new ClassFormatError("Method contains null attribute");

	if(attributes[j].getTag() == ATTR_CODE)    // Java. Peculiar, but ...
	  System.out.println((Code)attributes[j]); // it works
      }
    }
  }  
}
