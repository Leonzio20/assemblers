package com.sdp.common.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author leonzio
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class ObjectConstructor
{
  /**
   * Creates new object instance for given class.
   *
   * @param objectClass
   *   to create new object
   * @param <C>
   *   new object class
   *
   * @return created object
   */
  public static <C> @NotNull C construct(@NotNull Class<C> objectClass)
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
