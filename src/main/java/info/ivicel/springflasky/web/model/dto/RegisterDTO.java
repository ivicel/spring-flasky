package info.ivicel.springflasky.web.model.dto;

import static info.ivicel.springflasky.util.CommonUtil.hashAvatar;

import info.ivicel.springflasky.web.model.domain.Role;
import info.ivicel.springflasky.web.model.domain.User;
import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;


@Getter
@Setter
public class RegisterDTO extends PasswordDTO implements Serializable {

    private static final long serialVersionUID = -1298146585566057857L;

    @Email(regexp = "^[-.\\w\\d]+@[-.\\w\\d]+\\.[a-zA-Z]+$")
    private String email;

    @Pattern(regexp = "[.\\w\\d]{6,16}", message = "username must be between 6 and 16 length, "
            + "only [a-z, A-Z, 0-9, ., _] is available")
    private String username;

    public User toUser(Role defaultRole, PasswordEncoder encoder) {
        User u = new User();
        u.setUsername(username);
        u.setEmail(email);
        u.setPasswordHash(encoder.encode(getPassword()));
        u.setConfirmed(false);
        u.setRole(defaultRole);
        u.setAvatarHash(hashAvatar(email));

        return u;
    }
}
