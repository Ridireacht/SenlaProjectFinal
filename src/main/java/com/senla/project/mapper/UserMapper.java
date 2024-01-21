package com.senla.project.mapper;

import com.senla.project.dto.UserResponse;
import com.senla.project.entities.User;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserMapper {

  UserMapper MAPPER = Mappers.getMapper(UserMapper.class);


  UserResponse mapToUserResponse(User user);
}
