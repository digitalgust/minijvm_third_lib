/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package java.lang;

/**
 *
 * @author gust
 */
public class StackTraceElement {

    private String declaringClass;
    private String methodName;
    private String fileName;
    private int lineNumber;
    
    StackTraceElement parent;

    /**
     * @return the declaringClass
     */
    public String getDeclaringClass() {
        return declaringClass;
    }

    /**
     * @return the methodName
     */
    public String getMethodName() {
        return methodName;
    }

    /**
     * @return the fileName
     */
    public String getFileName() {
        return fileName;
    }

    /**
     * @return the lineNumber
     */
    public int getLineNumber() {
        return lineNumber;
    }
}
