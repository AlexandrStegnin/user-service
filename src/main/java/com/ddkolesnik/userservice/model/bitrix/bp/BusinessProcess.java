package com.ddkolesnik.userservice.model.bitrix.bp;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Data;

/**
 * @author Alexandr Stegnin
 */
@Data
public class BusinessProcess {

  @JsonProperty("TEMPLATE_ID")
  String templateId = "574";

  @JsonProperty("DOCUMENT_ID")
  List<String> documentId = new ArrayList<>();

  public BusinessProcess() {
    this.documentId.addAll(Stream.of("crm", "CCrmDocumentContact").collect(Collectors.toList()));
  }

}
