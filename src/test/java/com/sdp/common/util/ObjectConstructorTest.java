package com.sdp.common.util;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author leonzio
 */
class ObjectConstructorTest
{
  @Test
  void testConstruct()
  {
    TestClass created = ObjectConstructor.construct(TestClass.class);

    Assertions.assertThat(created)
      .isNotNull();
  }

  @Test
  void testConstructWithoutNoParameterConstructor()
  {
    Assertions.assertThatThrownBy(() -> ObjectConstructor.construct(ClassWithoutNoParameterConstructor.class))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage(
        "Cannot create target object of class com.sdp.common.util.ObjectConstructorTest$ClassWithoutNoParameterConstructor");
  }

  @NoArgsConstructor
  private static final class TestClass {}

  @AllArgsConstructor
  private static final class ClassWithoutNoParameterConstructor
  {
    private final String field;
  }
}