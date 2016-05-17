/*
 * Copyright 2014 The Netty Project
 *
 * The Netty Project licenses this file to you under the Apache License, version 2.0 (the
 * "License"); you may not use this file except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package io.nproto.util;

import java.util.AbstractCollection;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 * A hash map of integer primitives that uses open addressing for keys. To minimize
 * the memory footprint, this class uses open addressing rather than chaining. Collisions are
 * resolved using linear probing. Deletions implement compaction, so cost of remove can approach
 * O(N) for full maps, which makes a small loadFactor recommended.
 */
public final class IntToIntHashMap implements Map<Integer, Integer> {
  public static final int NULL_VALUE = Integer.MAX_VALUE;

  /**
   * Default initial capacity. Used if not specified in the constructor
   */
  public static final int DEFAULT_CAPACITY = 8;

  /**
   * Default load factor. Used if not specified in the constructor
   */
  public static final float DEFAULT_LOAD_FACTOR = 0.5f;

  /**
   * Placeholder for null values, so we can use the actual null to mean available.
   * (Better than using a placeholder for available: less references for GC processing.)
   */
  //private static final Object NULL_VALUE = new Object();

  /**
   * The maximum number of elements allowed without allocating more space.
   */
  private int maxSize;

  /**
   * The load factor for the map. Used to calculate {@link #maxSize}.
   */
  private final float loadFactor;

  /**
   * A primitive entry in the map, provided by the iterator from {@link #entries()}
   */
  interface PrimitiveEntry {
    /**
     * Gets the key for this entry.
     */
    int key();

    /**
     * Gets the value for this entry.
     */
    int value();

    /**
     * Sets the value for this entry.
     */
    void setValue(int value);
  }

  private int[] keys;
  private int[] values;
  private int size;
  private int mask;

  private final Set<Integer> keySet = new KeySet();
  private final Set<Entry<Integer, Integer>> entrySet = new EntrySet();
  private final Iterable<PrimitiveEntry> entries = new Iterable<PrimitiveEntry>() {
    @Override
    public Iterator<PrimitiveEntry> iterator() {
      return new PrimitiveIterator();
    }
  };

  public IntToIntHashMap() {
    this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
  }

  public IntToIntHashMap(int initialCapacity) {
    this(initialCapacity, DEFAULT_LOAD_FACTOR);
  }

  public IntToIntHashMap(int initialCapacity, float loadFactor) {
    if (initialCapacity < 1) {
      throw new IllegalArgumentException("initialCapacity must be >= 1");
    }
    if (loadFactor <= 0.0f || loadFactor > 1.0f) {
      // Cannot exceed 1 because we can never store more than capacity elements;
      // using a bigger loadFactor would trigger rehashing before the desired load is reached.
      throw new IllegalArgumentException("loadFactor must be > 0 and <= 1");
    }

    this.loadFactor = loadFactor;

    // Adjust the initial capacity if necessary.
    int capacity = findNextPositivePowerOfTwo(initialCapacity);
    mask = capacity - 1;

    // Allocate the arrays.
    keys = new int[capacity];
    values = new int[capacity];
    Arrays.fill(values, NULL_VALUE);

    // Initialize the maximum size value.
    maxSize = calcMaxSize(capacity);
  }

  private static Integer toExternal(int value) {
    return value == NULL_VALUE ? null : value;
  }
  private static int toInternal(Integer value) {
    return value == null ? NULL_VALUE : value;
  }

  public int get(int key) {
    int index = indexOf(key);
    return index == -1 ? NULL_VALUE : values[index];
  }

  public int put(int key, int value) {
    if (value == NULL_VALUE) {
      throw new IllegalArgumentException("Disallowed value: " + value);
    }

    int startIndex = hashIndex(key);
    int index = startIndex;

    for (; ; ) {
      if (values[index] == NULL_VALUE) {
        // Found empty slot, use it.
        keys[index] = key;
        values[index] = value;
        growSize();
        return NULL_VALUE;
      }
      if (keys[index] == key) {
        // Found existing entry with this key, just replace the value.
        int previousValue = values[index];
        values[index] = toInternal(value);
        return previousValue;
      }

      // Conflict, keep probing ...
      if ((index = probeNext(index)) == startIndex) {
        // Can only happen if the map was full at MAX_ARRAY_SIZE and couldn't grow.
        throw new IllegalStateException("Unable to insert");
      }
    }
  }

  private int probeNext(int index) {
    return index == values.length - 1 ? 0 : index + 1;
  }

