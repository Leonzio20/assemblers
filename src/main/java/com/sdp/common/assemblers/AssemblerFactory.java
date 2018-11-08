package com.sdp.common.assemblers;

import javax.validation.constraints.NotNull;

/**
 * Factory for creating assembler instance.
 *
 * @param <S>
 *   source object type
 * @param <T>
 *   target object type
 * @author leonzio
 */
public abstract class AssemblerFactory<S, T> implements Assembler<S, T>
{
  private Assembler<S, T> assembler;

  @Override
  public final T assemble(S source)
  {
    Assembler<S, T> assembler = getAssemblerInstance();
    return assembler.assemble(source);
  }

  private @NotNull Assembler<S, T> getAssemblerInstance()
  {
    if (assembler == null)
    {
      assembler = createAssemblerFactory();
    }
    return assembler;
  }

  /**
   * Creates configured assembler instance.
   *
   * @return assembler
   */
  protected abstract @NotNull Assembler<S, T> createAssemblerFactory();
}
