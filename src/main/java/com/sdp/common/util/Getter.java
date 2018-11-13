package com.sdp.common.util;

import java.io.Serializable;

import javax.validation.constraints.NotNull;

/**
 * Interface for passing getter by method reference.
 *
 * @param <S>
 *   source object type
 * @param <V>
 *   returned value type
 *
 * @author leonzio
 */
@FunctionalInterface
public interface Getter<S, V> extends Serializable
{
  /**
   * Gets value from given source.
   *
   * @param source
   *   to get value from
   *
   * @return value from source
   */
  V get(@NotNull S source);
}
