package com.ddkolesnik.userservice.controller.view;

import com.ddkolesnik.userservice.model.view.KindOnProject;
import com.ddkolesnik.userservice.service.view.KindOnProjectService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static com.ddkolesnik.userservice.configuration.Location.KIND_ON_PROJECT;

/**
 * @author Alexandr Stegnin
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class KindOnProjectController {

  KindOnProjectService kindOnProjectService;

  @PostMapping(path = KIND_ON_PROJECT)
  public List<KindOnProject> findByInvestorLogin(@RequestBody(required = false) String login) {
    return kindOnProjectService.findByInvestorLogin(login);
  }

}
