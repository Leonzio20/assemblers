package com.sdp.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * @author leonzio
 */
public final class ObjectConstructor
{
  /**
   * Creates new object instance for given class.
   *
   * @param objectClass
   *   to create new object
   * @param <C>
   *   new object class
   * @return created object
   */
  public static <C> C construct(Class<C> objectClass)
  {
    try
    {
      Constructor<C> declaredConstructor = objectClass.getDeclaredConstructor();
      declaredConstructor.setAccessible(true);
      return declaredConstructor.newInstance();
    }
    catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
    {
      throw new IllegalStateException("Cannot create target object of " + objectClass, e);
    }
  }
}
