/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.mini.reflect;

import org.mini.reflect.vm.RConst;
import org.mini.reflect.vm.RefNative;

/**
 * 反射一个数组实例，如
 *
 * int[] a=new int[3]; Array rarr = new Array(RefNative.obj2id(a)); int
 * v=(int)rarr.getVal(2);
 *
 * 即可通过rarr 访问内数组成员
 *
 * @author gust
 */
public class ReflectArray {

    //不可随意改动字段类型及名字，要和native一起改
    public long arrayId;
    public byte type;
    char bytesTag; //'1','2','4','8','R'
    public int length;
    long arr_addr;
    //

    public ReflectArray(long array) {
        arrayId = array;
        mapArray(arrayId);
        bytesTag = RConst.getBytes(type);
    }

    /**
     * return data start memory address
     *
     * @return
     */
    public long getDataPtr() {
        return arr_addr;
    }

    public void setValObj(int index, Object val) {
        switch (bytesTag) {
            case '1': {
                if (type == RConst.TAG_BOOLEAN) {
                    setVal(arrayId, index, ((Boolean) val) ? 1 : 0);

                } else {
                    setVal(arrayId, index, (Byte) val);
                }
                break;
            }
            case '2': {
                if (type == RConst.TAG_CHAR) {
                    setVal(arrayId, index, (Character) val);

                } else {
                    setVal(arrayId, index, (Byte) val);
                }
                break;
            }
            case '4': {
                setVal(arrayId, index, (Integer) val);
                break;
            }
            case '8': {
                setVal(arrayId, index, (Long) val);
                break;
            }
            case 'R': {
                setVal(arrayId, index, RefNative.obj2id(val));
                break;
            }
        }
        throw new IllegalArgumentException();
    }

    public Object getValObj(int index) {

        switch (bytesTag) {
            case '1':
                if (type == RConst.TAG_BOOLEAN) {
                    return getVal(arrayId, index) != 0;
                }
                return ((byte) getVal(arrayId, index));

            case '2':
                if (type == RConst.TAG_CHAR) {
                    return ((char) getVal(arrayId, index));
                }
                return ((short) getVal(arrayId, index));
            case '4':
                return ((int) getVal(arrayId, index));
            case '8':
                return getVal(arrayId, index);
            case 'R': {
                long objptr = getVal(arrayId, index);
                if (objptr != 0) {
                    return RefNative.id2obj(objptr);
                }
                return null;
            }
        }
        throw new IllegalArgumentException();
    }

    final native void mapArray(long classId);

    static native long getVal(long arrayId, int index);

    static native void setVal(long arrayId, int index, long val);

    /*
     * Private
     */
    public static native Object newArray(Class componentType, int length);

    public static native Object multiNewArray(Class componentType,
            int[] dimensions)
            throws IllegalArgumentException;
}
