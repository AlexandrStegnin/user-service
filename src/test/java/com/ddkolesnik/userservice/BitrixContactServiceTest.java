package com.ddkolesnik.userservice;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import com.ddkolesnik.userservice.model.bitrix.Contact;
import com.ddkolesnik.userservice.model.bitrix.ContactList;
import com.ddkolesnik.userservice.model.bitrix.DuplicateResult;
import com.ddkolesnik.userservice.model.bitrix.UpdateContact;
import com.ddkolesnik.userservice.service.BitrixContactService;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.Map;
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
    DuplicateResult duplicateResult = bitrixContactService.findDuplicatesByPhones(Collections.singletonList("+79224777567"));
    assertFalse(((LinkedHashMap<?, ?>) duplicateResult.getResult()).isEmpty());
  }

  @Test
  public void getContactListByPhoneNumbersTest() {
    ContactList contactList = bitrixContactService.getContactByPhones(Collections.singletonList("+79224777567"));
    Contact earlyContact = contactList.getResult()
        .stream().min(Comparator.comparing(Contact::getId))
        .orElse(null);
    assertNotNull(earlyContact);
  }

  @Test
  public void updateContactTest() {
    Map<String, String> fields = new LinkedHashMap<>();
    fields.put("LAST_NAME", "Stegnin");
    fields.put("UF_CRM_1625221385", "1");
    UpdateContact contact = UpdateContact.builder()
        .id(2539)
        .fields(fields)
        .build();

    Object updated = bitrixContactService.updateContact(contact);
    assertNotNull(updated);
  }

}
