package ru.rstdv.monitoringservice.dto.createupdate;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

/**
 * класс, представляющий запрос на создание пользователя
 *
 * @param firstname       имя
 * @param email           почта
 * @param password        пароль
 * @param personalAccount лицевой счет
 * @param city            город
 * @param street          улица
 * @param houseNumber     номер дома
 * @author RustamD
 * @version 1.0
 */
public record CreateUpdateUserDto(
        @NotBlank
        String firstname,
        @NotBlank
        @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")
        String email,
        @NotBlank
        String password,
        @NotBlank
        @Pattern(regexp = "\\d{9}")
        String personalAccount,
        @NotBlank
        @Pattern(regexp = "[a-zA-Z]+")
        String city,
        @NotBlank
        @Pattern(regexp = "[a-zA-Z]+")
        String street,
        @NotBlank
        @Pattern(regexp = "\\d+")
        String houseNumber
) {
}
