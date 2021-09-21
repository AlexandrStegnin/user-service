package com.ddkolesnik.userservice.configuration;

import org.mapstruct.Builder;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.MapperConfig;
import org.mapstruct.ReportingPolicy;

/**
 * @author Alexandr Stegnin
 */
@MapperConfig(
    unmappedTargetPolicy = ReportingPolicy.ERROR,
    componentModel = "spring",
    injectionStrategy = InjectionStrategy.CONSTRUCTOR,
    builder = @Builder(disableBuilder = true)
)
public interface MapStructConfig {
}

