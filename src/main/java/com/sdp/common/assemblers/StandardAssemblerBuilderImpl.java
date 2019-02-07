package com.sdp.common.assemblers;

import java.util.Set;

import com.sdp.common.util.Getter;
import com.sdp.common.util.ObjectConstructor;

import com.google.common.collect.ImmutableSet;
import javax.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

/**
 * @author leonzio
 */
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Log4j2
final class StandardAssemblerBuilderImpl<S, T> implements StandardAssemblerBuilder<S, T>
{
  private final Class<T> targetClass;
  private final AssembledFieldValidator<S> assembledFieldValidator;
  private final Set<ValueMapper<S, T, ?>> mappers;

  StandardAssemblerBuilderImpl(@NotNull Class<S> sourceClass, @NotNull Class<T> targetClass)
  {
    this(targetClass, new AssembledFieldValidator<>(sourceClass), ImmutableSet.of());
    log.debug("Creating assembler from {} to {}.", sourceClass.getCanonicalName(), targetClass.getCanonicalName());
  }

  StandardAssemblerBuilderImpl(@NotNull StandardAssemblerBuilderImpl<S, T> source,
    @NotNull ValueMapper<S, T, ?> nextValueMapper)
  {
    this(source.targetClass, source.assembledFieldValidator, ImmutableSet.<ValueMapper<S, T, ?>>builder()
      .addAll(source.mappers)
      .add(nextValueMapper)
      .build());
  }

  @Override
  public @NotNull <V> AssemblerBuilder<S, T, V> from(@NotNull Getter<S, V> getter)
  {
    assembledFieldValidator.resolveCalled(getter);
    return AssemblerBuilderImpl.of(this, getter);
  }

  @Override
  public @NotNull StandardAssemblerBuilder<S, T> ignore(@NotNull Getter<S, ?> getter)
  {
    assembledFieldValidator.resolveIgnored(getter);
    return this;
  }

  @Override
  public @NotNull Assembler<S, T> build()
  {
    assembledFieldValidator.validate();
    return this::fillTargetObject;
  }

  private T fillTargetObject(S source)
  {
    T target = ObjectConstructor.construct(targetClass);
    mappers.forEach(valueMapper -> valueMapper.fill(source, target));
    return target;
  }
}
