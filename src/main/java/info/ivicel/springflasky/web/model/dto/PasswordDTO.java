package info.ivicel.springflasky.web.model.dto;

import info.ivicel.springflasky.core.validation.EqualsMatch;
import java.io.Serializable;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsMatch(first = "password", second = "confirmPassword",
        message = "password and save password must be the same")
public class PasswordDTO implements Serializable {

    private static final long serialVersionUID = 8722679334906337610L;

    @Pattern(regexp = "[\\w\\d@#$%^&*]{6,18}", message = "password can only be in "
            + "[a-z, A-Z, 0-9, @, #, $, %, ^, &, *, _] and length between 6 and 18")
    private String password;

    private String confirmPassword;
}
