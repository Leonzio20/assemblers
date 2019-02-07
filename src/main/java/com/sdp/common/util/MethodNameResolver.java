package com.sdp.common.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author leonzio
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class MethodNameResolver
{
  /**
   * Resolves given method name from lambda.
   *
   * @param method
   *   to resolve name from
   *
   * @return method name
   */
  public static <S, T> @NotNull String resolveMethodName(@NotNull Getter<S, T> method)
  {
    Class<?> methodClass = method.getClass();
    Method writeReplace;
    try
    {
      writeReplace = methodClass.getDeclaredMethod("writeReplace");
    }
    catch (NoSuchMethodException e)
    {
      throw new IllegalStateException("Method must implement a " + Serializable.class, e);
    }
    SerializedLambda serializedLambda;
    writeReplace.setAccessible(true);
    try
    {
      serializedLambda = (SerializedLambda) writeReplace.invoke(method);
    }
    catch (IllegalAccessException | InvocationTargetException e)
    {
      throw new IllegalStateException("Cannot access method", e);
    }
    return serializedLambda.getImplMethodName();
  }
}
