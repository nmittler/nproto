package io.nproto.example;

import io.nproto.ByteString;
import io.nproto.Internal;
import io.nproto.InternalUtil;
import io.nproto.runtime.MessageRuntime;
import io.nproto.runtime.MessageVisitor;
import io.nproto.runtime.collect.IntArrayList;
import io.nproto.runtime.io.BinaryDecoder;
import io.nproto.runtime.io.BinaryFormat;
import io.nproto.runtime.io.BinarySizeCalculator;

/**
 * Generated runtime for SimpleMessage
 */
@Internal
final class SimpleMessageRuntime implements MessageRuntime<SimpleMessage, SimpleMessageRuntime> {
  private static final SimpleMessageImpl DEFAULT_INSTANCE =
          new SimpleMessageImpl(new SimpleMessageRuntime().makeImmutable());

  private int _int;
  private long _long;
  private boolean _bool;
  private String _string;
  private ByteString _bytes;
  private IntArrayList _repeated;

  private int serializedSize = InternalUtil.INVALID_SIZE;

  public int getInt() {
    return _int;
  }

  public void setInt(int _int) {
    this._int = _int;
  }

  public long getLong() {
    return _long;
  }

  public void setLong(long _long) {
    this._long = _long;
  }

  public boolean getBool() {
    return _bool;
  }

  public void setBool(boolean _bool) {
    this._bool = _bool;
  }

  public String getString() {
    return _string;
  }

  public void setString(String _string) {
    this._string = _string;
  }

  public ByteString getBytes() {
    return _bytes;
  }

  public void setBytes(ByteString _bytes) {
    this._bytes = _bytes;
  }

  public IntArrayList getRepeated() {
    return _repeated;
  }

  public IntArrayList getOrCreateRepeated() {
    if (_repeated == null || _repeated == IntArrayList.emptyList()) {
      _repeated = new IntArrayList();
    }
    return _repeated;
  }

  @Override
  public SimpleMessageRuntime makeImmutable() {
    if (_repeated == null) {
      _repeated = IntArrayList.emptyList();
    } else {
      _repeated.makeImmutable();
    }
    return this;
  }

  @Override
  public SimpleMessageRuntime mutableCopy() {
    SimpleMessageRuntime copy = new SimpleMessageRuntime();
    copy._int = _int;
    copy._long = _long;
    copy._bool = _bool;
    copy._string = _string;
    copy._bytes = _bytes;
    copy._repeated = _repeated != null && _repeated != IntArrayList.emptyList() ? new IntArrayList(_repeated) : null;
    return copy;
  }

  @Override
  public int getRuntimeVersion() {
    return 1;
  }

  @Override
  public SimpleMessage getDefaultInstanceForType() {
    return DEFAULT_INSTANCE;
  }

  @Override
  public int getSerializedMessageSize() {
    // Even though we're updating serialized size, we don't consider this as mutating the
    // object. The calculation should result in the same value each time.
    if (serializedSize == InternalUtil.INVALID_SIZE) {
      BinarySizeCalculator calc = new BinarySizeCalculator();
      visit(calc);
      serializedSize = calc.getSize();
    }
    return serializedSize;
  }

  @Override
  public void visit(MessageVisitor visitor) {
    visitor.visitInt(1, _int);
    visitor.visitLong(2, _long);
    visitor.visitBool(3, _bool);
    visitor.visitString(4, _string);
    visitor.visitBytes(5, _bytes);
    visitor.visitIntList(6, true, _repeated);
  }

  @Override
  public void mergeFrom(byte[] data) {
    BinaryDecoder decoder = new BinaryDecoder(data);
    while (decoder.hasRemaining()) {
      int fieldId = BinaryFormat.getTagFieldNumber(decoder.readTag());
      switch (fieldId) {
        case 1: {
          _int = decoder.readVarint32();
          break;
        }
        case 2: {
          _long = decoder.readVarint64();
          break;
        }
        case 3: {
          _bool = decoder.readBool();
          break;
        }
        case 4: {
          _string = decoder.readString();
          break;
        }
        case 5: {
          _bytes = decoder.readBytes();
          break;
        }
        case 6: {
          decoder.readIntList(fieldId, true, getOrCreateRepeated());
          break;
        }
        default: {
          // Unknown field.
          break;
        }
      }
    }
  }
}
