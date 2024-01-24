package com.senla.project.mapper;

import com.senla.project.dto.UserResponse;
import com.senla.project.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {

  UserResponse mapToUserResponse(User user);
}
