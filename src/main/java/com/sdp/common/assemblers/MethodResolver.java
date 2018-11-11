package com.sdp.common.assemblers;

import com.google.common.collect.ImmutableSet;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @param <S>
 *   source class type
 * @author leonzio
 */
class MethodResolver<S>
{
  private final Class<S> sourceClass;
  private final Set<String> calledMethods = new HashSet<>();
  private final Set<String> ignoredMethods = new HashSet<>();

  MethodResolver(@NotNull Class<S> sourceClass)
  {
    this.sourceClass = sourceClass;
  }

  <V> void resolveCalled(@NotNull Getter<S, V> getter)
  {
    String methodName = resolveMethodName(getter);
    calledMethods.add(methodName);
  }

  void resolveIgnored(@NotNull Getter<S, ?> getter)
  {
    String methodName = resolveMethodName(getter);
    ignoredMethods.add(methodName);
  }

  void validate()
  {
    Set<String> sourceFields = Stream.of(sourceClass.getDeclaredFields())
      .filter(field -> !field.isSynthetic())
      .map(Field::getName)
      .collect(Collectors.toSet());
    Set<String> usedFields = new HashSet<>();
    for (String field : sourceFields)
    {
      boolean anyMatch = ImmutableSet.<String>builder()
        .addAll(calledMethods)
        .addAll(ignoredMethods)
        .build()
        .stream()
        .map(String::toLowerCase)
        .anyMatch(m -> m.endsWith(field.toLowerCase()));
      if (anyMatch)
      {
        usedFields.add(field);
      }
    }
    sourceFields.removeAll(usedFields);
    if (sourceFields.isEmpty())
    {
      return;
    }
    throw new IllegalArgumentException("Those fields should be assembled or ignored: " + sourceFields.stream()
      .collect(Collectors.joining(", ")));
  }

  private <V, G extends Getter<S, V> & Serializable> String resolveMethodName(G getter)
  {
    try
    {
      Class<?> getterClass = getter.getClass();
      Method writeReplace = getterClass.getDeclaredMethod("writeReplace");
      writeReplace.setAccessible(true);
      SerializedLambda serializedLambda = (SerializedLambda) writeReplace.invoke(getter);
      return serializedLambda.getImplMethodName();
    }
    catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e)
    {
      throw new RuntimeException("Cannot resolve method name", e);
    }
  }
}
