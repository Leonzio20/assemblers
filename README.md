# Assemblers library
Simple way to a assemble one object to another (eg. entity to dto).
Provides functionality that checks if all of source fields are served in some way, prescribed or ignored.

Example usage:
```
  class AssemblerClass extends AssemblerFactory<Source, TargetDTO> // created factory will be singleton
  {
    @Override
    protected Assembler<Source, TargetDTO> createAssemblerFactory()
    {
      return StandardAssemblerBuilder.create(Source.class, TargetDTO.class)
        .from(Source::getId) // source field
          .to(TargetDTO::setId) // target field
        .from(Source::getSourceInner)
          .nullSafe() // if we expect that previous method in chain can return null this breaks a call chain
          .assembleWith(otherAssembler) // or to other converting way mapWith(mapper)
          .to(TargetDTO::setTargetInner)
        .ignore(Source::getContent) // if field cannot be prescribed for some reason
        .build();
    }
  }
```

Free to use in other projects.
