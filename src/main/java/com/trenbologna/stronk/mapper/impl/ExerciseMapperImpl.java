package com.trenbologna.stronk.mapper.impl;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.domain.dto.ExerciseDTO;
import com.trenbologna.stronk.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ExerciseMapperImpl implements Mapper<ExerciseEntity, ExerciseDTO> {
    private ModelMapper modelMapper;
    public ExerciseMapperImpl(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }
    @Override
    public ExerciseDTO mapTo(ExerciseEntity exerciseEntity) {
        return modelMapper.map(exerciseEntity, ExerciseDTO.class);
    }

    @Override
    public ExerciseEntity mapFrom(ExerciseDTO exerciseDTO) {
        return modelMapper.map(exerciseDTO, ExerciseEntity.class);
    }

    @Override
    public List<ExerciseDTO> mapTo(List<ExerciseEntity> exerciseEntityList) {
        return modelMapper.map(exerciseEntityList, new TypeToken<List<ExerciseDTO>>(){}.getType());
    }

    @Override
    public List<ExerciseEntity> mapFrom(List<ExerciseDTO> exerciseDTOList) {
        return modelMapper.map(exerciseDTOList, new TypeToken<List<ExerciseEntity>>(){}.getType());
    }
}
