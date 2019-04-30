/*
 * Copyright C 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package java.util;

import java.io.*;

/**
 * This class implements a hashtable, which maps keys to values. Any
 * non-<code>null</code> object can be used as a key or as a value.
 * <p>
 * To successfully store and retrieve objects from a hashtable, the
 * objects used as keys must implement the <code>hashCode</code>
 * method and the <code>equals</code> method.
 * <p>
 * An instance of <code>Hashtable</code> has two parameters that
 * affect its efficiency: its <i>capacity</i> and its <i>load
 * factor</i>. The load factor in the CLDC implementation of
 * the hashtable class is always 75 percent.  When the number
 * of entries in the hashtable exceeds the product of the
 * load factor and the current capacity, the capacity is increased 
 * by calling the <code>rehash</code> method.
 * <p>
 * If many entries are to be made into a <code>Hashtable</code>,
 * creating it with a sufficiently large capacity may allow the
 * entries to be inserted more efficiently than letting it perform
 * automatic rehashing as needed to grow the table.
 * <p>
 * This example creates a hashtable of numbers. It uses the names of
 * the numbers as keys:
 * <p><blockquote><pre>
 *     Hashtable numbers = new Hashtable();
 *     numbers.put("one", new Integer(1));
 *     numbers.put("two", new Integer(2));
 *     numbers.put("three", new Integer(3));
 * </pre></blockquote>
 * <p>
 * To retrieve a number, use the following code:
 * <p><blockquote><pre>
 *     Integer n = (Integer)numbers.get("two");
 *     if (n != null) {
 *         System.out.println("two = " + n);
 *     }
 * </pre></blockquote>
 *
 * @author  Arthur van Hoff, Nik Shaylor, Antero Taivalsaari
 * @version 12/17/01 (CLDC 1.1)
 * @see     java.lang.Object#equals(java.lang.Object)
 * @see     java.lang.Object#hashCode()
 * @see     java.util.Hashtable#rehash()
 * @since   JDK1.0, CLDC 1.0
 */
public
class Hashtable<K,V> {

    /**
     * The hash table data.
     */
    private transient HashtableEntry table[];

    /**
     * The total number of entries in the hash table.
     */
    private transient int count;

    /**
     * Rehashes the table when count exceeds this threshold.
     */
    private int threshold;

    /**
     * The load factor for the hashtable.  In CLDC,
     * the default load factor is 75%.
     */
    private static final int loadFactorPercent = 75;

    /**
     * Constructs a new, empty hashtable with the specified initial
     * capacity.
     *
     * @param      initialCapacity   the initial capacity of the hashtable.
     * @exception  IllegalArgumentException  if the initial capacity is less
     *             than zero
     * @since      JDK1.0
     */
    public Hashtable(int initialCapacity) {
        if (initialCapacity < 0) {
            throw new IllegalArgumentException();
        }
        if (initialCapacity == 0) {
            initialCapacity = 1;
        }
        table = new HashtableEntry[initialCapacity];
        threshold = (int)((initialCapacity * loadFactorPercent) / 100);
    }

    /**
     * Constructs a new, empty hashtable with a default capacity and load
     * factor.
     *
     * @since   JDK1.0
     */
    public Hashtable() {
        this(11);
    }

    /**
     * Returns the number of keys in this hashtable.
     *
     * @return  the number of keys in this hashtable.
     * @since   JDK1.0
     */
    public int size() {
        return count;
    }

    /**
     * Tests if this hashtable maps no keys to values.
     *
     * @return  <code>true</code> if this hashtable maps no keys to values;
     *          <code>false</code> otherwise.
     * @since   JDK1.0
     */
    public boolean isEmpty() {
        return count == 0;
    }

    /**
     * Returns an enumeration of the keys in this hashtable.
     *
     * @return  an enumeration of the keys in this hashtable.
     * @see     java.util.Enumeration
     * @see     java.util.Hashtable#elements()
     * @since   JDK1.0
     */
    public synchronized Enumeration<K> keys() {
        return new HashtableKeyEnumerator(table);
    }

