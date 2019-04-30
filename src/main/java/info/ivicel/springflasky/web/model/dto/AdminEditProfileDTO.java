package info.ivicel.springflasky.web.model.dto;

import javax.validation.constraints.Email;
import javax.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AdminEditProfileDTO extends UserProfileDTO {

    @Email(regexp = "^[-.\\w\\d]+@[-.\\w\\d]+\\.[a-zA-Z]+$")
    private String email;

    @Pattern(regexp = "[.\\w\\d]{6,16}", message = "username must be between 6 and 16 length, "
            + "only [a-z, A-Z, 0-9, ., _] is available")
    private String username;

    private Long role;

    private boolean confirmed;

}
