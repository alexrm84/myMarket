package alexrm84.DTOs;

import lombok.Data;

@Data
public class AuthRequestDto {
    private String username;
    private String password;

    public AuthRequestDto(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
