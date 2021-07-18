package com.ddkolesnik.userservice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import com.ddkolesnik.userservice.model.bitrix.address.Address;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateResult;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.dto.AddressDTO;
import com.ddkolesnik.userservice.model.dto.PassportDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.service.BitrixContactService;
import java.util.LinkedHashMap;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@SpringBootTest
public class BitrixContactServiceTest {

  @Autowired
  private BitrixContactService bitrixContactService;

  public DuplicateResult findDuplicatesTest() {
    return bitrixContactService.findDuplicates(getTestUserDTO(null));
  }

  public Contact getContactListByPhoneNumbersTest() {
    return bitrixContactService.findFirstContact(getTestUserDTO(null));
  }

  public Object updateContactTest(Integer id) {
    return bitrixContactService.updateContact(getTestUserDTO(id));
  }

  public Object createContactTest() {
    return bitrixContactService.createContact(getTestUserDTO(null));
  }

  public Object deleteContactTest(Integer id) {
    return bitrixContactService.deleteContact(getTestUserDTO(id));
  }

  @Test
  public void findRequisiteTest() {
    Requisite requisite = bitrixContactService.findRequisite(getTestUserDTO(777555));
    assertNull(requisite);
  }

  @Test
  public void updateRequisiteTest() {
    UserDTO dto = getTestRequisiteUserDTO(2735);
    Requisite requisite = bitrixContactService.findRequisite(dto);
    assertNotNull(requisite);
    Object update = bitrixContactService.updateRequisite(requisite, dto);
    assertNotNull(update);
  }

  @Test
  public void createRequisiteTest() {
    UserDTO dto = getTestRequisiteUserDTO(2735);
    Requisite requisite = bitrixContactService.findRequisite(dto);
    assertNull(requisite);
    Object create = bitrixContactService.createRequisite(dto);
    assertNotNull(create);
  }

  @Test
  public void findAddressTest() {
    UserDTO dto = getTestRequisiteUserDTO(2735);
    Address address = bitrixContactService.findAddress(dto);
    assertNull(address);
  }

  @Test
  public void createAddressTest() {
    UserDTO dto = getTestRequisiteUserDTO(2735);
    dto.setAddress(getAddressDTO());
    Address address = bitrixContactService.findAddress(dto);
    assertNull(address);
    Object create = bitrixContactService.createAddress(dto);
    assertNotNull(create);
  }

  @Test
  public void updateAddressTest() {
    UserDTO dto = getTestRequisiteUserDTO(2735);
    dto.setAddress(getAddressDTO());
    dto.getAddress().setBuilding("777");
    Address address = bitrixContactService.findAddress(dto);
    assertNotNull(address);
    Object update = bitrixContactService.updateAddress(dto);
    assertNotNull(update);
  }

  @Test
  public void crudTest() {
    Object created = createContactTest();
    assertNotNull(created);
    Integer id = (Integer) (((LinkedHashMap<?, ?>) created).get("result"));
    DuplicateResult duplicate = findDuplicatesTest();
    assertFalse(((LinkedHashMap<?, ?>) duplicate.getResult()).isEmpty());
    Contact earlyContact = getContactListByPhoneNumbersTest();
    assertNotNull(earlyContact);
    Object updated = updateContactTest(id);
    assertNotNull(updated);
    Object deleted = deleteContactTest(id);
    assertNotNull(deleted);
  }

  private UserDTO getTestUserDTO(Integer id) {
    return UserDTO.builder()
        .id(id)
        .name("Тест")
        .secondName("Тестович")
        .lastName("Тестовый")
        .email("myemail@example.com")
        .phone("+79998887766")
        .build();
  }

  private UserDTO getTestRequisiteUserDTO(Integer id) {
    return UserDTO.builder()
        .id(id)
        .name("Эпл")
        .secondName("Макинтошевич")
        .lastName("Яблоков")
        .email("a@a.ru")
        .phone("+79876543210")
        .inn("89095")
        .passport(getPassportDTO())
        .build();
  }

  private PassportDTO getPassportDTO() {
    return PassportDTO.builder()
        .serial("9999")
        .number("888888")
        .departmentCode("720-021")
        .issuedBy("УФМС России")
        .build();
  }

  private AddressDTO getAddressDTO() {
    return AddressDTO.builder()
        .city("Тюмень")
        .street("Республики")
        .house("1")
        .building("111")
        .build();
  }

}
