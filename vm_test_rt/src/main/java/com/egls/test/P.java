/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.egls.test;
   
/**
 *
 * @author gust
 */
public class P  {
     public int p_ins_var=6;
     static public int p_static_var;
    int x;

    P() {
        x=p();
    }

    static public int p() {
        return 65;
    }
    
    int getX(){
        return x;
    }

}
