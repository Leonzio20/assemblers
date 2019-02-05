package com.sdp.common.assemblers;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.google.common.collect.ImmutableSet;
import com.sdp.common.util.Getter;
import com.sdp.common.util.MethodNameResolver;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;

/**
 * @author leonzio
 */
@AllArgsConstructor
class AssembledFieldValidator<S>
{
  private final Class<S> sourceClass;
  private final Set<String> calledMethods = new HashSet<>();
  private final Set<String> ignoredMethods = new HashSet<>();

  <V> void resolveCalled(@NotNull Getter<S, V> getter)
  {
    String methodName = MethodNameResolver.resolveMethodName(getter);
    calledMethods.add(methodName);
  }

  void resolveIgnored(@NotNull Getter<S, ?> getter)
  {
    String methodName = MethodNameResolver.resolveMethodName(getter);
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
    String ignoredFields = String.join(", ", sourceFields);
    throw new IllegalArgumentException("Those fields should be assembled or ignored: " + ignoredFields);
  }
}
