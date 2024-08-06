package com.trenbologna.stronk.mapper.impl;

import com.trenbologna.stronk.domain.ExerciseEntity;
import com.trenbologna.stronk.domain.WorkoutEntity;
import com.trenbologna.stronk.domain.dto.ExerciseDTO;
import com.trenbologna.stronk.domain.dto.WorkoutDTO;
import com.trenbologna.stronk.mapper.Mapper;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class WorkoutMapperImpl implements Mapper<WorkoutEntity, WorkoutDTO> {
    private ModelMapper modelMapper;
    public WorkoutMapperImpl(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
    }
    @Override
    public WorkoutDTO mapTo(WorkoutEntity workoutEntity) {
        return modelMapper.map(workoutEntity, WorkoutDTO.class);
    }

    @Override
    public WorkoutEntity mapFrom(WorkoutDTO workoutDTO) {
        return modelMapper.map(workoutDTO, WorkoutEntity.class);
    }

    @Override
    public List<WorkoutDTO> mapTo(List<WorkoutEntity> workoutEntityList) {
        return modelMapper.map(workoutEntityList, new TypeToken<List<WorkoutDTO>>(){}.getType());
    }

    @Override
    public List<WorkoutEntity> mapFrom(List<WorkoutDTO> workoutDTOList) {
        return modelMapper.map(workoutDTOList, new TypeToken<List<WorkoutEntity>>(){}.getType());
    }
}
