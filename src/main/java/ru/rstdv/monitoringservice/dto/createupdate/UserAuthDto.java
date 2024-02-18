package ru.rstdv.monitoringservice.dto.createupdate;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record UserAuthDto(@NotBlank (message = "email must not be empty")
                          @Pattern(regexp = "^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$", message = "email must satisfy the pattern : %login%@gmail.com")
                          String email,
                          @NotBlank
                          String password) {
}
