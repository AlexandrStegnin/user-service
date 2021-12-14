package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.domain.Attachment;
import com.ddkolesnik.userservice.repository.AttachmentRepository;
import com.ddkolesnik.userservice.repository.UserAnnexRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AttachmentService {

  final AttachmentRepository attachmentRepository;
  final UserAnnexRepository annexRepository;
  final AppUserService userService;

  public List<Attachment> findByUserAndAnnexName(Long userId, String annexName) {
    if (!annexName.endsWith(".pdf")) {
      annexName = annexName + ".pdf";
    }
    return attachmentRepository.findByUserIdAndAnnex_AnnexName(userId, annexName);
  }

  public List<Attachment> findAll() {
    return attachmentRepository.findAll();
  }

  public Attachment findById(BigInteger id) {
    return attachmentRepository.getById(id);
  }

  public List<Attachment> findByUserId(Long userId) {
    return attachmentRepository.findByUserIdOrderByDateReadDesc(userId);
  }

  public List<Attachment> findByLogin(String login) {
    if (Objects.isNull(login)) {
      throw new RuntimeException("Необходимо передать логин пользователя");
    }
    AppUser user = userService.findByPhone(login);
    if (Objects.isNull(user)) {
      throw new RuntimeException("Пользователь с логином = [" + login + "] не найден");
    }
    return findByUserId(user.getId());
  }

  public void deleteById(BigInteger id) {
    attachmentRepository.deleteById(id);
  }

  public void update(Attachment attachment) {
    attachmentRepository.save(attachment);
  }

  public void create(Attachment attachment) {
    attachmentRepository.save(attachment);
  }

  public boolean haveUnread(String login) {
    var user = userService.findByPhone(login);
    if (Objects.nonNull(user)) {
      return haveUnread(user.getId());
    }
    return false;
  }

  private boolean haveUnread(Long userId) {
    return annexRepository.existsByUserIdAndDateReadIsNull(userId);
  }

  public void markRead(BigInteger id) {
    Attachment attachment = findById(id);
    if (Objects.isNull(attachment)) {
      throw new EntityNotFoundException("Attachment not found");
    }
    attachment.setAnnexRead(1);
    attachment.setDateRead(new Date());
    update(attachment);
  }
}
