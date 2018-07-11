package com.sdp.common.assemblers;

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
  private final Class<S> sourceClass;
  private final Class<T> targetClass;
  private final Set<ValueMapper<S, T, ?>> mappers = new HashSet<>();

  StandardAssemblerBuilderImpl(Class<S> sourceClass, Class<T> targetClass)
  {
    this.sourceClass = sourceClass;
    this.targetClass = targetClass;
  }

  @Override
  public <V> AssemblerBuilder<S, T, V> from(Getter<S, V> getter)
  {
    return AssemblerBuilderImpl.of(this, getter);
  }

  @Override
  public Assembler<S, T> build()
  {
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
      throw new RuntimeException(e);
    }
  }

  StandardAssemblerBuilder<S, T> withMapper(ValueMapper<S, T, ?> valueMapper)
  {
    mappers.add(valueMapper);
    return this;
  }
}
