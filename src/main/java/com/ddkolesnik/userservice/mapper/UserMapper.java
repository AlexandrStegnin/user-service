package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.configuration.MapStructConfig;
import com.ddkolesnik.userservice.enums.Gender;
import com.ddkolesnik.userservice.model.bitrix.address.BitrixAddress;
import com.ddkolesnik.userservice.model.bitrix.contact.BitrixContact;
import com.ddkolesnik.userservice.model.bitrix.enums.TaxStatus;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.ddkolesnik.userservice.model.domain.AppRole;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.domain.UserProfile;
import com.ddkolesnik.userservice.model.dto.PassportDTO;
import com.ddkolesnik.userservice.model.dto.SnilsDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.repository.AppRoleRepository;
import com.ddkolesnik.userservice.utils.DateUtils;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@Component
@Mapper(config = MapStructConfig.class)
public abstract class UserMapper {

  protected AppRoleRepository appRoleRepository;

  @Autowired
  public final void setAppRoleRepository(AppRoleRepository appRoleRepository) {
    this.appRoleRepository = appRoleRepository;
  }

  @Mapping(target = "login", source = "phone")
  @Mapping(target = "role", expression = "java(getInvestorRole())")
  public abstract AppUser toEntity(UserDTO dto);

  @Mapping(target = "profile", expression = "java(getProfile(dto, user))")
  public abstract void updateProfile(UserDTO dto, @MappingTarget AppUser user);

  public abstract UserDTO toDTO(AppUser user);

  @Mapping(target = "bitrixId", source = "id")
  @Mapping(target = "email", expression = "java(extractEmail(bitrixContact))")
  @Mapping(target = "gender", expression = "java(extractGender(bitrixContact))")
  @Mapping(target = "phone", expression = "java(extractPhone(bitrixContact))")
  @Mapping(target = "taxStatus", expression = "java(convertTaxStatus(bitrixContact))")
  @Mapping(target = "accredited", expression = "java(convertAccredited(bitrixContact))")
  public abstract UserDTO toDTO(BitrixContact bitrixContact);

  @Mapping(target = "snils", expression = "java(extractSnils(requisite))")
  @Mapping(target = "passport", expression = "java(extractPassport(requisite))")
  public abstract void updatePassport(Requisite requisite, @MappingTarget UserDTO dto);

  @Mapping(target = "snils", ignore = true)
  @Mapping(target = "birthdate", expression = "java(extractBirthdate(requisite))")
  public abstract void updateBirthdate(Requisite requisite, @MappingTarget UserDTO dto);

  @Mapping(target = "address", source = "address.address")
  public abstract void updateAddress(BitrixAddress address, @MappingTarget UserDTO dto);

  protected String extractBirthdate(Requisite requisite) {
    if (Objects.nonNull(requisite.getBirthdate())) {
      return DateUtils.convertToDDMMYYYY(requisite.getBirthdate().split("T")[0]);
    }
    return null;
  }

  protected AppRole getInvestorRole() {
    return appRoleRepository.findByName("ROLE_INVESTOR");
  }

  protected UserProfile getProfile(UserDTO dto, AppUser user) {
    UserProfile profile = UserProfile.builder()
        .email(dto.getEmail())
        .build();
    profile.setUser(user);
    return profile;
  }

  protected String extractEmail(BitrixContact bitrixContact) {
    return bitrixContact.getEmails().stream()
        .findAny()
        .map(Email::getValue)
        .orElse(null);
  }

  protected Gender extractGender(BitrixContact bitrixContact) {
    if (Objects.nonNull(bitrixContact.getGender()) && !bitrixContact.getGender().isEmpty()) {
      return Gender.fromId(Integer.parseInt(bitrixContact.getGender()));
    }
    return null;
  }

  protected PassportDTO extractPassport(Requisite requisite) {
    return PassportDTO.builder()
        .serial(requisite.getSerial())
        .issuedBy(requisite.getIssuedBy())
        .departmentCode(requisite.getDepartmentCode())
        .number(requisite.getNumber())
        .issuedAt(requisite.getIssuedAt())
        .build();
  }

  protected SnilsDTO extractSnils(Requisite requisite) {
    return SnilsDTO.builder()
        .number(requisite.getSnils())
        .build();
  }

  protected String extractPhone(BitrixContact bitrixContact) {
    return bitrixContact.getPhones().stream()
        .findAny()
        .map(Phone::getValue)
        .orElse(null);
  }

  protected TaxStatus convertTaxStatus(BitrixContact bitrixContact) {
    return TaxStatus.fromCode(bitrixContact.getTaxStatus());
  }

  protected boolean convertAccredited(BitrixContact bitrixContact) {
    return !Objects.isNull(bitrixContact.getContactAccredited()) && bitrixContact.getContactAccredited() != 0;
  }

}
