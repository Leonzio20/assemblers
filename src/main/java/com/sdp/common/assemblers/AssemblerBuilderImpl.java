package com.sdp.common.assemblers;

import lombok.AllArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.Objects;
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

  static <S, T, V> @NotNull AssemblerBuilderImpl<S, T, V> of(@NotNull StandardAssemblerBuilderImpl<S, T> builder,
    @NotNull Getter<S, V> getter)
  {
    Function<S, V> newConverter = getter::get;
    return new AssemblerBuilderImpl<>(builder, newConverter);
  }

  @Override
  public <X> @NotNull AssemblerBuilder<S, T, X> mapWith(@NotNull Function<V, X> mapper)
  {
    Function<S, X> newConverter = converter.andThen(mapper);
    return new AssemblerBuilderImpl<>(builder, newConverter);
  }

  @Override
  public <X> @NotNull AssemblerBuilder<S, T, X> assembleWith(@NotNull Assembler<V, X> assembler)
  {
    Function<S, X> newConverter = converter.andThen(assembler::assemble);
    return new AssemblerBuilderImpl<>(builder, newConverter);
  }

  @Override
  public @NotNull AssemblerBuilder<S, T, V> nullSafe()
  {
    Function<S, V> newConverter = new Function<S, V>()
    {
      @Override
      public V apply(S source)
      {
        return converter.apply(source);
      }

      @Override
      public <X> Function<S, X> andThen(Function<? super V, ? extends X> after)
      {
        Objects.requireNonNull(after);
        return (S s) -> {
          V v = apply(s);
          return v == null ? null : after.apply(v);
        };
      }
    };
    return new AssemblerBuilderImpl<>(builder, newConverter);
  }

  @Override
  public @NotNull StandardAssemblerBuilder<S, T> to(@NotNull Setter<T, V> setter)
  {
    ValueMapper<S, T, ?> valueMapper = new ValueMapper<>(converter, setter);
    //noinspection unchecked
    return new StandardAssemblerBuilderImpl(builder, valueMapper);
  }
}
