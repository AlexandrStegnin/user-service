package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.configuration.MapStructConfig;
import com.ddkolesnik.userservice.enums.Gender;
import com.ddkolesnik.userservice.model.bitrix.address.BitrixAddress;
import com.ddkolesnik.userservice.model.bitrix.bp.Parameter;
import com.ddkolesnik.userservice.model.bitrix.contact.BitrixContact;
import com.ddkolesnik.userservice.model.bitrix.enums.TaxStatus;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.ddkolesnik.userservice.model.domain.AppRole;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.domain.UserProfile;
import com.ddkolesnik.userservice.model.dto.BankRequisitesDTO;
import com.ddkolesnik.userservice.model.dto.PassportDTO;
import com.ddkolesnik.userservice.model.dto.SnilsDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.repository.AppRoleRepository;
import com.ddkolesnik.userservice.utils.DateUtils;
import com.ddkolesnik.userservice.utils.ScanConverter;
import org.apache.commons.lang3.ObjectUtils;
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
  @Mapping(target = "birthdate", expression = "java(extractBirthdate(requisite, dto))")
  @Mapping(target = "placeOfBirth", expression = "java(extractPlaceOfBirth(requisite, dto))")
  public abstract void updateBirthdate(Requisite requisite, @MappingTarget UserDTO dto);

  @Mapping(target = "address", source = "address.address")
  public abstract void updateAddress(BitrixAddress address, @MappingTarget UserDTO dto);

  protected String extractBirthdate(Requisite requisite, UserDTO dto) {
    switch (dto.getTaxStatus()) {
      case INDIVIDUAL:
      case SELF_EMPLOYED:
        if (Objects.nonNull(requisite.getBirthdate())) {
          return DateUtils.convertToDDMMYYYY(requisite.getBirthdate().split("T")[0]);
        }
        break;
      case BUSINESSMAN:
        if (Objects.nonNull(requisite.getBusinessmanBirthdate())) {
          return DateUtils.convertToDDMMYYYY(requisite.getBusinessmanBirthdate().split("T")[0]);
        }
        break;
      case LEGAL_ENTITY:
        if (Objects.nonNull(requisite.getLegalEntityBirthdate())) {
          return DateUtils.convert(requisite.getLegalEntityBirthdate().split("T")[0]);
        }
    }
    return null;
  }

  protected String extractPlaceOfBirth(Requisite requisite, UserDTO dto) {
    switch (dto.getTaxStatus()) {
      case INDIVIDUAL:
      case SELF_EMPLOYED:
        if (Objects.nonNull(requisite.getPlaceOfBirth())) {
          return requisite.getPlaceOfBirth();
        }
        break;
      case BUSINESSMAN:
        if (Objects.nonNull(requisite.getBusinessmanPlaceOfBirth())) {
          return requisite.getBusinessmanPlaceOfBirth();
        }
        break;
      case LEGAL_ENTITY:
        if (Objects.nonNull(requisite.getLegalEntityPlaceOfBirth())) {
          return requisite.getLegalEntityPlaceOfBirth();
        }
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

  public Parameter getUpdatedFields(UserDTO dto, UserDTO dbUser) {
    var builder = new StringBuilder();
    if (ObjectUtils.allNotNull(dto.getInn(), dbUser.getInn()) && !dto.getInn().equalsIgnoreCase(dbUser.getInn())) {
      builder.append("ИНН ");
    }
    getSnilsUpdatedFields(builder, dto.getSnils(), dbUser.getSnils());
    getPassportUpdatedFields(builder, dto.getPassport(), dbUser.getPassport());
    getBankRequisiteUpdatedFields(builder, dto.getBankRequisites(), dbUser.getBankRequisites());
    if (ObjectUtils.allNotNull(dto.getAddress(), dbUser.getAddress()) && !dto.getAddress().equalsIgnoreCase(dbUser.getAddress())) {
      builder.append("Адрес ");
    }
    if (ObjectUtils.allNotNull(dto.getBirthdate(), dbUser.getBirthdate()) && !dto.getBirthdate().equalsIgnoreCase(dbUser.getBirthdate())) {
      builder.append("Дата рождения ");
    }
    if (ObjectUtils.allNotNull(dto.getTaxStatus(), dbUser.getTaxStatus()) && dto.getTaxStatus() != dbUser.getTaxStatus()) {
      builder.append("Налоговый статус ");
    }
    if (ObjectUtils.allNotNull(dto.getPlaceOfBirth(), dbUser.getPlaceOfBirth()) && !dto.getPlaceOfBirth().equalsIgnoreCase(dbUser.getPlaceOfBirth())) {
      builder.append("Место рождения ");
    }

    return Parameter.builder()
        .param(builder.toString())
        .build();
  }

  private void getSnilsUpdatedFields(StringBuilder builder, SnilsDTO dto, SnilsDTO dbSnils) {
    if (ObjectUtils.allNotNull(dto.getNumber(), dbSnils.getNumber()) && !dto.getNumber().equalsIgnoreCase(dbSnils.getNumber())) {
      builder.append("Номер СНИЛС ");
    }
    if (ScanConverter.isScansAvailable(dto)) {
      builder.append("Сканы СНИЛС ");
    }
  }

  private void getPassportUpdatedFields(StringBuilder builder, PassportDTO dto, PassportDTO dbPassport) {
    if (ObjectUtils.allNotNull(dto.getSerial(), dbPassport.getSerial()) && !dto.getSerial().equalsIgnoreCase(dbPassport.getSerial())) {
      builder.append("Серия паспорта ");
    }
    if (ObjectUtils.allNotNull(dto.getNumber(), dbPassport.getNumber()) && !dto.getNumber().equalsIgnoreCase(dbPassport.getNumber())) {
      builder.append("Номер паспорта ");
    }
    if (ObjectUtils.allNotNull(dto.getDepartmentCode(), dbPassport.getDepartmentCode()) && !dto.getDepartmentCode().equalsIgnoreCase(dbPassport.getDepartmentCode())) {
      builder.append("Код подразделения ");
    }
    if (ObjectUtils.allNotNull(dto.getIssuedBy(), dbPassport.getIssuedBy()) && !dto.getIssuedBy().equalsIgnoreCase(dbPassport.getIssuedBy())) {
      builder.append("Кем выдан ");
    }
    if (ScanConverter.isScansAvailable(dto)) {
      builder.append("Сканы паспорта ");
    }
    if (ObjectUtils.allNotNull(dto.getIssuedAt(), dbPassport.getIssuedAt()) && !dto.getIssuedAt().equalsIgnoreCase(dbPassport.getIssuedAt())) {
      builder.append("Дата выдачи ");
    }
  }

  private void getBankRequisiteUpdatedFields(StringBuilder builder, BankRequisitesDTO dto, BankRequisitesDTO dbBankRequisites) {
    if (ObjectUtils.anyNull(dto, dbBankRequisites)) {
      return;
    }
    if (ObjectUtils.allNotNull(dto.getBik(), dbBankRequisites.getBik()) && !dto.getBik().equalsIgnoreCase(dbBankRequisites.getBik())) {
      builder.append("БИК банка ");
    }
    if (ObjectUtils.allNotNull(dto.getCorrespondentAccountNumber(), dbBankRequisites.getCorrespondentAccountNumber()) &&
        !dto.getCorrespondentAccountNumber().equalsIgnoreCase(dbBankRequisites.getCorrespondentAccountNumber())) {
      builder.append("Корр. счёт ");
    }
    if (ObjectUtils.allNotNull(dto.getAccountNumber(), dbBankRequisites.getAccountNumber()) && !dto.getAccountNumber().equalsIgnoreCase(dbBankRequisites.getAccountNumber())) {
      builder.append("Номер счёта ");
    }
    if (ScanConverter.isScansAvailable(dto)) {
      builder.append("Сканы банковских реквизитов ");
    }
  }

}
