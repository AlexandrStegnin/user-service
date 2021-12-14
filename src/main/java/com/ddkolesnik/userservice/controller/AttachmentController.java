package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.domain.Attachment;
import com.ddkolesnik.userservice.model.dto.AttachmentDTO;
import com.ddkolesnik.userservice.service.AttachmentService;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AttachmentController {

  AttachmentService annexService;
  
  @PostMapping(path = "/have-unread", produces = MediaType.APPLICATION_JSON_VALUE)
  public boolean haveUnread() {
    return annexService.haveUnread(SecurityUtils.getCurrentUserPhone());
  }

  @PostMapping(value = "/get-attachments", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Attachment> getAnnexes() {
    return annexService.findByLogin(SecurityUtils.getCurrentUserPhone());
  }

  @PostMapping(value = "/mark-read", produces = MediaType.APPLICATION_JSON_VALUE)
  public String saveAnnexRead(@RequestBody AttachmentDTO dto) {
    annexService.markRead(dto.getId());
    return "success";
  }
}
