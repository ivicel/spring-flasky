package info.ivicel.springflasky.web.repository;

import info.ivicel.springflasky.web.model.domain.Follow;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface FollowRepository extends BaseRepository<Follow, Long> {

    /**
     * find users that follow param user
     * @param user
     * @param pageable
     * @return
     */
    <T> Page<T> findAllByFollowed(User user, Class<T> type, Pageable pageable);

    /**
     * find users that followed by param user
     * @param user
     * @param pageable
     * @return
     */
    <T> Page<T> findAllByFollower(User user, Class<T> type, Pageable pageable);
}
