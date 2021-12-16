package com.ddkolesnik.userservice.configuration;

import com.ddkolesnik.userservice.configuration.property.NextcloudProperty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.aarboard.nextcloud.api.AuthenticationConfig;
import org.aarboard.nextcloud.api.NextcloudConnector;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @author Alexandr Stegnin
 */
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class NextcloudConfig {

  NextcloudProperty nextcloudProperty;

  @Bean
  public NextcloudConnector getConnector() {
    AuthenticationConfig config = new AuthenticationConfig(nextcloudProperty.getLogin(), nextcloudProperty.getPassword());
    NextcloudConnector connector = new NextcloudConnector(nextcloudProperty.getServiceUrl(), config);
    connector.trustAllCertificates(true);
    return connector;
  }

}
