package com.ddkolesnik.userservice.model;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author Alexandr Stegnin
 */
public interface Scanned {

  MultipartFile[] getScans();

}
