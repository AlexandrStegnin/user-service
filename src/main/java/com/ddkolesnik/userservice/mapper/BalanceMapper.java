package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.configuration.MapStructConfig;
import com.ddkolesnik.userservice.model.dto.AccountDTO;
import com.ddkolesnik.userservice.model.dto.BalanceDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

/**
 * @author Alexandr Stegnin
 */
@Component
@Mapper(config = MapStructConfig.class)
public interface BalanceMapper {

  @Mapping(target = "accountNumber", source = "account.ownerName")
  @Mapping(target = "sum", source = "account.summary")
  BalanceDTO toBalance(AccountDTO account);

}
