package com.sdp.common.assemblers;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.function.Function;

/**
 * @param <S>
 * @param <T>
 * @param <V>
 * @author leonzio
 */
@AllArgsConstructor
final class AssemblerBuilderImpl<S, T, V> implements AssemblerBuilder<S, T, V>
{
  private final @NotNull StandardAssemblerBuilderImpl<S, T> builder;
  private final @NotNull Function<S, V> converter;

  static <S, T, V> AssemblerBuilderImpl<S, T, V> of(@NotNull StandardAssemblerBuilderImpl<S, T> builder,
    @NotNull Getter<S, V> getter)
  {
    Function<S, V> converter = getter::get;
    return new AssemblerBuilderImpl<>(builder, converter);
  }

  @Override
  public <X> AssemblerBuilder<S, T, X> mapWith(@NotNull Function<V, X> mapper)
  {
    Function<S, X> converter = this.converter.andThen(mapper);
    return new AssemblerBuilderImpl<>(builder, converter);
  }

  @Override
  public <X> AssemblerBuilder<S, T, X> assembleWith(@NotNull Assembler<V, X> assembler)
  {
    Function<S, X> converter = this.converter.andThen(assembler::assemble);
    return new AssemblerBuilderImpl<>(builder, converter);
  }

  @Override
  public StandardAssemblerBuilder<S, T> to(@NotNull Setter<T, V> setter)
  {
    ValueMapper<S, T, ?> valueMapper = new ValueMapper<>(converter, setter);
    //noinspection unchecked
    return new StandardAssemblerBuilderImpl(builder, valueMapper);
  }
}
