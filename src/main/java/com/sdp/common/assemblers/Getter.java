package com.sdp.common.assemblers;

/**
 * Interface for passing getter by method reference.
 *
 * @param <S>
 *   source object type
 * @param <V>
 *   returned value type
 * @author leonzio
 */
@FunctionalInterface
public interface Getter<S, V>
{
  /**
   * Gets value from given source.
   *
   * @param source
   *   to get value from
   * @return value from source
   */
  V get(S source);
}
