package ru.rstdv.monitoringservice.dto.createupdate;

public record CreateUpdateUserDto(
        String firstname,
        String email,
        String password,
        String personalAccount,
        String city,
        String street,
        String houseNumber
) {
}
