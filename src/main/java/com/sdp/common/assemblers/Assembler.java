package com.sdp.common.assemblers;

/**
 * Object assembler interface.
 *
 * @param <S>
 *   source object type
 * @param <T>
 *   target object type
 *
 * @author leonzio
 */
public interface Assembler<S, T>
{
  /**
   * Assembles source object to target object.
   *
   * @param source
   *   to assembled from
   *
   * @return assembled object result
   */
  T assemble(S source);
}
