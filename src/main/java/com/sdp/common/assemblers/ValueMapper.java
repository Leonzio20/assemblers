package com.sdp.common.assemblers;

import java.util.function.Function;

/**
 * Maps value from source object and sets it on the target.
 *
 * @param <S>
 *   source object type
 * @param <T>
 *   target object type
 * @param <V>
 *   converted value type
 * @author leonzio
 */
final class ValueMapper<S, T, V>
{
  private final Function<S, V> converter;
  private final Setter<T, V> setter;

  ValueMapper(Function<S, V> converter, Setter<T, V> setter)
  {
    this.converter = converter;
    this.setter = setter;
  }

  /**
   * Fills target with value converted from source object.
   *
   * @param source
   *   object to get value from
   * @param target
   *   object to set value to
   */
  void fill(S source, T target)
  {
    V converted = converter.apply(source);
    setter.set(target, converted);
  }
}
