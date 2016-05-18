package io.nproto.schema;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import io.nproto.ByteString;
import io.nproto.descriptor.PropertyType;
import io.nproto.PojoMessage;
import io.nproto.Reader;
import io.nproto.Writer;
import io.nproto.util.TestUtil;

import org.junit.Before;
import org.junit.Test;

import java.util.List;

public abstract class AbstractSchemaFactoryTest {

  private PojoMessage msg;
  private Schema<PojoMessage> schema;

  @Before
  public void setup() {
    msg = TestUtil.newTestMessage();
    schema = schema();
  }

  protected abstract Schema<PojoMessage> schema();

  @Test
  public void iteratedFieldsShouldMatchExpected() {
    msg = new PojoMessage();
    for(Field f : schema) {
      switch(f.number()) {
        case 1:
          assertEquals(PropertyType.ENUM, f.type());
          assertNull(f.enumValue(msg, PojoMessage.MyEnum.class));
          msg.enumField = PojoMessage.MyEnum.VALUE1;
          assertEquals(PojoMessage.MyEnum.VALUE1, f.enumValue(msg, PojoMessage.MyEnum.class));
          break;
        case 2:
          break;
        case 3:
          break;
        case 4:
          break;
        case 5:
          break;
        case 6:
          break;
        case 7:
          break;
        case 8:
          break;
        case 9:
          break;
        case 10:
          break;
        case 11:
          break;
        case 12:
          break;
        case 13:
          break;
        case 14:
          break;
        case 15:
          break;
        case 16:
          break;
        case 17:
          break;
        case 18:
          break;
        case 19:
          break;
        case 20:
          break;
        case 21:
          break;
        case 22:
          break;
        case 23:
          break;
        case 24:
          break;
        case 25:
          break;
        case 26:
          break;
        case 27:
          break;
        case 28:
          break;
        case 29:
          break;
        case 30:
          break;
      }
    }
  }

  @Test
  public void testWriteTo() {
    schema.writeTo(msg, new MyWriter());
  }

  @Test
  public void testMergeFrom() {
    Reader reader = new TestUtil.PojoReader(msg);

    PojoMessage newMsg = new PojoMessage();
    schema.mergeFrom(newMsg, reader);
    assertEquals(msg, newMsg);
  }

  // TODO: convert this to a mock.
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
    public <E extends Enum<E>> void writeEnum(int fieldNumber, E value) {
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
    public void writeInt32List(int fieldNumber, List<Integer> value) {
      System.err.println("NM: writeInt32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFixed32List(int fieldNumber, List<Integer> value) {
      System.err.println("NM: writeFixed32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeInt64List(int fieldNumber, List<Long> value) {
      System.err.println("NM: writeInt64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeUInt64List(int fieldNumber, List<Long> value) {
      System.err.println("NM: writeUInt64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFixed64List(int fieldNumber, List<Long> value) {
      System.err.println("NM: writeFixed64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeFloatList(int fieldNumber, List<Float> value) {
      System.err.println("NM: writeFloatList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeDoubleList(int fieldNumber, List<Double> value) {
      System.err.println("NM: writeDoubleList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public <E extends Enum<E>> void writeEnumList(int fieldNumber, List<E> value) {
      System.err.println("NM: writeEnumList, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeBoolList(int fieldNumber, List<Boolean> value) {
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
    public void writeUInt32List(int fieldNumber, List<Integer> value) {
      System.err.println("NM: writeUInt32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSFixed32List(int fieldNumber, List<Integer> value) {
      System.err.println("NM: writeSFixed32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSFixed64List(int fieldNumber, List<Long> value) {
      System.err.println("NM: writeSFixed64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSInt32List(int fieldNumber, List<Integer> value) {
      System.err.println("NM: writeSInt32List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeSInt64List(int fieldNumber, List<Long> value) {
      System.err.println("NM: writeSInt64List, fieldNumber=" + fieldNumber + ", value=" + value);
    }

    @Override
    public void writeMessageList(int fieldNumber, List<?> value) {
      System.err.println("NM: writeMessageList, fieldNumber=" + fieldNumber + ", value=" + value);
    }
  }
}
