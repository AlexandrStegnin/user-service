package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.configuration.exception.AttachmentNotFoundException;
import com.ddkolesnik.userservice.configuration.exception.UserNotFoundException;
import com.ddkolesnik.userservice.configuration.property.NextcloudProperty;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.domain.Attachment;
import com.ddkolesnik.userservice.model.dto.AttachmentDTO;
import com.ddkolesnik.userservice.service.AppUserService;
import com.ddkolesnik.userservice.service.AttachmentService;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.aarboard.nextcloud.api.NextcloudConnector;
import org.springframework.http.MediaType;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Slf4j
public class AttachmentController {

  AttachmentService attachmentService;
  AppUserService userService;
  NextcloudConnector connector;
  NextcloudProperty nextcloudProperty;
  
  @PostMapping(path = "/have-unread", produces = MediaType.APPLICATION_JSON_VALUE)
  public boolean haveUnread() {
    return attachmentService.haveUnread(SecurityUtils.getCurrentUserPhone());
  }

  @PostMapping(value = "/get-attachments", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<Attachment> getAnnexes() {
    return attachmentService.findByLogin(SecurityUtils.getCurrentUserPhone());
  }

  @PostMapping(value = "/mark-read", produces = MediaType.APPLICATION_JSON_VALUE)
  public String saveAnnexRead(@RequestBody AttachmentDTO dto) {
    attachmentService.markRead(dto.getId());
    return "success";
  }

  @RequestMapping("/attachments/{attachmentId}")
  public void getFile(HttpServletResponse response, @PathVariable BigInteger attachmentId) throws IOException {
    AppUser currentUser = userService.findByPhone(SecurityUtils.getCurrentUserPhone());
    if (Objects.isNull(currentUser)) {
      throw new UserNotFoundException("Пользователь не найден");
    }
    Attachment attachment = attachmentService.findById(attachmentId);
    if (Objects.isNull(attachment)) {
      throw new AttachmentNotFoundException("Приложение не найдено");
    }
    if (!attachment.getUserId().equals(currentUser.getId())) {
      throw new SecurityException("Доступ к файлу запрещён");
    }
    Path path = Files.createTempFile("temp-", ".pdf");

    var transferred = connector.downloadFile(getPath(attachment), path.getParent().toAbsolutePath() + File.separator);
    if (!transferred) {
      log.error("Error get file {}", attachment.getAnnex().getAnnexName());
    }
    File file = Path.of(path.getParent() + File.separator + attachment.getAnnex().getAnnexName()).toFile();
    try (FileInputStream inputStream = new FileInputStream(file)) {
      response.setContentType(MediaType.APPLICATION_PDF_VALUE);
      response.setContentLength((int) file.length());
      response.setHeader("Content-Disposition", "inline;filename=\"" + file.getName() + "\"");
      FileCopyUtils.copy(inputStream, response.getOutputStream());
    }
    var deleted = Files.deleteIfExists(file.toPath());
    if (!deleted) {
      log.debug("Can't delete file {}", file.getName());
    }
    deleted = Files.deleteIfExists(path);
    if (!deleted) {
      log.debug("Can't delete file {}", path.toFile().getName());
    }
  }

  private String getPath(Attachment attachment) {
    return File.separator + nextcloudProperty.getFolder() + File.separator + attachment.getAnnex().getAnnexName();
  }

}
