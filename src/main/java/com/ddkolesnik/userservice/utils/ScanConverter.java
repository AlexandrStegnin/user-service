package com.ddkolesnik.userservice.utils;

import com.ddkolesnik.userservice.model.Scanned;
import com.ddkolesnik.userservice.model.bitrix.file.FileData;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@UtilityClass
public class ScanConverter {

  public List<FileData> convertScans(Scanned dto) {
    MultipartFile[] files = dto.getScans();
    var fileDataList = new ArrayList<FileData>();
    for (MultipartFile file : files) {
      try {
        byte[] content = file.getBytes();
        if (content.length > 0) {
          var imageString = Base64Utils.encodeToString(content);
          var fileData = new FileData();
          fileData.setFiles(new String[]{file.getOriginalFilename(), imageString});
          fileDataList.add(fileData);
        }
      } catch (IOException e) {
        log.error("Произошла ошибка {}", e.getMessage());
      }
    }
    return fileDataList;
  }

  public boolean isScansAvailable(Scanned scanned) {
    if (ObjectUtils.anyNull(scanned, scanned.getScans())) {
      return false;
    }
    return !convertScans(scanned).isEmpty();
  }

}
