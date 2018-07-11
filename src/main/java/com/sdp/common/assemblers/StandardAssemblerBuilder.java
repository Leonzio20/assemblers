package com.sdp.common.assemblers;

/**
 * Standard builder fo assembler.
 *
 * @param <S>
 *   source object type
 * @param <T>
 *   target object type
 * @author leonzio
 */
public interface StandardAssemblerBuilder<S, T>
{
  /**
   * Starts the conversion chain.
   *
   * @param getter
   *   to get value to set
   * @param <V>
   *   returned value type
   * @return extracted value
   */
  <V> AssemblerBuilder<S, T, V> from(Getter<S, V> getter);

  /**
   * Builds new assembler.
   *
   * @return new assembler with configured mappings
   */
  Assembler<S, T> build();

  /**
   * Creates new assembler builder instance.
   *
   * @param sourceClass
   *   to create builder with
   * @param targetClass
   *   to create builder with
   * @param <S>
   *   Source object type
   * @param <T>
   *   Target object type
   * @return builder instance
   */
  static <S, T> StandardAssemblerBuilder<S, T> create(Class<S> sourceClass, Class<T> targetClass)
  {
    return new StandardAssemblerBuilderImpl<>(sourceClass, targetClass);
  }
}
