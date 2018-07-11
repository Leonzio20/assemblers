package com.sdp.common.assemblers;

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
  @Override
  public final T assemble(S source)
  {
    Assembler<S, T> assembler = createAssemblerFactory();
    return assembler.assemble(source);
  }

  /**
   * Creates configured assembler instance.
   *
   * @return assembler
   */
  protected abstract Assembler<S, T> createAssemblerFactory();
}
