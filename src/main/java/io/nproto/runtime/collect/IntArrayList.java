package io.nproto.runtime.collect;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

public final class IntArrayList extends AbstractProtobufList<Integer> implements RandomAccess {

  private static final int DEFAULT_CAPACITY = 10;

  private static final IntArrayList EMPTY_LIST = new IntArrayList();
  static {
    EMPTY_LIST.makeImmutable();
  }

  public static IntArrayList emptyList() {
    return EMPTY_LIST;
  }

  /**
   * The backing store for the list.
   */
  private int[] array;

  /**
   * The size of the list distinct from the length of the array. That is, it is the number of
   * elements set in the list.
   */
  private int size;

  /**
   * Constructs a new mutable {@code IntArrayList} with default capacity.
   */
  public IntArrayList() {
    this(DEFAULT_CAPACITY);
  }

  /**
   * Constructs a new mutable {@code IntArrayList} with the provided capacity.
   */
  public IntArrayList(int capacity) {
    array = new int[capacity];
    size = 0;
  }

  /**
   * Constructs a new mutable {@code IntArrayList} containing the same elements as {@code other}.
   */
  public IntArrayList(List<Integer> other) {
    if (other instanceof IntArrayList) {
      IntArrayList list = (IntArrayList) other;
      array = list.array.clone();
      size = list.size;
    } else {
      size = other.size();
      array = new int[size];
      for (int i = 0; i < size; i++) {
        array[i] = other.get(i);
      }
    }
  }

  @Override
  public Integer get(int index) {
    return getInt(index);
  }

  public int getInt(int index) {
    ensureIndexInRange(index);
    return array[index];
  }

  @Override
  public int size() {
    return size;
  }

  @Override
  public Integer set(int index, Integer element) {
    return setInt(index, element);
  }

  public int setInt(int index, int element) {
    ensureIsMutable();
    ensureIndexInRange(index);
    int previousValue = array[index];
    array[index] = element;
    return previousValue;
  }

  @Override
  public void add(int index, Integer element) {
    addInt(index, element);
  }

  public void addInt(int element) {
    addInt(size, element);
  }

  /**
   * Like {@link #add(int, Integer)} but more efficient in that it doesn't box the element.
   */
  private void addInt(int index, int element) {
    ensureIsMutable();
    if (index < 0 || index > size) {
      throw new IndexOutOfBoundsException(makeOutOfBoundsExceptionMessage(index));
    }

    if (size < array.length) {
      // Shift everything over to make room
      System.arraycopy(array, index, array, index + 1, size - index);
    } else {
      // Resize to 1.5x the size
      int length = ((size * 3) / 2) + 1;
      int[] newArray = new int[length];

      // Copy the first part directly
      System.arraycopy(array, 0, newArray, 0, index);

      // Copy the rest shifted over by one to make room
      System.arraycopy(array, index, newArray, index + 1, size - index);
      array = newArray;
    }

    array[index] = element;
    size++;
    modCount++;
  }

  @Override
  public boolean addAll(Collection<? extends Integer> collection) {
    ensureIsMutable();

    if (collection == null) {
      throw new NullPointerException();
    }

    // We specialize when adding another IntArrayList to avoid boxing elements.
    if (!(collection instanceof IntArrayList)) {
      return super.addAll(collection);
    }

    IntArrayList list = (IntArrayList) collection;
    if (list.size == 0) {
      return false;
    }

    int overflow = Integer.MAX_VALUE - size;
    if (overflow < list.size) {
      // We can't actually represent a list this large.
      throw new OutOfMemoryError();
    }

    int newSize = size + list.size;
    if (newSize > array.length) {
      array = Arrays.copyOf(array, newSize);
    }

    System.arraycopy(list.array, 0, array, size, list.size);
    size = newSize;
    modCount++;
    return true;
  }

  @Override
  public boolean remove(Object o) {
    ensureIsMutable();
    for (int i = 0; i < size; i++) {
      if (o.equals(array[i])) {
        System.arraycopy(array, i + 1, array, i, size - i);
        size--;
        modCount++;
        return true;
      }
    }
    return false;
  }

  @Override
  public Integer remove(int index) {
    ensureIsMutable();
    ensureIndexInRange(index);
    int value = array[index];
    System.arraycopy(array, index + 1, array, index, size - index);
    size--;
    modCount++;
    return value;
  }

  /**
   * Ensures that the provided {@code index} is within the range of {@code [0, size]}. Throws an
   * {@link IndexOutOfBoundsException} if it is not.
   *
   * @param index the index to verify is in range
   */
  private void ensureIndexInRange(int index) {
    if (index < 0 || index >= size) {
      throw new IndexOutOfBoundsException(makeOutOfBoundsExceptionMessage(index));
    }
  }

  private String makeOutOfBoundsExceptionMessage(int index) {
    return "Index:" + index + ", Size:" + size;
  }
}