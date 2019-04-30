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

package espresso.util;

import espresso.util.IntegerStream;

import java.util.Vector;
import java.lang.String;
import java.lang.Integer;
import java.util.Hashtable;
import java.io.PrintStream;

public class Symbol {

	final static int MAPPING_SIZE = 101;

	/**
	  * A unique id for this symbol. Used as a key in the
	  * hash table.
	  */	
	Integer key_d = null;
	
	/**
	  * The name of the symbol. This is kept to avoid accessing
	  * the hash table several times.
	  */
	String name_d = new String("");
	
	/**
	  * This hash table is used to map symbols to integer in
	  * order to speed up lookups in the SymbolTable. 
	  */
	static IntegerStream counter_d = new IntegerStream();
	static Hashtable mapping_d = new Hashtable(MAPPING_SIZE);
	
	void obtainKey() {
		key_d = (Integer) mapping_d.get(name_d);
		
		if (key_d == null) {
			key_d = counter_d.next();
			mapping_d.put(name_d, key_d);
		}
	}
		
	public Symbol(String name) {
		name_d = name;
		obtainKey();
	}
	
	public Symbol(Symbol prefix, String suffix) {
		if (!prefix.name_d.equals("")) {
			name_d = prefix.name_d + '.';
		}
		name_d += suffix;
		obtainKey();
	}
	
	public Symbol(String prefix, Symbol suffix) {
		name_d = prefix + '.' + suffix.name_d;
		obtainKey();
	}

	public Symbol(Symbol prefix, Symbol suffix) {
		if (!prefix.name_d.equals("")) {
			name_d = prefix.name_d + '.';
		}
		name_d += suffix.name_d;
		obtainKey();
	}

	public Symbol(String prefix, String suffix) {
		if (!prefix.equals("")) {
			name_d = prefix + '.';
		}
		name_d += suffix;
		obtainKey();
	}

	public Symbol(Symbol prefix, Integer scope, Symbol suffix) {
		if (!prefix.name_d.equals("")) {
			name_d = prefix.name_d + '.';
		}
                if(scope==null||suffix==null){
                    int debug=1;
                }
		name_d += scope.toString() + '.' + suffix.name_d;
		obtainKey();
	}

	public Symbol(Symbol prefix, Integer scope, String suffix) {
		if (!prefix.name_d.equals("")) {
			name_d = prefix.name_d + '.';
		}
		name_d += scope.toString() + '.' + suffix;
		obtainKey();
	}

	public int nOfFields() {
		int result = 1;
		int i = name_d.length() - 1;
		while (i >= 0) {
			if (name_d.charAt(i--) == '.') {
				result++;
			}
		}
		return result;
	}
	
	public boolean isQualified() {
		return (name_d.indexOf('.') != -1);
	}
	
	public String baseName() {
		int i = name_d.length() - 1;
		while (i > 0 && name_d.charAt(i) != '.') {
			i--;
		}
		return (i == 0) ? name_d : name_d.substring(i + 1);
	}
	
	/**
	  * Returns the path name of a symbol after skipping over a number of
	  * specified separators, i.e, nskip - 1.
	  */	
	public String baseName(int nskip) {
		int i = name_d.length();
		while (i > 0 && nskip-- > 0) {
			i--;
			while (i > 0 && name_d.charAt(i) != '.') {
				i--;
			}
		}
		return name_d.substring(i + 1);
	}

	public String pathName() {
		int i = name_d.length() - 1;
		while (i > 0 && name_d.charAt(i) != '.') {
			i--;
		}
		return name_d.substring(0, i);
	}

	/**
	  * Returns the path name of a symbol after skipping over a number of
	  * specified separators, i.e, nskip - 1.
	  */	
	public String pathName(int nskip) {
		int i = name_d.length();
		while (i > 0 && nskip-- > 0) {
			i--;
			while (i > 0 && name_d.charAt(i) != '.') {
				i--;
			}
		}
		return name_d.substring(0, i);
	}
	
	public boolean equals(Object obj) {
		Symbol other = (Symbol) obj;
		return other.key_d.equals(key_d);
	}

	public boolean equals(Symbol other) {
		return other.key_d.equals(key_d);
	}
	
	public boolean equals(String other) {
		return (name_d.compareTo(other) == 0);
	}

	public int hashCode() {
		return key_d.intValue();
	}
	
	public String toString() {
		return name_d;
	}
	
	public String toInternalString() {
		return name_d.replace('.', '/');
	}

}
