package com.ddkolesnik.userservice.model;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {

  @JsonProperty("NAME")
  private String name;

  @JsonProperty("SECOND_NAME")
  private String secondName;

  @JsonProperty("LAST_NAME")
  private String lastName;

  @JsonProperty("EMAIL")
  private List<Email> emails;

  @JsonCreator
  public Contact(@JsonProperty("NAME") String name,
                 @JsonProperty("SECOND_NAME") String secondName,
                 @JsonProperty("LAST_NAME") String lastName,
                 @JsonProperty("EMAIL") List<Email> email) {
    this.name = name;
    this.secondName = secondName;
    this.lastName = lastName;
    this.emails = email;
  }

}
