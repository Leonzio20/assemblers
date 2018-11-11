package com.sdp.common.assemblers;

import com.sdp.common.util.Getter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
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
  private final MethodResolver<S> methodResolver;

  private final Set<ValueMapper<S, T, ?>> mappers = new HashSet<>();

  StandardAssemblerBuilderImpl(@NotNull Class<S> sourceClass, @NotNull Class<T> targetClass)
  {
    LOGGER.debug("Creating assembler from {} to {}.", sourceClass.getCanonicalName(), targetClass.getCanonicalName());
    this.targetClass = targetClass;
    methodResolver = new MethodResolver<>(sourceClass);
  }

  StandardAssemblerBuilderImpl(@NotNull StandardAssemblerBuilderImpl<S, T> source,
    @NotNull ValueMapper<S, T, ?> nextValueMapper)
  {
    targetClass = source.targetClass;
    methodResolver = source.methodResolver;
    mappers.addAll(source.mappers);
    mappers.add(nextValueMapper);
  }

  @Override
  public @NotNull <V> AssemblerBuilder<S, T, V> from(@NotNull Getter<S, V> getter)
  {
    methodResolver.resolveCalled(getter);
    return AssemblerBuilderImpl.of(this, getter);
  }

  @Override
  public @NotNull StandardAssemblerBuilder<S, T> ignore(@NotNull Getter<S, ?> getter)
  {
    methodResolver.resolveIgnored(getter);
    return this;
  }

  @Override
  public @NotNull Assembler<S, T> build()
  {
    methodResolver.validate();
    return this::fillTargetObject;
  }

  private T fillTargetObject(S source)
  {
    T target = createTargetObject();
    mappers.forEach(valueMapper -> valueMapper.fill(source, target));
    return target;
  }

  private T createTargetObject()
  {
    try
    {
      Constructor<T> declaredConstructor = targetClass.getDeclaredConstructor();
      declaredConstructor.setAccessible(true);
      return declaredConstructor.newInstance();
    }
    catch (InstantiationException | IllegalAccessException | NoSuchMethodException | InvocationTargetException e)
    {
      throw new IllegalStateException("Cannot create target object of " + targetClass, e);
    }
  }
}
