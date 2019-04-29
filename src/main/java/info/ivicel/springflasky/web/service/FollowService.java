package info.ivicel.springflasky.web.service;

import info.ivicel.springflasky.web.model.domain.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FollowService {

    <T> Page<T> findAllByFollower(User user, Class<T> type, Pageable pageable);

    <T> Page<T> findAllByFollowed(User user, Class<T> type, Pageable pageable);
}
