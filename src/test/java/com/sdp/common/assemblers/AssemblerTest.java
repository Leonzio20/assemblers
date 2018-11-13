package com.sdp.common.assemblers;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.same;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import lombok.Getter;
import lombok.Setter;
import name.falgout.jeffrey.testing.junit.mockito.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;

/**
 * @author leonzio
 */
@ExtendWith(MockitoExtension.class)
class AssemblerTest
{
  @Mock
  private Assembler<SourceInner, TargetInnerDTO> sourceInnerTargetInnerDTOAssembler;

  @Test
  void testFullWithAssembleWithSourceInner()
  {
    Assembler<Source, TargetDTO> sourceTargetDTOAssembler = new AssemblerFactory<Source, TargetDTO>()
    {
      @Override
      protected Assembler<Source, TargetDTO> createAssemblerFactory()
      {
        return StandardAssemblerBuilder.create(Source.class, TargetDTO.class)
          .from(Source::getId)
          .to(TargetDTO::setId)
          .from(Source::getSourceInner)
          .assembleWith(sourceInnerTargetInnerDTOAssembler)
          .to(TargetDTO::setTargetInner)
          .build();
      }
    };

    Long id = 1L;

    SourceInner sourceInner = mock(SourceInner.class);
    TargetInnerDTO targetInnerDTO = mock(TargetInnerDTO.class);

    Source source = mock(Source.class);
    when(source.getId()).thenReturn(id);
    when(source.getSourceInner()).thenReturn(sourceInner);

    when(sourceInnerTargetInnerDTOAssembler.assemble(same(sourceInner))).thenReturn(targetInnerDTO);

    TargetDTO target = sourceTargetDTOAssembler.assemble(source);

    assertThat(target).isNotNull()
      .returns(id, TargetDTO::getId)
      .returns(targetInnerDTO, TargetDTO::getTargetInner);
  }

  @Test
  void testUsingMapWithSourceInnerName()
  {
    Assembler<Source, TargetSecondDTO> sourceTargetSecondDTOAssembler = new AssemblerFactory<Source, TargetSecondDTO>()
    {
      @Override
      protected Assembler<Source, TargetSecondDTO> createAssemblerFactory()
      {
        return StandardAssemblerBuilder.create(Source.class, TargetSecondDTO.class)
          .from(Source::getId)
          .to(TargetSecondDTO::setId)
          .from(Source::getSourceInner)
          .mapWith(SourceInner::getName)
          .to(TargetSecondDTO::setTargetInnerName)
          .build();
      }
    };

    Long id = 1L;
    String innerName = "name";

    SourceInner sourceInner = mock(SourceInner.class);
    when(sourceInner.getName()).thenReturn(innerName);

    Source source = mock(Source.class);
    when(source.getSourceInner()).thenReturn(sourceInner);
    when(source.getId()).thenReturn(id);

    TargetSecondDTO target = sourceTargetSecondDTOAssembler.assemble(source);

    assertThat(target).isNotNull()
      .returns(id, TargetSecondDTO::getId)
      .returns(innerName, TargetSecondDTO::getTargetInnerName);
  }

  @Test
  void testIgnoreSourceInner()
  {
    Assembler<Source, TargetDTO> sourceTargetDTOAssembler = new AssemblerFactory<Source, TargetDTO>()
    {
      @Override
      protected Assembler<Source, TargetDTO> createAssemblerFactory()
      {
        return StandardAssemblerBuilder.create(Source.class, TargetDTO.class)
          .from(Source::getId)
          .to(TargetDTO::setId)
          .ignore(Source::getSourceInner)
          .build();
      }
    };

    Long id = 1L;

    Source source = mock(Source.class);
    when(source.getId()).thenReturn(id);

    TargetDTO target = sourceTargetDTOAssembler.assemble(source);

    assertThat(target).isNotNull()
      .returns(id, TargetDTO::getId)
      .returns(null, TargetDTO::getTargetInner);
  }

  @Test
  void testNotHandlingAllFields()
  {
    Assembler<Source, TargetDTO> sourceTargetDTOAssembler = new AssemblerFactory<Source, TargetDTO>()
    {
      @Override
      protected Assembler<Source, TargetDTO> createAssemblerFactory()
      {
        return StandardAssemblerBuilder.create(Source.class, TargetDTO.class)
          .from(Source::getId)
          .to(TargetDTO::setId)
          .build();
      }
    };

    Long id = 1L;

    Source source = mock(Source.class);
    when(source.getId()).thenReturn(id);

    assertThatThrownBy(() -> sourceTargetDTOAssembler.assemble(source))
      .isInstanceOf(IllegalArgumentException.class)
      .hasMessage("Those fields should be assembled or ignored: sourceInner");
  }

  @Test
  void testUsingMapWithNullSafe()
  {
    Assembler<Source, TargetSecondDTO> sourceTargetSecondDTOAssembler = new AssemblerFactory<Source, TargetSecondDTO>()
    {
      @Override
      protected Assembler<Source, TargetSecondDTO> createAssemblerFactory()
      {
        return StandardAssemblerBuilder.create(Source.class, TargetSecondDTO.class)
          .from(Source::getId)
          .to(TargetSecondDTO::setId)
          .from(Source::getSourceInner)
          .nullSafe()
          .mapWith(SourceInner::getName)
          .to(TargetSecondDTO::setTargetInnerName)
          .build();
      }
    };

    Long id = 1L;

    Source source = mock(Source.class);
    when(source.getSourceInner()).thenReturn(null);
    when(source.getId()).thenReturn(id);

    TargetSecondDTO target = sourceTargetSecondDTOAssembler.assemble(source);

    assertThat(target).isNotNull()
      .returns(id, TargetSecondDTO::getId)
      .returns(null, TargetSecondDTO::getTargetInnerName);
  }

  @Setter
  @Getter
  private static class Source
  {
    private Long id;
    private SourceInner sourceInner;
  }

  @Setter
  @Getter
  private static class SourceInner
  {
    private String name;
  }

  @Setter
  @Getter
  private static class TargetDTO
  {
    private Long id;
    private TargetInnerDTO targetInner;
  }

  @Setter
  @Getter
  private static class TargetInnerDTO
  {
    private String name;
  }

  @Setter
  @Getter
  private static class TargetSecondDTO
  {
    private Long id;
    private String targetInnerName;
  }
}