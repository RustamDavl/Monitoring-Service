package ru.rstdv.monitoringservice.entity.embeddable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class Address {

    private String city;
    private String street;
    private String houseNumber;
}
