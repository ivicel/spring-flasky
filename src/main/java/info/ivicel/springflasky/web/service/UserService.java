package info.ivicel.springflasky.web.service;

import info.ivicel.springflasky.exception.AccountExistsException;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.model.dto.RegisterDto;
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

    User register(RegisterDto dto) throws AccountExistsException;

    void confirm(User user);

    Optional<User> findByUsernameAndNotConfrim(String username);

    int updatePostCount(User author);

    int follow(User u1, User u2);

    void updateLastSeen(User user);
}