    /**
     * Returns an enumeration of the values in this hashtable.
     * Use the Enumeration methods on the returned object to fetch the elements
     * sequentially.
     *
     * @return  an enumeration of the values in this hashtable.
     * @see     java.util.Enumeration
     * @see     java.util.Hashtable#keys()
     * @since   JDK1.0
     */
    public synchronized Enumeration<V> elements() {
        return new HashtableValueEnumerator(table);
    }

    /**
     * Tests if some key maps into the specified value in this hashtable.
     * This operation is more expensive than the <code>containsKey</code>
     * method.
     *
     * @param      value   a value to search for.
     * @return     <code>true</code> if some key maps to the
     *             <code>value</code> argument in this hashtable;
     *             <code>false</code> otherwise.
     * @exception  NullPointerException  if the value is <code>null</code>.
     * @see        java.util.Hashtable#containsKey(java.lang.Object)
     * @since      JDK1.0
     */
    public synchronized boolean contains(V value) {
        if (value == null) {
            throw new NullPointerException();
        }

        HashtableEntry<K,V> tab[] = table;
        for (int i = tab.length ; i-- > 0 ;) {
            for (HashtableEntry<K,V> e = tab[i] ; e != null ; e = e.next) {
                if (e.value.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Tests if the specified object is a key in this hashtable.
     *
     * @param   key   possible key.
     * @return  <code>true</code> if the specified object is a key in this
     *          hashtable; <code>false</code> otherwise.
     * @see     java.util.Hashtable#contains(java.lang.Object)
     * @since   JDK1.0
     */
    public synchronized boolean containsKey(K key) {
        HashtableEntry tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (HashtableEntry e = tab[index] ; e != null ; e = e.next) {
            if ((e.hash == hash) && e.key.equals(key)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Returns the value to which the specified key is mapped in this hashtable.
     *
     * @param   key   a key in the hashtable.
     * @return  the value to which the key is mapped in this hashtable;
     *          <code>null</code> if the key is not mapped to any value in
     *          this hashtable.
     * @see     java.util.Hashtable#put(java.lang.Object, java.lang.Object)
     * @since   JDK1.0
     */
    public synchronized V get(K key) {
        HashtableEntry<K,V> tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (HashtableEntry<K,V> e = tab[index] ; e != null ; e = e.next) {
            if ((e.hash == hash) && e.key.equals(key)) {
                return e.value;
            }
        }
        return null;
    }

    /**
     * Rehashes the contents of the hashtable into a hashtable with a
     * larger capacity. This method is called automatically when the
     * number of keys in the hashtable exceeds this hashtable's capacity
     * and load factor.
     *
     * @since   JDK1.0
     */
    protected void rehash() {
        int oldCapacity = table.length;
        HashtableEntry oldTable[] = table;

        int newCapacity = oldCapacity * 2 + 1;
        HashtableEntry newTable[] = new HashtableEntry[newCapacity];

        threshold = (int)((newCapacity * loadFactorPercent) / 100);
        table = newTable;

        for (int i = oldCapacity ; i-- > 0 ;) {
            for (HashtableEntry old = oldTable[i] ; old != null ; ) {
                HashtableEntry e = old;
                old = old.next;

                int index = (e.hash & 0x7FFFFFFF) % newCapacity;
                e.next = newTable[index];
                newTable[index] = e;
            }
        }
    }

    /**
     * Maps the specified <code>key</code> to the specified
     * <code>value</code> in this hashtable. Neither the key nor the
     * value can be <code>null</code>.
     * <p>
     * The value can be retrieved by calling the <code>get</code> method
     * with a key that is equal to the original key.
     *
     * @param      key     the hashtable key.
     * @param      value   the value.
     * @return     the previous value of the specified key in this hashtable,
     *             or <code>null</code> if it did not have one.
     * @exception  NullPointerException  if the key or value is
     *               <code>null</code>.
     * @see     java.lang.Object#equals(java.lang.Object)
     * @see     java.util.Hashtable#get(java.lang.Object)
     * @since   JDK1.0
     */
    public synchronized V put(K key, V value) {
        // Make sure the value is not null
        if (value == null) {
            throw new NullPointerException();
        }

        // Makes sure the key is not already in the hashtable.
        HashtableEntry<K,V> tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (HashtableEntry<K,V> e = tab[index] ; e != null ; e = e.next) {
            if ((e.hash == hash) && e.key.equals(key)) {
                V old = e.value;
                e.value = value;
                return old;
            }
        }

        if (count >= threshold) {
            // Rehash the table if the threshold is exceeded
            rehash();
            return put(key, value);
        }

        // Creates the new entry.
        HashtableEntry e = new HashtableEntry();
        e.hash = hash;
        e.key = key;
        e.value = value;
        e.next = tab[index];
        tab[index] = e;
        count++;
        return null;
    }

    /**
     * Removes the key (and its corresponding value) from this
     * hashtable. This method does nothing if the key is not in the hashtable.
     *
     * @param   key   the key that needs to be removed.
     * @return  the value to which the key had been mapped in this hashtable,
     *          or <code>null</code> if the key did not have a mapping.
     * @since   JDK1.0
     */
    public synchronized V remove(K key) {
        HashtableEntry<K,V> tab[] = table;
        int hash = key.hashCode();
        int index = (hash & 0x7FFFFFFF) % tab.length;
        for (HashtableEntry<K,V> e = tab[index], prev = null ; e != null ; prev = e, e = e.next) {
            if ((e.hash == hash) && e.key.equals(key)) {
                if (prev != null) {
                    prev.next = e.next;
                } else {
                    tab[index] = e.next;
                }
                count--;
                return e.value;
            }
        }
        return null;
    }

    /**
     * Clears this hashtable so that it contains no keys.
     *
     * @since   JDK1.0
     */
    public synchronized void clear() {
        HashtableEntry tab[] = table;
        for (int index = tab.length; --index >= 0; )
            tab[index] = null;
        count = 0;
    }

    /**
     * Returns a rather long string representation of this hashtable.
     *
     * @return  a string representation of this hashtable.
     * @since   JDK1.0
     */
    public synchronized String toString() {
        int max = size() - 1;
        StringBuffer buf = new StringBuffer();
        Enumeration k = keys();
        Enumeration e = elements();
        buf.append("{");

        for (int i = 0; i <= max; i++) {
            String s1 = k.nextElement().toString();
            String s2 = e.nextElement().toString();
            buf.append(s1 + "=" + s2);
            if (i < max) {
                buf.append(", ");
            }
        }
        buf.append("}");
        return buf.toString();
    }

    /**
     * A hashtable enumerator class.  This class should remain opaque
     * to the client. It will use the Enumeration interface.
     */
    class HashtableValueEnumerator implements Enumeration {
        int index;
        HashtableEntry<K,V> table[];
        HashtableEntry<K,V> entry;

        HashtableValueEnumerator(HashtableEntry table[]) {
            this.table = table;
            this.index = table.length;
        }

        public boolean hasMoreElements() {
            if (entry != null) {
                return true;
            }
            while (index-- > 0) {
                if ((entry = table[index]) != null) {
                    return true;
                }
            }
            return false;
        }

        public V nextElement() {
            if (entry == null) {
                while ((index-- > 0) && ((entry = table[index]) == null));
            }
            if (entry != null) {
                HashtableEntry<K,V> e = entry;
                entry = e.next;
                return  e.value;
            }
            throw new NoSuchElementException("HashtableEnumerator");
        }
    }
    
    class HashtableKeyEnumerator implements Enumeration {
        int index;
        HashtableEntry<K,V> table[];
        HashtableEntry<K,V> entry;

        HashtableKeyEnumerator(HashtableEntry table[]) {
            this.table = table;
            this.index = table.length;
        }

        public boolean hasMoreElements() {
            if (entry != null) {
                return true;
            }
            while (index-- > 0) {
                if ((entry = table[index]) != null) {
                    return true;
                }
            }
            return false;
        }

        public K nextElement() {
            if (entry == null) {
                while ((index-- > 0) && ((entry = table[index]) == null));
            }
            if (entry != null) {
                HashtableEntry<K,V> e = entry;
                entry = e.next;
                return e.key;
            }
            throw new NoSuchElementException("HashtableEnumerator");
        }
    }
    
}


/**
 * Hashtable collision list.
 */
class HashtableEntry<K,V> {
    int hash;
    K key;
    V value;
    HashtableEntry<K,V> next;
}

