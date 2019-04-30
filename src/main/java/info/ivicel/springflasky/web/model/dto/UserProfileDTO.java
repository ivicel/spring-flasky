package info.ivicel.springflasky.web.model.dto;

import lombok.Data;

@Data
public class UserProfileDTO {
    private String name;
    private String location;
    private String aboutMe;
}
