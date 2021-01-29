package com.sdp.common.assemblers;

import java.util.function.Function;

import com.sdp.common.util.Setter;

import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

/**
 * Maps value from source object and sets it on the target.
 *
 * @author leonzio
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE, staticName = "of")
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
  void fill(@NotNull S source, @NotNull T target)
  {
    V converted = converter.apply(source);
    setter.set(target, converted);
  }
}
