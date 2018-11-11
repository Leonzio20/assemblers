package com.sdp.common.assemblers;

import com.sdp.common.util.Getter;
import com.sdp.common.util.ObjectConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.NotNull;
import java.util.HashSet;
import java.util.Set;

/**
 * @param <S>
 *   Source object type
 * @param <T>
 *   Target object type
 * @author leonzio
 */
final class StandardAssemblerBuilderImpl<S, T> implements StandardAssemblerBuilder<S, T>
{
  private static final Logger LOGGER = LogManager.getLogger(StandardAssemblerBuilder.class);

  private final Class<T> targetClass;
  private final AssembledFieldValidator<S> assembledFieldValidator;

  private final Set<ValueMapper<S, T, ?>> mappers = new HashSet<>();

  StandardAssemblerBuilderImpl(@NotNull Class<S> sourceClass, @NotNull Class<T> targetClass)
  {
    LOGGER.debug("Creating assembler from {} to {}.", sourceClass.getCanonicalName(), targetClass.getCanonicalName());
    this.targetClass = targetClass;
    assembledFieldValidator = new AssembledFieldValidator<>(sourceClass);
  }

  StandardAssemblerBuilderImpl(@NotNull StandardAssemblerBuilderImpl<S, T> source,
    @NotNull ValueMapper<S, T, ?> nextValueMapper)
  {
    targetClass = source.targetClass;
    assembledFieldValidator = source.assembledFieldValidator;
    mappers.addAll(source.mappers);
    mappers.add(nextValueMapper);
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
