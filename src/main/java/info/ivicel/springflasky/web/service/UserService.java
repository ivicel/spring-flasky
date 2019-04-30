package info.ivicel.springflasky.web.service;

import info.ivicel.springflasky.exception.AccountExistsException;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.model.dto.AdminEditProfileDTO;
import info.ivicel.springflasky.web.model.dto.RegisterDTO;
import java.util.Optional;

public interface UserService {

    Optional<User> findByUsername(String username);

    boolean hasOwnPost(User user, Long postId);

    /**
     * check u1 has followed u2
     * @param u1
     * @param u2
     * @return true, u1 follow u2 else false
     */
    boolean checkFollowStatus(User u1, User u2);

    int updateCommonProfile(User user);

    User register(RegisterDTO dto) throws AccountExistsException;

    void confirm(User user);

    Optional<User> findByUsernameAndNotConfrim(String username);

    int updatePostCount(User author);

    void follow(User user, User other);

    void updateLastSeen(User user);

    void unfollow(User user, User other);

    Optional<User> findByEmail(String email);

    boolean checkEmailExists(String email);

    boolean checkUsernameExists(String username);

    void updateProfileByAdmin(User user, AdminEditProfileDTO profile);
}
