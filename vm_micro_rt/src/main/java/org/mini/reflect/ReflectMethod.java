/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mini.reflect;

import org.mini.reflect.vm.RConst;
import java.util.ArrayList;
import java.util.List;
import org.mini.reflect.vm.RefNative;

/**
 * 类方法的反射，以mini jvm中的 MethofInfo的实例内存地址进行初始化 初始化中会把内存中的相应变量反射到Method实例中。  <code>
 *      try {
 *          //same as: "abcd".indexOf("cd",1);
 *          String s = "abcd";
 *          Method m;
 *          Reference r = new Reference(RefNative.obj2id(java.lang.String.class));
 *          m = r.getMethod("indexOf", new Class[]{java.lang.String.class, java.lang.Integer.class});
 *          if (m != null) {
 *              Object result = m.invoke(s, new Object[]{"cd", 1});
 *              System.out.println("reflect invoke result:" + result);
 *          }
 *
 *          //same as: new Long(0x1010101020202020L).longValue();
 *          Long lo = new Long(0x1010101020202020L);
 *          r = new Reference(RefNative.obj2id(java.lang.Long.class));
 *          m = r.getMethod("longValue", new Class[]{});
 *          if (m != null) {
 *              Object result = m.invoke(lo, new Object[]{});
 *              System.out.println("reflect invoke result:" + Long.toString((Long) result, 16));
 *          }
 *      } catch (Exception ex) {
 *      }
 * </code>
 *
 * @author gust
 */
public class ReflectMethod {

    //不可随意改动字段类型及名字，要和native一起改
    public long methodId;
    public String methodName;
    public String signature;
    public String genericSignature = "";
    public short accessFlags;
    public long codeStart;
    public long codeEnd;
    public int lines;
    public short[] lineNum;

    public int argCnt;//The number of words in the frame used by arguments. Eight-byte arguments use two words; all others use one. 
    public LocalVarTable[] localVarTable;

    private String[] paras;//参数列表
    private Class[] paras_class;

    public ReflectMethod(long mid) {
        if (mid == 0) {
            throw new IllegalArgumentException();
        }
        //System.out.println("mid:" + mid);
        this.methodId = mid;
        mapMethod(methodId);
        parseMethodPara();
    }

    public Class[] getParameterTypes() {
        return paras_class;
    }

    public String[] getParameterStrs() {
        return paras;
    }

    public int getLineNum(long pc) {
        if (lineNum != null && lineNum.length > 0) {
            for (int i = 0; i < lineNum.length; i += 2) {
                if (pc >= lineNum[lineNum.length - 2 - i]) {
                    return lineNum[lineNum.length - 2 - i + 1];
                }
            }
        }
        return -1;
    }

    public Object invoke(Object obj, Object... args)
            throws IllegalAccessException,
            IllegalArgumentException {
        if (obj == null && ((accessFlags & RConst.ACC_STATIC) == 0)) {//none static method but obj is null
            throw new NullPointerException();
        }
        Class[] pc = getParameterTypes();
        if (args.length != paras.length) {
            throw new IllegalArgumentException();
        } 
        if ((accessFlags & RConst.ACC_PRIVATE) != 0) {
            throw new IllegalAccessException();
        }
        long[] argslong = new long[paras.length];

        for (int i = 0; i < paras.length; i++) {
            switch (paras[i].charAt(0)) {
                case 'S':
                    argslong[i] = ((Short) args[i]);
                    break;
                case 'C':
                    argslong[i] = ((Character) args[i]);
                    break;
                case 'B':
                    argslong[i] = ((Byte) args[i]);
                    break;
                case 'I':
                    argslong[i] = ((Integer) args[i]);
                    break;
                case 'F':
                    argslong[i] = Float.floatToIntBits(((Float) args[i]));
                    break;
                case 'Z':
                    argslong[i] = ((Boolean) args[i]) ? 1 : 0;
                    break;
                case 'D':
                    argslong[i] = Double.doubleToLongBits(((Double) args[i]));
                    break;
                case 'J':
                    argslong[i] = ((Long) args[i]);
                    break;
                default:
                    argslong[i] = RefNative.obj2id(args[i]);
                    break;
            }
        }
        long result = invokeMethod(methodId, obj, argslong);
        char rtype = signature.charAt(signature.indexOf(')') + 1);
        switch (rtype) {
            case 'S':
                return ((short) result);
            case 'C':
                return ((char) result);
            case 'B':
                return ((byte) result);
            case 'I':
                return ((int) result);
            case 'F':
                return Float.intBitsToFloat((int) result);
            case 'Z':
                return (result != 0);
            case 'D':
                return Double.longBitsToDouble((long) result);
            case 'J':
                return result;
            default:
                return RefNative.id2obj(result);
        }
    }

    private void parseMethodPara() {
        String methodType = signature;
        List<String> args = new ArrayList();
        //System.out.println("methodType:" + methodType);
        String s = methodType.substring(methodType.indexOf("(") + 1, methodType.indexOf(")"));
        //从后往前拆分方法参数，从栈中弹出放入本地变量
        while (s.length() > 0) {
            char ch = s.charAt(0);
            String types = "";
            switch (ch) {
                case 'S':
                case 'C':
                case 'B':
                case 'I':
                case 'F':
                case 'Z':
                case 'D':
                case 'J': {
                    String tmps = s.substring(0, 1);
                    args.add(tmps);
                    s = s.substring(1, s.length());
                    break;
                }
                case 'L': {
                    int end = s.indexOf(';') + 1;
                    String tmps = s.substring(0, end);
                    args.add(tmps);
                    s = s.substring(end, s.length());
                    break;
                }
                case '[': {
                    int end = 1;
                    while (s.charAt(end) == '[') {//去掉多维中的 [[[[LObject; 中的 [符
                        end++;
                    }
                    if (s.charAt(end) == 'L') {
                        end = s.indexOf(';') + 1;
                    } else {
                        end++;
                    }
                    String tmps = s.substring(0, end);
                    args.add(tmps);
                    s = s.substring(end, s.length());
                    break;
                }
            }

        }
        paras = args.toArray(new String[args.size()]);
        paras_class = new Class[paras.length];
        for (int i = 0; i < paras.length; i++) {
            paras_class[i] = ReflectClass.getClassBySignature(paras[i]);
        }
    }
    
    
    public Class<?> getReturnType() {
        String s=signature.substring(signature.indexOf(')')+1);
        if(s.equals("V")){
            return Void.TYPE;
        }
        return ReflectClass.getClassBySignature(s);
    }

    public String toString() {
        return Long.toString(methodId, 16) + "|"
                + methodName + "|"
                + signature + "|access:"
                + Integer.toHexString(accessFlags) + "|"
                + codeStart + "|"
                + codeEnd + "|lines:"
                + lines;
    }

    final native void mapMethod(long mid);

    native long invokeMethod(long mid, Object ins, long[] args_long);
}
