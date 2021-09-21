package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.configuration.MapStructConfig;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * @author Alexandr Stegnin
 */
@Component
@Mapper(config = MapStructConfig.class)
public abstract class UserMapper {

  @Mapping(target = "name", ignore = true)
  @Mapping(target = "secondName", ignore = true)
  @Mapping(target = "lastName", ignore = true)
  @Mapping(target = "email", ignore = true)
  @Mapping(target = "confirmCode", ignore = true)
  @Mapping(target = "inn", ignore = true)
  @Mapping(target = "snils", ignore = true)
  @Mapping(target = "passport", ignore = true)
  @Mapping(target = "address", ignore = true)
  @Mapping(target = "gender", ignore = true)
  @Mapping(target = "birthdate", ignore = true)
  @Mapping(target = "individual", ignore = true)
  @Mapping(target = "selfEmployed", ignore = true)
  @Mapping(target = "placeOfBirth", ignore = true)
  public abstract UserDTO toDTO(AppUser user);

}
