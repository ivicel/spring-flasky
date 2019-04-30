package info.ivicel.springflasky.web.model.dto;

import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;


@Data
public class LoginDTO {
    @NotEmpty(message = "username or email can not be empty")
    @Length(min = 6, max = 16)
    private String username;

    @NotEmpty(message = "password can not be empty")
    @Length(min = 8, max = 16)
    private String password;

    private boolean rememberMe;
}
