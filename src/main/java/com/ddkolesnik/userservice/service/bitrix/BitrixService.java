package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexandr Stegnin
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PROTECTED)
@PropertySource(value = "classpath:application.properties")
public abstract class BitrixService {

  BitrixProperty bitrixProperty;
  RestTemplate restTemplate;

}
