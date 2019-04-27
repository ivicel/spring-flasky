package info.ivicel.springflasky.web.model.dto;

import static info.ivicel.springflasky.util.CommonUtil.hashAvatar;

import info.ivicel.springflasky.core.validation.EqualsMatch;
import info.ivicel.springflasky.web.model.domain.Role;
import info.ivicel.springflasky.web.model.domain.User;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.Data;
import org.springframework.security.crypto.password.PasswordEncoder;


@Data
@EqualsMatch(first = "password", second = "confirmPassword",
        message = "password and confirm password must be the same")
public class RegisterDto implements Serializable {

    private static final long serialVersionUID = -1298146585566057857L;

    @Email(regexp = "^[.\\w\\d]+@[_\\w\\d]+\\.[a-zA-Z]+$")
    private String email;

    @Pattern(regexp = "[.\\w\\d]{6,16}", message = "username must be between 6 and 16 length, "
            + "only [a-z, A-Z, 0-9, ., _] is available")
    private String username;

    @Pattern(regexp = "[\\w\\d@#$%^&*]{6,18}", message = "password can only be in "
            + "[a-z, A-Z, 0-9, @, #, $, %, ^, &, *, _] and length between 6 and 18")
    private String password;

    private String confirmPassword;

    public User toUser(Role defaultRole, PasswordEncoder encoder) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(password));
        u.setConfirmed(false);
        u.setRole(defaultRole);
        u.setAvatarHash(hashAvatar(email));

        return u;
    }
}
