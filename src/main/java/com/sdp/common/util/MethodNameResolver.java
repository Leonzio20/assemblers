package com.sdp.common.util;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author leonzio
 */
public final class MethodNameResolver
{
  /**
   * Resolves given method name from lambda.
   *
   * @param method
   *   to resolve name from
   * @param <M>
   *   type of passed method. Must implements {@link Serializable}.
   * @return method name
   */
  public static <M extends Serializable> String resolveMethodName(M method)
  {
    Class<?> methodClass = method.getClass();
    Method writeReplace;
    try
    {
      writeReplace = methodClass.getDeclaredMethod("writeReplace");
    }
    catch (NoSuchMethodException e)
    {
      throw new RuntimeException("Method must implement a " + Serializable.class, e);
    }
    writeReplace.setAccessible(true);
    SerializedLambda serializedLambda;
    try
    {
      serializedLambda = (SerializedLambda) writeReplace.invoke(method);
    }
    catch (IllegalAccessException | InvocationTargetException e)
    {
      throw new RuntimeException("", e);
    }
    return serializedLambda.getImplMethodName();
  }
}
