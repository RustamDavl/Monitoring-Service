package ru.rstdv.monitoringservice.dto.createupdate;

/**
 * класс, представляющий запрос на создание пользователя
 * @param firstname имя
 * @param email почта
 * @param password пароль
 * @param personalAccount лицевой счет
 * @param city город
 * @param street улица
 * @param houseNumber номер дома
 */
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
