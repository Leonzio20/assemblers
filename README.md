# Assemblers
Simple way to a assemble one object to another (eg. entity to dto).

Example usage:
```
  class AssemblerClass extends AssemblerFactory<Source, TargetDTO>
  {
    @Override
    protected Assembler<Source, TargetDTO> createAssemblerFactory()
    {
      return StandardAssemblerBuilder.create(Source.class, TargetDTO.class)
        .from(Source::getId)
          .to(TargetDTO::setId)
        .from(Source::getSourceInner)
          .assembleWith(otherAssembler)
          .to(TargetDTO::setTargetInner)
        .build();
    }
  }
```
