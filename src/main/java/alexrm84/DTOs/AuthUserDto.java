package alexrm84.DTOs;

import alexrm84.utils.validation.FieldMatch;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
public class AuthUserDto {

    @NotNull(message = "Not null check")
    @Size(min = 11, message = "Phone length must be 11 symbols")
    private String phone;

    @NotNull(message = "Not null check")
    @Size(min = 4, message = "Password length must be greater than 3 symbols")
    private String password;

    @NotNull(message = "Not null check")
    @Size(min = 4, message = "Password length must be greater than 3 symbols")
    private String matchingPassword;

    @NotNull(message = "Not null check")
    @Email
    private String email;

    @NotNull(message = "Not null check")
    @Size(min = 4, message = "FirstName length must be greater than 3 symbols")
    private String firstName;

    @NotNull(message = "Not null check")
    @Size(min = 4, message = "LastName length must be greater than 3 symbols")
    private String lastName;
}
