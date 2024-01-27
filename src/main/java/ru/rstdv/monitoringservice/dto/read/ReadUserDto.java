package ru.rstdv.monitoringservice.dto.read;

import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;

public record ReadUserDto(
        String id,
        String firstname,
        String email,

        Address address,

        String role,
        String personalAccount

) {
}
