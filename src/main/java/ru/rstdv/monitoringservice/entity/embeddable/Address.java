package ru.rstdv.monitoringservice.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * встариваемый класс, представляющий адрес пользователя с полями : город, улица и номера дома
 *
 * @author RustamD
 * @version 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Address {

    private String city;
    private String street;
    private String houseNumber;
}
