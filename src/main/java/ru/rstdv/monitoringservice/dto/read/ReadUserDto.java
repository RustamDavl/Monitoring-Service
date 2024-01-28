package ru.rstdv.monitoringservice.dto.read;

import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;

/**
 * отображение класса User, возвращаемое пользователю
 *
 * @param id              идентификатор
 * @param firstname       имя
 * @param email           почта
 * @param address         адрес
 * @param role            роль
 * @param personalAccount лицевой счет
 */
public record ReadUserDto(
        String id,
        String firstname,
        String email,

        Address address,

        String role,
        String personalAccount

) {
}
