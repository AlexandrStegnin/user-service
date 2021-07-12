package com.ddkolesnik.userservice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ddkolesnik.userservice.model.UserDTO;
import com.ddkolesnik.userservice.model.bitrix.Contact;
import com.ddkolesnik.userservice.model.bitrix.DuplicateResult;
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
    return bitrixContactService.findContacts(getTestUserDTO(null));
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

}
