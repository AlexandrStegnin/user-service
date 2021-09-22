package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.configuration.MapStructConfig;
import com.ddkolesnik.userservice.enums.Gender;
import com.ddkolesnik.userservice.model.bitrix.address.Address;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.AddressDTO;
import com.ddkolesnik.userservice.model.dto.PassportDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@Component
@Mapper(config = MapStructConfig.class)
public abstract class UserMapper {

  public abstract UserDTO toDTO(AppUser user);

  @Mapping(target = "bitrixId", source = "id")
  @Mapping(target = "email", expression = "java(extractEmail(contact))")
  @Mapping(target = "gender", expression = "java(extractGender(contact))")
  public abstract UserDTO toDTO(Contact contact);

  @Mapping(target = "passport", expression = "java(extractPassport(requisite))")
  public abstract void updatePassport(Requisite requisite, @MappingTarget UserDTO dto);

  @Mapping(target = "address", expression = "java(extractAddress(address))")
  public abstract void updateAddress(Address address, @MappingTarget UserDTO dto);

  protected String extractEmail(Contact contact) {
    return contact.getEmails().stream()
        .findAny()
        .map(Email::getValue)
        .orElse(null);
  }

  protected Gender extractGender(Contact contact) {
    if (Objects.nonNull(contact.getGender()) && !contact.getGender().isEmpty()) {
      return Gender.fromId(Integer.parseInt(contact.getGender()));
    }
    return null;
  }

  protected PassportDTO extractPassport(Requisite requisite) {
    return PassportDTO.builder()
        .serial(requisite.getSerial())
        .issuedBy(requisite.getIssuedBy())
        .departmentCode(requisite.getDepartmentCode())
        .number(requisite.getNumber())
        .issuedAt(convertToYYYYMMDD(requisite.getIssuedAt()))
        .build();
  }

  protected AddressDTO extractAddress(Address address) {
    return AddressDTO.builder()
        .city(address.getCity())
        .streetAndHouse(address.getAddress1())
        .office(address.getAddress2())
        .build();
  }

  private String convertToYYYYMMDD(String issuedAt) {
    return convertTo(issuedAt);
  }

  private String convertTo(String date) {
    if (Objects.isNull(date) || date.isBlank()) {
      return null;
    }
    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    return localDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
  }

}
