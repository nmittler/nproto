package io.nproto.schema.reflect;

import static io.nproto.UnsafeUtil.fieldOffset;

import io.nproto.ByteString;
import io.nproto.UnsafeUtil;
import io.nproto.Writer;
import io.nproto.schema.Schema;
import io.nproto.schema.SchemaUtil;
import io.nproto.schema.SchemaUtil.FieldInfo;

import java.util.List;

/**
 * This is similar to what protostuff does. The performance for {@link UnsafeReflectiveSchema} is much
 * better, so not including this in the benchmarks.
 */
final class UnsafeReflectiveSchema2<T> implements Schema<T> {

  private final FieldWriter[] writers;

  static <T> UnsafeReflectiveSchema2<T> newInstance(Class<T> messageType) {
    return new UnsafeReflectiveSchema2<T>(messageType);
  }

  private UnsafeReflectiveSchema2(Class<T> messageType) {
    List<FieldInfo> fields = SchemaUtil.getAllFieldInfo(messageType);
    writers = new FieldWriter[fields.size()];
    int lastFieldNumber = Integer.MAX_VALUE;
    for (int i = 0, dataPos = 0; i < fields.size(); ++i) {
      FieldInfo f = fields.get(i);
      if (f.fieldNumber == lastFieldNumber) {
        throw new RuntimeException("Duplicate field number: " + f.fieldNumber);
      }
      switch(f.fieldType) {
        case DOUBLE:
          writers[i] = new DoubleWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case FLOAT:
          writers[i] = new FloatWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case INT64:
          writers[i] = new Int64Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case UINT64:
          writers[i] = new UInt64Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case INT32:
          writers[i] = new Int32Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case FIXED64:
          writers[i] = new Fixed64Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case FIXED32:
          writers[i] = new Fixed32Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case BOOL:
          writers[i] = new BoolWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case STRING:
          writers[i] = new StringWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case MESSAGE:
          writers[i] = new MessageWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case BYTES:
          writers[i] = new BytesWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case UINT32:
          writers[i] = new UInt32Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case ENUM:
          writers[i] = new EnumWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case SFIXED32:
          writers[i] = new SFixed32Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case SFIXED64:
          writers[i] = new SFixed64Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case SINT32:
          writers[i] = new SInt32Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case SINT64:
          writers[i] = new SInt64Writer(f.fieldNumber, fieldOffset(f.field));
          break;
        case DOUBLE_LIST:
          writers[i] = new DoubleListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case FLOAT_LIST:
          writers[i] = new FloatListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case INT64_LIST:
          writers[i] = new Int64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case UINT64_LIST:
          writers[i] = new UInt64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case INT32_LIST:
          writers[i] = new Int32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case FIXED64_LIST:
          writers[i] = new Fixed64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case FIXED32_LIST:
          writers[i] = new Fixed32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case BOOL_LIST:
          writers[i] = new BoolListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case STRING_LIST:
          writers[i] = new StringListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case MESSAGE_LIST:
          writers[i] = new MessageListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case BYTES_LIST:
          writers[i] = new BytesListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case UINT32_LIST:
          writers[i] = new UInt32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case ENUM_LIST:
          writers[i] = new EnumListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case SFIXED32_LIST:
          writers[i] = new SFixed32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case SFIXED64_LIST:
          writers[i] = new SFixed64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case SINT32_LIST:
          writers[i] = new SInt32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case SINT64_LIST:
          writers[i] = new SInt64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_DOUBLE_LIST:
          writers[i] = new PackedDoubleListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_FLOAT_LIST:
          writers[i] = new PackedFloatListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_INT64_LIST:
          writers[i] = new PackedInt64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_UINT64_LIST:
          writers[i] = new PackedUInt64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_INT32_LIST:
          writers[i] = new PackedInt32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_FIXED64_LIST:
          writers[i] = new PackedFixed64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_FIXED32_LIST:
          writers[i] = new PackedFixed32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_BOOL_LIST:
          writers[i] = new PackedBoolListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_UINT32_LIST:
          writers[i] = new PackedUInt32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_ENUM_LIST:
          writers[i] = new PackedEnumListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_SFIXED32_LIST:
          writers[i] = new PackedSFixed32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_SFIXED64_LIST:
          writers[i] = new PackedSFixed64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_SINT32_LIST:
          writers[i] = new PackedSInt32ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
        case PACKED_SINT64_LIST:
          writers[i] = new PackedSInt64ListWriter(f.fieldNumber, fieldOffset(f.field));
          break;
      }
    }
  }

