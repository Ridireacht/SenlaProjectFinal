package com.senla.project.mapper;

import org.mapstruct.factory.Mappers;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserMapperTest {

  private final UserMapper adMapper = Mappers.getMapper(UserMapper.class);
}
