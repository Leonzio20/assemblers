package com.sdp.common.util;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.mock;

import lombok.AllArgsConstructor;
import lombok.Getter;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * @author leonzio
 */
@ExtendWith(MockitoExtension.class)
class MethodNameResolverTest
{
  @Test
  void testResolveMethodName()
  {
    com.sdp.common.util.Getter<TestClass, String> getter = TestClass::getContent;

    String methodName = MethodNameResolver.resolveMethodName(getter);

    assertThat(methodName)
      .isEqualTo("getContent");
  }

  @Test
  void testResolveMethodNameNoSerializableMethod()
  {
    @SuppressWarnings("unchecked")
    com.sdp.common.util.Getter<TestClass, String> method = mock(com.sdp.common.util.Getter.class);

    assertThatThrownBy(() -> MethodNameResolver.resolveMethodName(method))
      .isInstanceOf(IllegalStateException.class)
      .hasMessage("Method must implement a interface java.io.Serializable");
  }

  @AllArgsConstructor(staticName = "of")
  @Getter
  private static class TestClass
  {
    private final String content;
  }
}