  public void putAll(Map<? extends Integer, ? extends Integer> sourceMap) {
    if (sourceMap instanceof IntToIntHashMap) {
      // Optimization - iterate through the arrays.
      @SuppressWarnings("unchecked")
      IntToIntHashMap source = (IntToIntHashMap) sourceMap;
      for (int i = 0; i < source.values.length; ++i) {
        int sourceValue = source.values[i];
        if (sourceValue != NULL_VALUE) {
          put(source.keys[i], sourceValue);
        }
      }
      return;
    }

    // Otherwise, just add each entry.
    for (Entry<? extends Integer, ? extends Integer> entry : sourceMap.entrySet()) {
      put(entry.getKey(), entry.getValue());
    }
  }

  public int remove(int key) {
    int index = indexOf(key);
    if (index == -1) {
      return NULL_VALUE;
    }

    int prev = values[index];
    removeAt(index);
    return prev;
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public boolean isEmpty() {
    return size == 0;
  }

  @Override
  public void clear() {
    Arrays.fill(keys, 0);
    Arrays.fill(values, NULL_VALUE);
    size = 0;
  }

  public boolean containsKey(int key) {
    return indexOf(key) >= 0;
  }

  @Override
  public boolean containsValue(Object value) {
    int v1;
    if (value == null || (v1 = (Integer) value) == NULL_VALUE) {
      return false;
    }
    for (int v2 : values) {
      // The map supports null values; this will be matched as NULL_VALUE.equals(NULL_VALUE).
      if (v1 == v2) {
        return true;
      }
    }
    return false;
  }

  public Iterable<PrimitiveEntry> entries() {
    return entries;
  }

  @Override
  public Collection<Integer> values() {
    return new AbstractCollection<Integer>() {
      @Override
      public Iterator<Integer> iterator() {
        return new Iterator<Integer>() {
          final PrimitiveIterator iter = new PrimitiveIterator();

          @Override
          public boolean hasNext() {
            return iter.hasNext();
          }

          @Override
          public Integer next() {
            return iter.next().value();
          }

          @Override
          public void remove() {
            throw new UnsupportedOperationException();
          }
        };
      }

      @Override
      public int size() {
        return size;
      }
    };
  }

  @Override
  public int hashCode() {
    // Hashcode is based on all non-zero, valid keys. We have to scan the whole keys
    // array, which may have different lengths for two maps of same size(), so the
    // capacity cannot be used as input for hashing but the size can.
    int hash = size;
    for (int key : keys) {
      // 0 can be a valid key or unused slot, but won't impact the hashcode in either case.
      // This way we can use a cheap loop without conditionals, or hard-to-unroll operations,
      // or the devastatingly bad memory locality of visiting value objects.
      // Also, it's important to use a hash function that does not depend on the ordering
      // of terms, only their values; since the map is an unordered collection and
      // entries can end up in different positions in different maps that have the same
      // elements, but with different history of puts/removes, due to conflicts.
      hash ^= hashCode(key);
    }
    return hash;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!(obj instanceof IntToIntHashMap)) {
      return false;
    }
    @SuppressWarnings("rawtypes")
    IntToIntHashMap other = (IntToIntHashMap) obj;
    if (size != other.size()) {
      return false;
    }
    for (int i = 0; i < values.length; ++i) {
      int value = values[i];
      int key = keys[i];
      int otherValue = other.get(key);
      if (value != otherValue) {
        return false;
      }
    }
    return true;
  }

  @Override
  public boolean containsKey(Object key) {
    return containsKey(objectToKey(key));
  }

  @Override
  public Integer get(Object key) {
    return get(objectToKey(key));
  }

  @Override
  public Integer put(Integer key, Integer value) {
    return put(objectToKey(key), (int) value);
  }

  @Override
  public Integer remove(Object key) {
    return remove(objectToKey(key));
  }

  @Override
  public Set<Integer> keySet() {
    return keySet;
  }

  @Override
  public Set<Entry<Integer, Integer>> entrySet() {
    return entrySet;
  }

  private int objectToKey(Object key) {
    return ((Integer) key);
  }

  /**
   * Locates the index for the given key. This method probes using double hashing.
   *
   * @param key the key for an entry in the map.
   * @return the index where the key was found, or {@code -1} if no entry is found for that key.
   */
  private int indexOf(int key) {
    int startIndex = hashIndex(key);
    int index = startIndex;

    for (; ; ) {
      if (values[index] == NULL_VALUE) {
        // It's available, so no chance that this value exists anywhere in the map.
        return -1;
      }
      if (key == keys[index]) {
        return index;
      }

      // Conflict, keep probing ...
      if ((index = probeNext(index)) == startIndex) {
        return -1;
      }
    }
  }

