package alexrm84.DTOs;

import alexrm84.utils.validation.FieldMatch;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@NoArgsConstructor
@FieldMatch(first = "password", second = "matchingPassword", message = "The password fields must match")
public class NewPasswordDto {

    @NotNull(message = "Not null check")
    @Size(min = 4, message = "Password length must be greater than 3 symbols")
    private String password;

    @NotNull(message = "Not null check")
    @Size(min = 4, message = "Password length must be greater than 3 symbols")
    private String matchingPassword;

    @NotNull(message = "Not null check")
    @Size(min = 3, message = "Password length must be greater than 3 symbols")
    private String oldPassword;
}
