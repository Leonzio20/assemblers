package com.sdp.common.assemblers;

import com.sdp.common.util.Getter;

import javax.validation.constraints.NotNull;

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
   * @return sub builder for
   */
  <V> @NotNull AssemblerBuilder<S, T, V> from(@NotNull Getter<S, V> getter);

  /**
   * Excludes method from assembling.
   *
   * @param getter
   *   to exclude
   * @return this for chained build
   */
  @NotNull StandardAssemblerBuilder<S, T> ignore(@NotNull Getter<S, ?> getter);

  /**
   * Builds new assembler.
   *
   * @return new assembler with configured mappings
   */
  @NotNull Assembler<S, T> build();

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
  static <S, T> @NotNull StandardAssemblerBuilder<S, T> create(@NotNull Class<S> sourceClass,
    @NotNull Class<T> targetClass)
  {
    return new StandardAssemblerBuilderImpl<>(sourceClass, targetClass);
  }
}
