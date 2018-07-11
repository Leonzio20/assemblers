package com.sdp.common.assemblers;

import java.util.function.Function;

/**
 * @param <S>
 * @param <T>
 * @param <V>
 * @author leonzio
 */
final class AssemblerBuilderImpl<S, T, V> implements AssemblerBuilder<S, T, V>
{
  private final StandardAssemblerBuilderImpl<S, T> builder;
  private final Function<S, V> converter;

  private AssemblerBuilderImpl(StandardAssemblerBuilderImpl<S, T> builder, Function<S, V> converter)
  {
    this.builder = builder;
    this.converter = converter;
  }

  static <S, T, V> AssemblerBuilderImpl<S, T, V> of(StandardAssemblerBuilderImpl<S, T> builder, Getter<S, V> getter)
  {
    Function<S, V> converter = getter::get;
    return new AssemblerBuilderImpl<>(builder, converter);
  }

  @Override
  public <X> AssemblerBuilder<S, T, X> mapWith(Function<V, X> mapper)
  {
    Function<S, X> converter = this.converter.andThen(mapper);
    return new AssemblerBuilderImpl<>(builder, converter);
  }

  @Override
  public <X> AssemblerBuilder<S, T, X> assembleWith(Assembler<V, X> assembler)
  {
    Function<S, X> converter = this.converter.andThen(assembler::assemble);
    return new AssemblerBuilderImpl<>(builder, converter);
  }

  @Override
  public StandardAssemblerBuilder<S, T> to(Setter<T, V> setter)
  {
    ValueMapper<S, T, ?> valueMapper = new ValueMapper<>(converter, setter);
    return builder.withMapper(valueMapper);
  }
}
