package com.sdp.common.assemblers;

import java.util.function.Function;

import com.sdp.common.util.Setter;
import lombok.AllArgsConstructor;

/**
 * Maps value from source object and sets it on the target.
 *
 * @author leonzio
 */
@AllArgsConstructor
final class ValueMapper<S, T, V>
{
  private final Function<S, V> converter;
  private final Setter<T, V> setter;

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
