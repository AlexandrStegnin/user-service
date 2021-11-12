package com.ddkolesnik.userservice.service.view;

import com.ddkolesnik.userservice.model.view.KindOnProject;
import com.ddkolesnik.userservice.repository.view.KindOnProjectRepository;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class KindOnProjectService {

  KindOnProjectRepository kindOnProjectRepository;

  public List<KindOnProject> findByInvestorLogin(String login) {
    if (Objects.isNull(login)) {
      login = SecurityUtils.getCurrentUserPhone();
    }
    return kindOnProjectRepository.findByLoginOrderByBuyDate(login);
  }

}
