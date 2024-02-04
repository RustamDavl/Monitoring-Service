package ru.rstdv.monitoringservice.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.rstdv.monitoringservice.entity.embeddable.Address;
import ru.rstdv.monitoringservice.entity.embeddable.Role;

import java.util.List;

/**
 * сущность пользователя, сохраняемая в таблицу базы данных
 *
 * @author RustamD
 * @version 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User {

    private Long id;
    private String firstname;
    private String email;
    private String password;
    private String personalAccount;
    @Builder.Default
    private Role role = Role.USER;
    private Address address;

}
