package com.sdp.common.assemblers;

import com.sdp.common.util.Setter;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

/**
 * Builder for standard assembler conversions.
 *
 * @param <S>
 *   source object type
 * @param <T>
 *   target object type
 * @param <V>
 *   type of value to convert from
 * @author leonzio
 */
public interface AssemblerBuilder<S, T, V>
{
  /**
   * Maps value using given mapper.
   *
   * @param mapper
   *   to map value
   * @param <X>
   *   type of value to convert to
   * @return this for chained build
   */
  <X> @NotNull AssemblerBuilder<S, T, X> mapWith(@NotNull Function<V, X> mapper);

  /**
   * Assembles value using other assembler.
   *
   * @param assembler
   *   to assemble the value
   * @param <X>
   *   type of {@link Assembler#assemble(Object)} result
   * @return this for chained build
   */
  <X> @NotNull AssemblerBuilder<S, T, X> assembleWith(@NotNull Assembler<V, X> assembler);

  /**
   * Null safety. Use if in previous step you expect to receive a null value.
   *
   * @return same builder with null safe converting method
   */
  @NotNull AssemblerBuilder<S, T, V> nullSafe();

  /**
   * Ends standard mappings.
   *
   * @param setter
   *   to sets mapped value
   * @return standard builder
   */
  @NotNull StandardAssemblerBuilder<S, T> to(@NotNull Setter<T, V> setter);
}
