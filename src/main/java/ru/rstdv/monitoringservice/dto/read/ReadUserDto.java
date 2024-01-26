package ru.rstdv.monitoringservice.dto.read;

import ru.rstdv.monitoringservice.entity.embeddable.Address;

public record ReadUserDto(
        String id,
        String firstname,
        String email,

        Address address,

        String personalAccount

) {
}
