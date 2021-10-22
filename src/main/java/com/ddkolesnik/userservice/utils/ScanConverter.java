package com.ddkolesnik.userservice.utils;

import com.ddkolesnik.userservice.model.Scanned;
import com.ddkolesnik.userservice.model.bitrix.file.FileData;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
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

  public boolean isScansAvailable(Scanned scanned) {
    return Objects.nonNull(scanned)
        && Objects.nonNull(scanned.getScans())
        && (scanned.getScans().length > 0);
  }

  public List<FileData> convertScans(Scanned dto) {
    MultipartFile[] files = dto.getScans();
    var fileDataList = new ArrayList<FileData>();
    for (MultipartFile file : files) {
      try {
        byte[] content = file.getBytes();
        var imageString = Base64Utils.encodeToString(content);
        var fileData = new FileData();
        fileData.setFiles(new String[]{file.getOriginalFilename(), imageString});
        fileDataList.add(fileData);
      } catch (IOException e) {
        log.error("Произошла ошибка {}", e.getMessage());
      }
    }
    return fileDataList;
  }

}
