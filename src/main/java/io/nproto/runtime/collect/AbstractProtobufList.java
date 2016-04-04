package io.nproto.runtime.collect;

import java.util.AbstractList;
import java.util.Collection;

/**
 * An abstract list which manages mutability semantics. All mutate
 * methods are check if the list is mutable before proceeding. Subclasses must invoke
 * {@link #ensureIsMutable()} manually when overriding those methods.
 */
abstract class AbstractProtobufList<E> extends AbstractList<E> {

  /**
   * Whether or not this list is modifiable.
   */
  private boolean isMutable;

  /**
   * Constructs a mutable list by default.
   */
  AbstractProtobufList() {
    isMutable = true;
  }

  @Override
  public boolean add(E e) {
    ensureIsMutable();
    return super.add(e);
  }

  @Override
  public void add(int index, E element) {
    ensureIsMutable();
    super.add(index, element);
  }

  @Override
  public boolean addAll(Collection<? extends E> c) {
    ensureIsMutable();
    return super.addAll(c);
  }

  @Override
  public boolean addAll(int index, Collection<? extends E> c) {
    ensureIsMutable();
    return super.addAll(index, c);
  }

  @Override
  public void clear() {
    ensureIsMutable();
    super.clear();
  }

  public boolean isModifiable() {
    return isMutable;
  }

  public final void makeImmutable() {
    isMutable = false;
  }

  @Override
  public E remove(int index) {
    ensureIsMutable();
    return super.remove(index);
  }

  @Override
  public boolean remove(Object o) {
    ensureIsMutable();
    return super.remove(o);
  }

  @Override
  public boolean removeAll(Collection<?> c) {
    ensureIsMutable();
    return super.removeAll(c);
  }

  @Override
  public boolean retainAll(Collection<?> c) {
    ensureIsMutable();
    return super.retainAll(c);
  }

  @Override
  public E set(int index, E element) {
    ensureIsMutable();
    return super.set(index, element);
  }

  /**
   * Throws an {@link UnsupportedOperationException} if the list is immutable. Subclasses are
   * responsible for invoking this method on mutate operations.
   */
  protected void ensureIsMutable() {
    if (!isMutable) {
      throw new UnsupportedOperationException();
    }
  }
}

