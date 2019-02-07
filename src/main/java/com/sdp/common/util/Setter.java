package com.sdp.common.util;

import javax.annotation.Nullable;
import javax.validation.constraints.NotNull;

/**
 * Interface for passing setter by method reference.
 *
 * @param <T>
 *   target object type
 * @param <V>
 *   value to set type
 *
 * @author leonzio
 */
@FunctionalInterface
public interface Setter<T, V>
{
  /**
   * Sets value on given target.
   *
   * @param target
   *   to set value on
   * @param value
   *   to set on target
   */
  void set(@NotNull T target, @Nullable V value);
}
