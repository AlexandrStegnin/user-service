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
import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@UtilityClass
public class ScanConverter {

  public List<FileData> convertScans(Scanned dto) {
    var fileDataList = new ArrayList<FileData>();
    if (ObjectUtils.anyNull(dto, dto.getScans())) {
      return fileDataList;
    }
    MultipartFile[] files = dto.getScans();
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
    if (Objects.isNull(scanned) || Objects.isNull(scanned.getScans())) {
      return false;
    }
    return !convertScans(scanned).isEmpty();
  }

}