  /**
   * Returns the hashed index for the given key.
   */
  private int hashIndex(int key) {
    return hashCode(key) & mask;
  }

  /**
   * Returns the hash code for the key.
   */
  private static int hashCode(int key) {
    return key;
  }

  /**
   * Grows the map size after an insertion. If necessary, performs a rehash of the map.
   */
  private void growSize() {
    size++;

    if (size > maxSize) {
      if (keys.length == Integer.MAX_VALUE) {
        throw new IllegalStateException("Max capacity reached at size=" + size);
      }

      // Double the capacity.
      rehash(keys.length << 1);
    }
  }

  /**
   * Removes entry at the given index position. Also performs opportunistic, incremental rehashing
   * if necessary to not break conflict chains.
   *
   * @param index the index position of the element to remove.
   */
  private void removeAt(int index) {
    --size;
    // Clearing the key is not strictly necessary (for GC like in a regular collection),
    // but recommended for security. The memory location is still fresh in the cache anyway.
    keys[index] = 0;
    values[index] = NULL_VALUE;

    // In the interval from index to the next available entry, the arrays may have entries
    // that are displaced from their base position due to prior conflicts. Iterate these
    // entries and move them back if possible, optimizing future lookups.
    // Knuth Section 6.4 Algorithm R, also used by the JDK's IdentityHashMap.

    int nextFree = index;
    for (int i = probeNext(index); values[i] != NULL_VALUE; i = probeNext(i)) {
      int bucket = hashIndex(keys[i]);
      if (i < bucket && (bucket <= nextFree || nextFree <= i) ||
              bucket <= nextFree && nextFree <= i) {
        // Move the displaced entry "back" to the first available position.
        keys[nextFree] = keys[i];
        values[nextFree] = values[i];
        // Put the first entry after the displaced entry
        keys[i] = 0;
        values[i] = NULL_VALUE;
        nextFree = i;
      }
    }
  }

  /**
   * Calculates the maximum size allowed before rehashing.
   */
  private int calcMaxSize(int capacity) {
    // Clip the upper bound so that there will always be at least one available slot.
    int upperBound = capacity - 1;
    return Math.min(upperBound, (int) (capacity * loadFactor));
  }

  /**
   * Rehashes the map for the given capacity.
   *
   * @param newCapacity the new capacity for the map.
   */
  private void rehash(int newCapacity) {
    int[] oldKeys = keys;
    int[] oldVals = values;

    keys = new int[newCapacity];
    values = new int[newCapacity];
    Arrays.fill(values, NULL_VALUE);

    maxSize = calcMaxSize(newCapacity);
    mask = newCapacity - 1;

    // Insert to the new arrays.
    for (int i = 0; i < oldVals.length; ++i) {
      int oldVal = oldVals[i];
      if (oldVal != NULL_VALUE) {
        // Inlined put(), but much simpler: we don't need to worry about
        // duplicated keys, growing/rehashing, or failing to insert.
        int oldKey = oldKeys[i];
        int index = hashIndex(oldKey);

        for (; ; ) {
          if (values[index] == NULL_VALUE) {
            keys[index] = oldKey;
            values[index] = oldVal;
            break;
          }

          // Conflict, keep probing. Can wrap around, but never reaches startIndex again.
          index = probeNext(index);
        }
      }
    }
  }

  @Override
  public String toString() {
    if (isEmpty()) {
      return "{}";
    }
    StringBuilder sb = new StringBuilder(4 * size);
    sb.append('{');
    boolean first = true;
    for (int i = 0; i < values.length; ++i) {
      int value = values[i];
      if (value != NULL_VALUE) {
        if (!first) {
          sb.append(", ");
        }
        sb.append(keyToString(keys[i])).append('=').append(value);
        first = false;
      }
    }
    return sb.append('}').toString();
  }

  /**
   * Helper method called by {@link #toString()} in order to convert a single map key into a string.
   * This is protected to allow subclasses to override the appearance of a given key.
   */
  private String keyToString(int key) {
    return Integer.toString(key);
  }

  /**
   * Set implementation for iterating over the entries of the map.
   */
  private final class EntrySet extends AbstractSet<Entry<Integer, Integer>> {
    @Override
    public Iterator<Entry<Integer, Integer>> iterator() {
      return new MapIterator();
    }

    @Override
    public int size() {
      return IntToIntHashMap.this.size();
    }
  }

  /**
   * Set implementation for iterating over the keys.
   */
  private final class KeySet extends AbstractSet<Integer> {
    @Override
    public int size() {
      return IntToIntHashMap.this.size();
    }