  @Override
  public void writeTo(T message, Writer writer) {
    for (int i = 0; i < writers.length; ++i) {
      writers[i].writeTo(message, writer);
    }
  }

  private static abstract class FieldWriter {
    final long offset;
    final int fieldNumber;
    FieldWriter(int fieldNumber, long offset) {
      this.fieldNumber = fieldNumber;
      this.offset = offset;
    }

    abstract void writeTo(Object message, Writer writer);
  }

  private static final class DoubleWriter extends FieldWriter {
    public DoubleWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (Double.compare(UnsafeUtil.getDouble(message, offset), 0.0) != 0) {
        writer.writeDouble(fieldNumber, UnsafeUtil.getDouble(message, offset));
      }
    }
  }

  private static final class FloatWriter extends FieldWriter {
    public FloatWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (Float.compare(UnsafeUtil.getFloat(message, offset), 0.0f) != 0) {
        writer.writeFloat(fieldNumber, UnsafeUtil.getFloat(message, offset));
      }
    }
  }
  private static final class Int64Writer extends FieldWriter {
    public Int64Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getLong(message, offset) != 0L) {
        writer.writeInt64(fieldNumber, UnsafeUtil.getLong(message, offset));
      }
    }
  }
  private static final class UInt64Writer extends FieldWriter {
    public UInt64Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getLong(message, offset) != 0L) {
        writer.writeUInt64(fieldNumber, UnsafeUtil.getLong(message, offset));
      }
    }
  }
  private static final class SInt64Writer extends FieldWriter {
    public SInt64Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getLong(message, offset) != 0L) {
        writer.writeSInt64(fieldNumber, UnsafeUtil.getLong(message, offset));
      }
    }
  }
  private static final class Fixed64Writer extends FieldWriter {
    public Fixed64Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getLong(message, offset) != 0L) {
        writer.writeFixed64(fieldNumber, UnsafeUtil.getLong(message, offset));
      }
    }
  }
  private static final class SFixed64Writer extends FieldWriter {
    public SFixed64Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getLong(message, offset) != 0L) {
        writer.writeSFixed64(fieldNumber, UnsafeUtil.getLong(message, offset));
      }
    }
  }
  private static final class Int32Writer extends FieldWriter {
    public Int32Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getInt(message, offset) != 0) {
        writer.writeInt32(fieldNumber, UnsafeUtil.getInt(message, offset));
      }
    }
  }
  private static final class UInt32Writer extends FieldWriter {
    public UInt32Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getInt(message, offset) != 0) {
        writer.writeUInt32(fieldNumber, UnsafeUtil.getInt(message, offset));
      }
    }
  }
  private static final class SInt32Writer extends FieldWriter {
    public SInt32Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getInt(message, offset) != 0) {
        writer.writeSInt32(fieldNumber, UnsafeUtil.getInt(message, offset));
      }
    }
  }
  private static final class Fixed32Writer extends FieldWriter {
    public Fixed32Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getInt(message, offset) != 0) {
        writer.writeFixed32(fieldNumber, UnsafeUtil.getInt(message, offset));
      }
    }
  }
  private static final class SFixed32Writer extends FieldWriter {
    public SFixed32Writer(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getInt(message, offset) != 0) {
        writer.writeSFixed32(fieldNumber, UnsafeUtil.getInt(message, offset));
      }
    }
  }
  private static final class EnumWriter extends FieldWriter {
    public EnumWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getInt(message, offset) != 0) {
        writer.writeEnum(fieldNumber, UnsafeUtil.getInt(message, offset));
      }
    }
  }
  private static final class BoolWriter extends FieldWriter {
    public BoolWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getBoolean(message, offset)) {
        writer.writeBool(fieldNumber, UnsafeUtil.getBoolean(message, offset));
      }
    }
  }
  private static final class StringWriter extends FieldWriter {
    public StringWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeString(fieldNumber, (String) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class BytesWriter extends FieldWriter {
    public BytesWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeBytes(fieldNumber, (ByteString) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class MessageWriter extends FieldWriter {
    public MessageWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeMessage(fieldNumber, UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class DoubleListWriter extends FieldWriter {
    public DoubleListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeDoubleList(fieldNumber, false, (List<Double>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedDoubleListWriter extends FieldWriter {
    public PackedDoubleListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeDoubleList(fieldNumber, true, (List<Double>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class FloatListWriter extends FieldWriter {
    public FloatListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeFloatList(fieldNumber, false, (List<Float>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedFloatListWriter extends FieldWriter {
    public PackedFloatListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeFloatList(fieldNumber, true, (List<Float>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class Int64ListWriter extends FieldWriter {
    public Int64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeInt64List(fieldNumber, false, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedInt64ListWriter extends FieldWriter {
    public PackedInt64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeInt64List(fieldNumber, true, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class UInt64ListWriter extends FieldWriter {
    public UInt64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeUInt64List(fieldNumber, false, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedUInt64ListWriter extends FieldWriter {
    public PackedUInt64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeUInt64List(fieldNumber, true, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class SInt64ListWriter extends FieldWriter {
    public SInt64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeSInt64List(fieldNumber, false, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedSInt64ListWriter extends FieldWriter {
    public PackedSInt64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeSInt64List(fieldNumber, true, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class Fixed64ListWriter extends FieldWriter {
    public Fixed64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeFixed64List(fieldNumber, false, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedFixed64ListWriter extends FieldWriter {
    public PackedFixed64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeFixed64List(fieldNumber, true, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class SFixed64ListWriter extends FieldWriter {
    public SFixed64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeSFixed64List(fieldNumber, false, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedSFixed64ListWriter extends FieldWriter {
    public PackedSFixed64ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeSFixed64List(fieldNumber, true, (List<Long>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class Int32ListWriter extends FieldWriter {
    public Int32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeInt32List(fieldNumber, false, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedInt32ListWriter extends FieldWriter {
    public PackedInt32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeInt32List(fieldNumber, true, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class UInt32ListWriter extends FieldWriter {
    public UInt32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeUInt32List(fieldNumber, false, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedUInt32ListWriter extends FieldWriter {
    public PackedUInt32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeUInt32List(fieldNumber, true, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class SInt32ListWriter extends FieldWriter {
    public SInt32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeSInt32List(fieldNumber, false, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedSInt32ListWriter extends FieldWriter {
    public PackedSInt32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeSInt32List(fieldNumber, true, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class Fixed32ListWriter extends FieldWriter {
    public Fixed32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeFixed32List(fieldNumber, false, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedFixed32ListWriter extends FieldWriter {
    public PackedFixed32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeFixed32List(fieldNumber, true, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class SFixed32ListWriter extends FieldWriter {
    public SFixed32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeSFixed32List(fieldNumber, false, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedSFixed32ListWriter extends FieldWriter {
    public PackedSFixed32ListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeSFixed32List(fieldNumber, true, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class BoolListWriter extends FieldWriter {
    public BoolListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeBoolList(fieldNumber, false, (List<Boolean>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedBoolListWriter extends FieldWriter {
    public PackedBoolListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeBoolList(fieldNumber, true, (List<Boolean>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class EnumListWriter extends FieldWriter {
    public EnumListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeEnumList(fieldNumber, false, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class PackedEnumListWriter extends FieldWriter {
    public PackedEnumListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeEnumList(fieldNumber, true, (List<Integer>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class StringListWriter extends FieldWriter {
    public StringListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeStringList(fieldNumber, (List<String>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class BytesListWriter extends FieldWriter {
    public BytesListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeBytesList(fieldNumber, (List<ByteString>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
  private static final class MessageListWriter extends FieldWriter {
    public MessageListWriter(int fieldNumber, long offset) {
      super(fieldNumber, offset);
    }

    @Override
    @SuppressWarnings("unchecked")
    void writeTo(Object message, Writer writer) {
      if (UnsafeUtil.getObject(message, offset) != null) {
        writer.writeMessageList(fieldNumber, (List<?>) UnsafeUtil.getObject(message, offset));
      }
    }
  }
}
