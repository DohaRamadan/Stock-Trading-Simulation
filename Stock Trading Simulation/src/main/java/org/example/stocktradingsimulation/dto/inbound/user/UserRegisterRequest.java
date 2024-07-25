package org.example.stocktradingsimulation.dto.inbound.user;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.example.stocktradingsimulation.validtors.Email;
import org.example.stocktradingsimulation.validtors.Name;
import org.example.stocktradingsimulation.validtors.Password;
import org.example.stocktradingsimulation.validtors.PhoneNumber;
import org.springframework.validation.annotation.Validated;

@Validated
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterRequest {
    @NotNull
    @JsonProperty("firstName")
    @Name
    private String firstName;

    @NotNull
    @JsonProperty("lastName")
    @Name
    private String lastName;

    @NotNull
    @JsonProperty("phoneNumber")
    @PhoneNumber
    private String phoneNumber;

    @NotNull
    @JsonProperty("email")
    @Email
    private String email;

    @NotNull
    @JsonProperty("password")
    @Password
    private String password;
}