    @Override
    public boolean contains(Object o) {
      return IntToIntHashMap.this.containsKey(o);
    }

    @Override
    public boolean remove(Object o) {
      return IntToIntHashMap.this.remove(o) != NULL_VALUE;
    }

    @Override
    public boolean retainAll(Collection<?> retainedKeys) {
      boolean changed = false;
      for (Iterator<PrimitiveEntry> iter = entries().iterator(); iter.hasNext(); ) {
        PrimitiveEntry entry = iter.next();
        if (!retainedKeys.contains(entry.key())) {
          changed = true;
          iter.remove();
        }
      }
      return changed;
    }

    @Override
    public void clear() {
      IntToIntHashMap.this.clear();
    }

    @Override
    public Iterator<Integer> iterator() {
      return new Iterator<Integer>() {
        private final Iterator<Entry<Integer, Integer>> iter = entrySet.iterator();

        @Override
        public boolean hasNext() {
          return iter.hasNext();
        }

        @Override
        public Integer next() {
          return iter.next().getKey();
        }

        @Override
        public void remove() {
          iter.remove();
        }
      };
    }
  }

  /**
   * Iterator over primitive entries. Entry key/values are overwritten by each call to {@link
   * #next()}.
   */
  private final class PrimitiveIterator implements Iterator<PrimitiveEntry>, PrimitiveEntry {
    private int prevIndex = -1;
    private int nextIndex = -1;
    private int entryIndex = -1;

    private void scanNext() {
      for (; ; ) {
        if (++nextIndex == values.length || values[nextIndex] != NULL_VALUE) {
          break;
        }
      }
    }

    @Override
    public boolean hasNext() {
      if (nextIndex == -1) {
        scanNext();
      }
      return nextIndex < keys.length;
    }

    @Override
    public PrimitiveEntry next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      prevIndex = nextIndex;
      scanNext();

      // Always return the same Entry object, just change its index each time.
      entryIndex = prevIndex;
      return this;
    }

    @Override
    public void remove() {
      if (prevIndex < 0) {
        throw new IllegalStateException("next must be called before each remove.");
      }
      removeAt(prevIndex);
      prevIndex = -1;
    }

    // Entry implementation. Since this implementation uses a single Entry, we coalesce that
    // into the Iterator object (potentially making loop optimization much easier).

    @Override
    public int key() {
      return keys[entryIndex];
    }

    @Override
    public int value() {
      return values[entryIndex];
    }

    @Override
    public void setValue(int value) {
      values[entryIndex] = value;
    }
  }

  /**
   * Iterator used by the {@link Map} interface.
   */
  private final class MapIterator implements Iterator<Entry<Integer, Integer>> {
    private final PrimitiveIterator iter = new PrimitiveIterator();

    @Override
    public boolean hasNext() {
      return iter.hasNext();
    }

    @Override
    public Entry<Integer, Integer> next() {
      if (!hasNext()) {
        throw new NoSuchElementException();
      }

      iter.next();

      return new MapEntry(iter.entryIndex);
    }

    @Override
    public void remove() {
      iter.remove();
    }
  }

  /**
   * A single entry in the map.
   */
  private final class MapEntry implements Entry<Integer, Integer> {
    private final int entryIndex;

    MapEntry(int entryIndex) {
      this.entryIndex = entryIndex;
    }

    @Override
    public Integer getKey() {
      verifyExists();
      return keys[entryIndex];
    }

    @Override
    public Integer getValue() {
      verifyExists();
      return toExternal(values[entryIndex]);
    }

    @Override
    public Integer setValue(Integer value) {
      verifyExists();
      Integer prevValue = toExternal(values[entryIndex]);
      values[entryIndex] = toInternal(value);
      return prevValue;
    }

    private void verifyExists() {
      if (values[entryIndex] == NULL_VALUE) {
        throw new IllegalStateException("The map entry has been removed");
      }
    }
  }

  /**
   * Fast method of finding the next power of 2 greater than or equal to the supplied value.
   *
   * If the value is {@code <= 0} then 1 will be returned.
   * This method is not suitable for {@link Integer#MIN_VALUE} or numbers greater than 2^30.
   *
   * @param value from which to search for next power of 2
   * @return The next power of 2 or the value itself if it is a power of 2
   */
  private static int findNextPositivePowerOfTwo(final int value) {
    assert value > Integer.MIN_VALUE && value < 0x40000000;
    return 1 << (32 - Integer.numberOfLeadingZeros(value - 1));
  }
}
