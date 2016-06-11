package com.google.apps.tiktok.protobuf.experimental;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.apps.tiktok.protobuf.experimental.testing.TestMessage;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.lang.reflect.Field;
import java.util.AbstractList;
import java.util.List;

@RunWith(JUnit4.class)
public class FieldTypeTest {

  @Test
  public void unaryFieldIsValid() {
    assertTrue(FieldType.DOUBLE.isValidForField(field("doubleField")));
    assertTrue(FieldType.FLOAT.isValidForField(field("floatField")));
    assertTrue(FieldType.INT64.isValidForField(field("int64Field")));
    assertTrue(FieldType.UINT64.isValidForField(field("uint64Field")));
    assertTrue(FieldType.INT32.isValidForField(field("int32Field")));
    assertTrue(FieldType.FIXED64.isValidForField(field("fixed64Field")));
    assertTrue(FieldType.FIXED32.isValidForField(field("fixed32Field")));
    assertTrue(FieldType.BOOL.isValidForField(field("boolField")));
    assertTrue(FieldType.STRING.isValidForField(field("stringField")));
    assertTrue(FieldType.MESSAGE.isValidForField(field("messageField")));
    assertTrue(FieldType.BYTES.isValidForField(field("bytesField")));
    assertTrue(FieldType.UINT32.isValidForField(field("uint32Field")));
    assertTrue(FieldType.ENUM.isValidForField(field("enumField")));
    assertTrue(FieldType.SFIXED32.isValidForField(field("sfixed32Field")));
    assertTrue(FieldType.SFIXED64.isValidForField(field("sfixed64Field")));
    assertTrue(FieldType.SINT32.isValidForField(field("sint32Field")));
    assertTrue(FieldType.SINT64.isValidForField(field("sint64Field")));
  }

  @Test
  public void unaryFieldIsNotValid() {
    assertFalse(FieldType.DOUBLE.isValidForField(field("floatField")));
    assertFalse(FieldType.FLOAT.isValidForField(field("doubleField")));
    assertFalse(FieldType.INT64.isValidForField(field("int32Field")));
    assertFalse(FieldType.UINT64.isValidForField(field("uint32Field")));
    assertFalse(FieldType.INT32.isValidForField(field("int64Field")));
    assertFalse(FieldType.FIXED64.isValidForField(field("fixed32Field")));
    assertFalse(FieldType.FIXED32.isValidForField(field("fixed64Field")));
    assertFalse(FieldType.BOOL.isValidForField(field("stringField")));
    assertFalse(FieldType.STRING.isValidForField(field("bytesField")));
    assertFalse(FieldType.BYTES.isValidForField(field("stringField")));
    assertFalse(FieldType.UINT32.isValidForField(field("uint64Field")));
    assertFalse(FieldType.ENUM.isValidForField(field("uint64Field")));
    assertFalse(FieldType.SFIXED32.isValidForField(field("sfixed64Field")));
    assertFalse(FieldType.SFIXED64.isValidForField(field("sfixed32Field")));
    assertFalse(FieldType.SINT32.isValidForField(field("sint64Field")));
    assertFalse(FieldType.SINT64.isValidForField(field("sint32Field")));
  }

  @Test
  public void assignmentToListIsValid() {
    assertTrue(FieldType.DOUBLE_LIST.isValidForField(field("doubleListField")));
    assertTrue(FieldType.FLOAT_LIST.isValidForField(field("floatListField")));
    assertTrue(FieldType.INT64_LIST.isValidForField(field("int64ListField")));
    assertTrue(FieldType.UINT64_LIST.isValidForField(field("uint64ListField")));
    assertTrue(FieldType.INT32_LIST.isValidForField(field("int32ListField")));
    assertTrue(FieldType.FIXED64_LIST.isValidForField(field("fixed64ListField")));
    assertTrue(FieldType.FIXED32_LIST.isValidForField(field("fixed32ListField")));
    assertTrue(FieldType.BOOL_LIST.isValidForField(field("boolListField")));
    assertTrue(FieldType.STRING_LIST.isValidForField(field("stringListField")));
    assertTrue(FieldType.MESSAGE_LIST.isValidForField(field("messageListField")));
    assertTrue(FieldType.BYTES_LIST.isValidForField(field("bytesListField")));
    assertTrue(FieldType.UINT32_LIST.isValidForField(field("uint32ListField")));
    assertTrue(FieldType.ENUM_LIST.isValidForField(field("enumListField")));
    assertTrue(FieldType.SFIXED32_LIST.isValidForField(field("sfixed32ListField")));
    assertTrue(FieldType.SFIXED64_LIST.isValidForField(field("sfixed64ListField")));
    assertTrue(FieldType.SINT32_LIST.isValidForField(field("sint32ListField")));
    assertTrue(FieldType.SINT64_LIST.isValidForField(field("sint64ListField")));
  }

  @Test
  public void assignmentToListIsNotValid() {
    assertFalse(FieldType.DOUBLE_LIST.isValidForField(field("floatListField")));
    assertFalse(FieldType.FLOAT_LIST.isValidForField(field("doubleListField")));
    assertFalse(FieldType.INT64_LIST.isValidForField(field("int32ListField")));
    assertFalse(FieldType.UINT64_LIST.isValidForField(field("uint32ListField")));
    assertFalse(FieldType.INT32_LIST.isValidForField(field("int64ListField")));
    assertFalse(FieldType.FIXED64_LIST.isValidForField(field("fixed32ListField")));
    assertFalse(FieldType.FIXED32_LIST.isValidForField(field("fixed64ListField")));
    assertFalse(FieldType.BOOL_LIST.isValidForField(field("stringListField")));
    assertFalse(FieldType.STRING_LIST.isValidForField(field("bytesListField")));
    assertFalse(FieldType.BYTES_LIST.isValidForField(field("stringListField")));
    assertFalse(FieldType.UINT32_LIST.isValidForField(field("uint64ListField")));
    assertFalse(FieldType.ENUM_LIST.isValidForField(field("uint64ListField")));
    assertFalse(FieldType.SFIXED32_LIST.isValidForField(field("sfixed64ListField")));
    assertFalse(FieldType.SFIXED64_LIST.isValidForField(field("sfixed32ListField")));
    assertFalse(FieldType.SINT32_LIST.isValidForField(field("sint64ListField")));
    assertFalse(FieldType.SINT64_LIST.isValidForField(field("sint32ListField")));
  }

  @Test
  public void complexListHierarchyIsValid() {
    assertTrue(FieldType.INT32_LIST.isValidForField(complexListField("parameterizedList")));
    assertTrue(FieldType.INT32_LIST.isValidForField(complexListField("unparameterizedList")));
  }

  @Test
  public void complexListHierarchyIsNotValid() {
    assertFalse(FieldType.INT64_LIST.isValidForField(complexListField("parameterizedList")));
    assertFalse(FieldType.INT64_LIST.isValidForField(complexListField("unparameterizedList")));
  }

  private static Field field(String name) {
    try {
      return TestMessage.class.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  private static Field complexListField(String name) {
    try {
      return ComplexMessage.class.getDeclaredField(name);
    } catch (NoSuchFieldException e) {
      throw new RuntimeException(e);
    } catch (SecurityException e) {
      throw new RuntimeException(e);
    }
  }

  private interface ParameterizedListInterface<T, S> extends List<S> {}

  private abstract static class UnparameterizedList extends AbstractList<Integer> {}

  @SuppressWarnings("unused")
  private static final class ComplexMessage {
    ParameterizedListInterface<Long, Integer> parameterizedList;
    UnparameterizedList unparameterizedList;
  }
}
