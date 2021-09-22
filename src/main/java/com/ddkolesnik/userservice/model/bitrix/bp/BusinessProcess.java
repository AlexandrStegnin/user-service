package com.ddkolesnik.userservice.model.bitrix.bp;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BusinessProcess {

  @JsonProperty("TEMPLATE_ID")
  String templateId;

  @Builder.Default
  @JsonProperty("DOCUMENT_ID")
  List<String> documentId = new ArrayList<>(Stream.of("crm", "CCrmDocumentContact").collect(Collectors.toList()));

  public void addDocumentId(String documentId) {
    this.documentId.add(documentId);
  }

}
