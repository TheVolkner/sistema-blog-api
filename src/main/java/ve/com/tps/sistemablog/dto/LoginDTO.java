package ve.com.tps.sistemablog.dto;

import lombok.Data;

@Data
public class LoginDTO {

    private String usernameOrEmail;

    private String password;
}
