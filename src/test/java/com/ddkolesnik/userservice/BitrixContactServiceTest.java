package com.ddkolesnik.userservice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ddkolesnik.userservice.model.UserDTO;
import com.ddkolesnik.userservice.model.bitrix.Contact;
import com.ddkolesnik.userservice.model.bitrix.ContactList;
import com.ddkolesnik.userservice.model.bitrix.DuplicateResult;
import com.ddkolesnik.userservice.service.BitrixContactService;
import java.util.Comparator;
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

  @Test
  public void findDuplicatesTest() {
    DuplicateResult duplicateResult = bitrixContactService.findDuplicates(getUserDTO());
    assertFalse(((LinkedHashMap<?, ?>) duplicateResult.getResult()).isEmpty());
  }

  @Test
  public void getContactListByPhoneNumbersTest() {
    ContactList contactList = bitrixContactService.findContacts(getUserDTO());
    Contact earlyContact = contactList.getResult()
        .stream().min(Comparator.comparing(Contact::getId))
        .orElse(null);
    assertNotNull(earlyContact);
  }

  @Test
  public void updateContactTest() {
    Object updated = bitrixContactService.updateContact(getUserDTO());
    assertNotNull(updated);
  }

  private UserDTO getUserDTO() {
    return UserDTO.builder()
        .id(2539)
        .name("Александр")
        .secondName("Александрович")
        .lastName("Стегнин")
        .email("myemail@example.com")
        .phone("+79224777567")
        .build();
  }

}
