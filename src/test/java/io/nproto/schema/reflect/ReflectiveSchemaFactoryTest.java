package io.nproto.schema.reflect;

import io.nproto.ByteString;
import io.nproto.PojoMessage;
import io.nproto.Writer;
import io.nproto.schema.Schema;

import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ReflectiveSchemaFactoryTest {

  @Test
  public void test() {
    UnsafeReflectiveSchemaFactory f = new UnsafeReflectiveSchemaFactory();
    Schema<PojoMessage> schema = f.createSchema(PojoMessage.class);

    PojoMessage msg = new PojoMessage();
    msg.uint32Field = 1;
    msg.int32Field = 2;
    msg.fixedInt32Field = 3;
    msg.sInt32Field = 4;
    msg.sFixedInt32Field = 5;

    msg.uint64Field = 5;
    msg.int64Field = 6;
    msg.fixedInt64Field = 7;
    msg.sInt64Field = 8;
    msg.sFixedInt64Field = 9;

    msg.stringField = "hello world";
    msg.bytesField = ByteString.copyFromUtf8("here are some bytes");
    msg.messageField = new Object();

    msg.uint32ListField = Arrays.asList(1, 2);
    msg.int32ListField = Arrays.asList(3, 4);
    msg.fixedInt32ListField = Arrays.asList(5, 6);
    msg.sInt32ListField = Arrays.asList(7, 8);
    msg.sFixedInt32ListField = Arrays.asList(9, 10);

    msg.uint64ListField = Arrays.asList(1L, 2L);
    msg.int64ListField = Arrays.asList(3L, 4L);
    msg.fixedInt64ListField = Arrays.asList(5L, 6L);
    msg.sInt64ListField = Arrays.asList(7L, 8L);
    msg.sFixedInt64ListField = Arrays.asList(9L, 10L);

    msg.stringListField = Arrays.asList("ab", "cd");
    msg.bytesListField = Arrays.asList(ByteString.copyFromUtf8("ab"), ByteString.copyFromUtf8("cd"));
    msg.messageListField = Arrays.asList(new Object(), new Object());

    schema.writeTo(msg, new MyWriter());
  }
  private static final class MyWriter implements Writer {

    @Override
    public void writeSFixed32(int fieldNumber, int value) {
      System.err.println("NM: writeSFixed32, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeInt64(int fieldNumber, long value) {
      System.err.println("NM: writeInt64, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSFixed64(int fieldNumber, long value) {
      System.err.println("NM: writeSFixed64, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFloat(int fieldNumber, float value) {
      System.err.println("NM: writeFloat, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeDouble(int fieldNumber, double value) {
      System.err.println("NM: writeDouble, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeEnum(int fieldNumber, int value) {
      System.err.println("NM: writeEnum, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeUInt64(int fieldNumber, long value) {
      System.err.println("NM: writeUInt64, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeInt32(int fieldNumber, int value) {
      System.err.println("NM: writeInt32, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFixed64(int fieldNumber, long value) {
      System.err.println("NM: writeFixed64, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFixed32(int fieldNumber, int value) {
      System.err.println("NM: writeFixed32, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeBool(int fieldNumber, boolean value) {
      System.err.println("NM: writeBool, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeString(int fieldNumber, String value) {
      System.err.println("NM: writeString, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeBytes(int fieldNumber, ByteString value) {
      System.err.println("NM: writeBytes, fieldNumber=" + fieldNumber + ", value=" + value.toStringUtf8());
    }

    @Override
    public void writeUInt32(int fieldNumber, int value) {
      System.err.println("NM: writeUInt32, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSInt32(int fieldNumber, int value) {
      System.err.println("NM: writeSInt32, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSInt64(int fieldNumber, long value) {
      System.err.println("NM: writeSInt64, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeMessage(int fieldNumber, Object value) {
      System.err.println("NM: writeMessage, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeInt32List(int fieldNumber, boolean packed, List<Integer> value) {
      System.err.println("NM: writeInt32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFixed32List(int fieldNumber, boolean packed, List<Integer> value) {
      System.err.println("NM: writeFixed32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeInt64List(int fieldNumber, boolean packed, List<Long> value) {
      System.err.println("NM: writeInt64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeUInt64List(int fieldNumber, boolean packed, List<Long> value) {
      System.err.println("NM: writeUInt64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFixed64List(int fieldNumber, boolean packed, List<Long> value) {
      System.err.println("NM: writeFixed64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFloatList(int fieldNumber, boolean packed, List<Float> value) {
      System.err.println("NM: writeFloatList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeDoubleList(int fieldNumber, boolean packed, List<Double> value) {
      System.err.println("NM: writeDoubleList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeEnumList(int fieldNumber, boolean packed, List<Integer> value) {
      System.err.println("NM: writeEnumList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeBoolList(int fieldNumber, boolean packed, List<Boolean> value) {
      System.err.println("NM: writeBoolList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeStringList(int fieldNumber, List<String> value) {
      System.err.println("NM: writeStringList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeBytesList(int fieldNumber, List<ByteString> value) {
      System.err.println("NM: writeBytesList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeUInt32List(int fieldNumber, boolean packed, List<Integer> value) {
      System.err.println("NM: writeUInt32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSFixed32List(int fieldNumber, boolean packed, List<Integer> value) {
      System.err.println("NM: writeSFixed32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSFixed64List(int fieldNumber, boolean packed, List<Long> value) {
      System.err.println("NM: writeSFixed64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSInt32List(int fieldNumber, boolean packed, List<Integer> value) {
      System.err.println("NM: writeSInt32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSInt64List(int fieldNumber, boolean packed, List<Long> value) {
      System.err.println("NM: writeSInt64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeMessageList(int fieldNumber, List<?> value) {
      System.err.println("NM: writeMessageList, fieldNumber=" + fieldNumber + ", value=" + value);
    }
  }
}
