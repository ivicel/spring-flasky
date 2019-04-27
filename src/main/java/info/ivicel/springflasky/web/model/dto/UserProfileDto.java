package info.ivicel.springflasky.web.model.dto;

import lombok.Data;

@Data
public class UserProfileDto {
    private String name;
    private String location;
    private String aboutMe;
}
