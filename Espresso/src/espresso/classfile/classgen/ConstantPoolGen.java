package  espresso.classfile.classgen;




import  espresso.classfile.Constants;
import  espresso.classfile.javaclass.*;




/** 
 * This class is used to consequently build up a constant pool. The user
 * adds constants via `addXXX' methods, `addString', `addClass', etc.. These methods
 * return an index into the constant pool. Finally, `getFinalConstantPool()' returns
 * the constant pool built up. Intermediate versions of the constant pool can
 * be obtained with `getConstantPool()'. A constant pool has capacity for
 * Constants.MAX_SHORT entries. Note that the first (0) is used by the JVM 
 * and that Double and Long constants need two slots entries.
 *
 * @version 980203
 * @author  <A HREF="http://www.inf.fu-berlin.de/~dahm">M. Dahm</A>
 * @see     Constant
 */
public final class ConstantPoolGen
    implements Constants
{

    private Constant[] constants =  new Constant[MAX_SHORT];
    private ConstantPool cp =  new ConstantPool(constants);
    private int index =  1; // First entry (0) used by JVM


    /**
   * Initialize with given array of constants.
   *
   * @param c array of given constants, new ones will be appended
   */
    public ConstantPoolGen (Constant[] c) {
        System.arraycopy(c, 0, constants, 0, c.length);
        if (c.length > 0) index =  c.length;
    }


    /**
   * Initialize with given constant pool.
   */
    public ConstantPoolGen (ConstantPool cp) {
        this(cp.getConstantPool());
    }


    /**
   * Create empty constant pool.
   */
    public ConstantPoolGen () {

    }


    /** 
   * Look for ConstantString in ConstantPool containing String `str'.
   *
   * @param str String to search for
   * @return index on success, -1 otherwise
   */
    public final int lookupString (String str) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantString) {
                ConstantString s =  (ConstantString)constants[i];
                ConstantUtf8 u8 =  (ConstantUtf8)constants[s.getStringIndex()];
                if (u8.getBytes().equals(str)) return  i;
            }
        }

        return  -1;
    }


    /**
   * Add a new String constant to the ConstantPool, if it is not already in there.
   *
   * @param str String to add
   * @return index of entry
   */
    public final int addString (String str) {
        int ret;
        if ((ret =  lookupString(str)) != -1) return  ret; // Already in CP
        ConstantUtf8 u8 =  new ConstantUtf8(str);
        ConstantString s =  new ConstantString(index);
        constants[index++] =  u8;
        ret =  index;
        constants[index++] =  s;
        return  ret;
    }


    /**
   * Look for ConstantClass in ConstantPool named `str'.
   *
   * @param str String to search for
   * @return index on success, -1 otherwise
   */
    public final int lookupClass (String str) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantClass) {
                ConstantClass c =  (ConstantClass)constants[i];
                ConstantUtf8 u8 =  (ConstantUtf8)constants[c.getNameIndex()];
                if (u8.getBytes().equals(str)) return  i;
            }
        }

        return  -1;
    }


    /**
   * Add a new Class reference to the ConstantPool, if it is not already in there.
   *
   * @param str Class to add
   * @return index of entry
   */
    public final int addClass (String str) {
        String clazz =  str.replace('.', '/');
        int ret;
        if ((ret =  lookupClass(clazz)) != -1) return  ret; // Already in CP
        ConstantUtf8 u8 =  new ConstantUtf8(clazz);
        ConstantClass c =  new ConstantClass(index);
        constants[index++] =  u8;
        ret =  index;
        constants[index++] =  c;
        return  ret;
    }


    /** 
   * Look for ConstantInteger in ConstantPool.
   *
   * @param n integer number to look for
   * @return index on success, -1 otherwise
   */
    public final int lookupInteger (int n) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantInteger) {
                ConstantInteger c =  (ConstantInteger)constants[i];
                if (c.getBytes() == n) return  i;
            }
        }

        return  -1;
    }


    /**
   * Add a new Integer constant to the ConstantPool, if it is not already in there.
   *
   * @param n integer number to add
   * @return index of entry
   */
    public final int addInteger (int n) {
        int ret;
        if ((ret =  lookupInteger(n)) != -1) return  ret; // Already in CP
        ret =  index;
        constants[index++] =  new ConstantInteger(n);
        return  ret;
    }


    /** 
   * Look for ConstantFloat in ConstantPool.
   *
   * @param n Float number to look for
   * @return index on success, -1 otherwise
   */
    public final int lookupFloat (float n) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantFloat) {
                ConstantFloat c =  (ConstantFloat)constants[i];
                if (c.getBytes() == n) return  i;
            }
        }

        return  -1;
    }


    /**
   * Add a new Float constant to the ConstantPool, if it is not already in there.
   *
   * @param n Float number to add
   * @return index of entry
   */
    public final int addFloat (float n) {
        int ret;
        if ((ret =  lookupFloat(n)) != -1) return  ret; // Already in CP
        ret =  index;
        constants[index++] =  new ConstantFloat(n);
        return  ret;
    }


    /** 
   * Look for ConstantUnicode in ConstantPool.
   *
   * @param n Unicode string to look for
   * @return index on success, -1 otherwise
   */
    public final int lookupUnicode (String n) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantUnicode) {
                ConstantUnicode c =  (ConstantUnicode)constants[i];
                if (c.getBytes().equals(n)) return  i;
            }
        }

        return  -1;
    }


    /**
   * Add a new Unicode constant to the ConstantPool, if it is not already in there.
   *
   * @param n Unicode string to add
   * @return index of entry
   */
    public final int addUnicode (String n) {
        int ret;
        if ((ret =  lookupUnicode(n)) != -1) return  ret; // Already in CP
        ret =  index;
        constants[index++] =  new ConstantUnicode(n);
        return  ret;
    }


    /** 
   * Look for ConstantUtf8 in ConstantPool.
   *
   * @param n Utf8 string to look for
   * @return index on success, -1 otherwise
   */
    public final int lookupUtf8 (String n) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantUtf8) {
                ConstantUtf8 c =  (ConstantUtf8)constants[i];
                if (c.getBytes().equals(n)) return  i;
            }
        }

        return  -1;
    }


    /**
   * Add a new Utf8 constant to the ConstantPool, if it is not already in there.
   *
   * @param n Utf8 string to add
   * @return index of entry
   */
    public final int addUtf8 (String n) {
        int ret;
        if ((ret =  lookupUtf8(n)) != -1) return  ret; // Already in CP
        ret =  index;
        constants[index++] =  new ConstantUtf8(n);
        return  ret;
    }


    /** 
   * Look for ConstantLong in ConstantPool.
   *
   * @param n Long number to look for
   * @return index on success, -1 otherwise
   */
    public final int lookupLong (long n) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantLong) {
                ConstantLong c =  (ConstantLong)constants[i];
                if (c.getBytes() == n) return  i;
            }
        }

        return  -1;
    }


    /**
   * Add a new long constant to the ConstantPool, if it is not already in there.
   *
   * @param n Long number to add
   * @return index of entry
   */
    public final int addLong (long n) {
        int ret;
        if ((ret =  lookupLong(n)) != -1) return  ret; // Already in CP
        ret =  index;
        constants[index] =  new ConstantLong(n);
        index +=  2; // Wastes one entry according to spec
        return  ret;
    }


    /** 
   * Look for ConstantDouble in ConstantPool.
   *
   * @param n Double number to look for
   * @return index on success, -1 otherwise
   */
    public final int lookupDouble (double n) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantDouble) {
                ConstantDouble c =  (ConstantDouble)constants[i];
                if (c.getBytes() == n) return  i;
            }
        }

        return  -1;
    }


    /**
   * Add a new double constant to the ConstantPool, if it is not already in there.
   *
   * @param n Double number to add
   * @return index of entry
   */
    public final int addDouble (double n) {
        int ret;
        if ((ret =  lookupDouble(n)) != -1) return  ret; // Already in CP
        ret =  index;
        constants[index] =  new ConstantDouble(n);
        index +=  2; // Wastes one entry according to spec
        return  ret;
    }


    /** 
   * Look for ConstantNameAndType in ConstantPool.
   *
   * @param name of variable/method
   * @param signature of variable/method
   * @return index on success, -1 otherwise
   */
    public final int lookupNameAndType (String name, String signature) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantNameAndType) {
                ConstantNameAndType c =  (ConstantNameAndType)constants[i];
                ConstantUtf8 u8 =  (ConstantUtf8)constants[c.getNameIndex()];
                if (!u8.getBytes().equals(name)) // Name mismatch
                continue;
                u8 =  (ConstantUtf8)constants[c.getSignatureIndex()];
                if (!u8.getBytes().equals(signature)) // Signature mismatch
                continue;
                return  i; // Everything matches
            }
        }

        return  -1;
    }


    /**
   * Add a new NameAndType constant to the ConstantPool if it is not already 
   * in there.
   *
   * @param n NameAndType string to add
   * @return index of entry
   */
    public final int addNameAndType (String name, String signature) {
        int ret;
        int name_index, signature_index;
        if ((ret =  lookupNameAndType(name, signature)) != -1) return  ret; // Already in CP
        name_index =  addUtf8(name);
        signature_index =  addUtf8(signature);
        ret =  index;
        constants[index++] =  new ConstantNameAndType(name_index, signature_index);
        return  ret;
    }


    /** 
   * Look for ConstantMethodref in ConstantPool.
   *
   * @param class_name Where to find method
   * @param method_name Guess what
   * @param signature return and argument types
   * @return index on success, -1 otherwise
   */
    public final int lookupMethodref (String class_name, String method_name, String signature) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantMethodref) {
                ConstantMethodref c =  (ConstantMethodref)constants[i];
                ConstantClass clazz =  (ConstantClass)constants[c.getClassIndex()];
                ConstantNameAndType n =  (ConstantNameAndType)constants[c.getNameAndTypeIndex()];
                ConstantUtf8 u8 =  (ConstantUtf8)constants[clazz.getNameIndex()];
                if (!u8.getBytes().equals(class_name.replace('.', '/'))) // Class name mismatch
                continue;
                u8 =  (ConstantUtf8)constants[n.getNameIndex()];
                if (!u8.getBytes().equals(method_name)) // Method name mismatch
                continue;
                u8 =  (ConstantUtf8)constants[n.getSignatureIndex()];
                if (!u8.getBytes().equals(signature)) // Signature mismatch
                continue;
                return  i; // Everything matches -> success
            }
        }

        return  -1;
    }


    /**
   * Add a new Methodref constant to the ConstantPool, if it is not already 
   * in there.
   *
   * @param n Methodref string to add
   * @return index of entry
   */
    public final int addMethodref (String class_name, String method_name, String signature) {
        int ret, class_index, name_and_type_index;
        if ((ret =  lookupMethodref(class_name, method_name, signature)) != -1) return  ret; // Already in CP
        name_and_type_index =  addNameAndType(method_name, signature);
        class_index =  addClass(class_name);
        ret =  index;
        constants[index++] =  new ConstantMethodref(class_index, name_and_type_index);
        return  ret;
    }


    /** 
   * Look for ConstantInterfaceMethodref in ConstantPool.
   *
   * @param class_name Where to find method
   * @param method_name Guess what
   * @param signature return and argument types
   * @return index on success, -1 otherwise
   */
    public final int lookupInterfaceMethodref (String class_name, String method_name, String signature) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantInterfaceMethodref) {
                ConstantInterfaceMethodref c =  (ConstantInterfaceMethodref)constants[i];
                ConstantClass clazz =  (ConstantClass)constants[c.getClassIndex()];
                ConstantNameAndType n =  (ConstantNameAndType)constants[c.getNameAndTypeIndex()];
                ConstantUtf8 u8 =  (ConstantUtf8)constants[clazz.getNameIndex()];
                if (!u8.getBytes().equals(class_name.replace('.', '/'))) // Class name mismatch
                continue;
                u8 =  (ConstantUtf8)constants[n.getNameIndex()];
                if (!u8.getBytes().equals(method_name)) // Method name mismatch
                continue;
                u8 =  (ConstantUtf8)constants[n.getSignatureIndex()];
                if (!u8.getBytes().equals(signature)) // Signature mismatch
                continue;
                return  i; // Everything matches -> success
            }
        }

        return  -1;
    }


    /**
   * Add a new InterfaceMethodref constant to the ConstantPool, if it is not already 
   * in there.
   *
   * @param n InterfaceMethodref string to add
   * @return index of entry
   */
    public final int addInterfaceMethodref (String class_name, String method_name, String signature) {
        int ret, class_index, name_and_type_index;
        if ((ret =  lookupInterfaceMethodref(class_name, method_name, signature)) != -1) return  ret; // Already in CP
        class_index =  addClass(class_name);
        name_and_type_index =  addNameAndType(method_name, signature);
        ret =  index;
        constants[index++] =  new ConstantInterfaceMethodref(class_index, name_and_type_index);
        return  ret;
    }


    /** 
   * Look for ConstantFieldref in ConstantPool.
   *
   * @param class_name Where to find method
   * @param field_name Guess what
   * @param signature return and argument types
   * @return index on success, -1 otherwise
   */
    public final int lookupFieldref (String class_name, String field_name, String signature) {
        for (int i =  1; i < index; i++) {
            if (constants[i] instanceof ConstantFieldref) {
                ConstantFieldref c =  (ConstantFieldref)constants[i];
                ConstantClass clazz =  (ConstantClass)constants[c.getClassIndex()];
                ConstantNameAndType n =  (ConstantNameAndType)constants[c.getNameAndTypeIndex()];
                ConstantUtf8 u8 =  (ConstantUtf8)constants[clazz.getNameIndex()];
                if (!u8.getBytes().equals(class_name.replace('.', '/'))) // Class name mismatch
                continue;
                u8 =  (ConstantUtf8)constants[n.getNameIndex()];
                if (!u8.getBytes().equals(field_name)) // Method name mismatch
                continue;
                u8 =  (ConstantUtf8)constants[n.getSignatureIndex()];
                if (!u8.getBytes().equals(signature)) // Signature mismatch
                continue;
                return  i; // Everything matches
            }
        }

        return  -1;
    }


    /**
   * Add a new Fieldref constant to the ConstantPool, if it is not already 
   * in there.
   *
   * @param n Fieldref string to add
   * @return index of entry
   */
    public final int addFieldref (String class_name, String field_name, String signature) {
        int ret;
        int class_index, name_and_type_index;
        if ((ret =  lookupFieldref(class_name, field_name, signature)) != -1) return  ret; // Already in CP
        class_index =  addClass(class_name);
        name_and_type_index =  addNameAndType(field_name, signature);
        ret =  index;
        constants[index++] =  new ConstantFieldref(class_index, name_and_type_index);
        return  ret;
    }


    /**
   * Add a given constant (without checking for double entries).
   *
   * @param c Constant to add
   * @return index of entry
   */
    public final int addConstant (Constant c) {
        int ret =  index;
        constants[index++] =  c;
        if ((c instanceof ConstantLong) || (c instanceof ConstantDouble)) constants[index++] =  null;
        return  ret;
    }


    /** 
   * Look up constant in ConstantPool.
   *
   * @param c constant to look up
   * @return index on success, -1 otherwise
   */
    public final int lookupConstant (Constant c) {
        for (int i =  1; i < index; i++) if (constants[i] == c) return  i;
        return  -1;
    }


    /**
   * @param i index in constant pool
   * @return constant pool entry at index i
   */
    public Constant getConstant (int i) {
        return  constants[i];
    }


    /**
   * @return intermediate constant pool
   */
    public ConstantPool getConstantPool () {
        return  cp;
    }


    /**
   * @return current size of constant pool
   */
    public int getSize () {
        return  index;
    }


    /**
   * @return constant pool with proper length
   */
    public ConstantPool getFinalConstantPool () {
        Constant[] cs =  new Constant[index];
        System.arraycopy(constants, 0, cs, 0, index);
        return  new ConstantPool(cs);
    }

}

