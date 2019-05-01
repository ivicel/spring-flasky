package info.ivicel.springflasky.web.repository;

import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.repository.base.BaseRepository;
import java.util.Optional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    /**
     * check does user1 follow user2 by id
     * @param id1
     * @param id2
     */
    @Query(value = "select count(*) from follows where follower_id = ?1 and followed_id = ?2", nativeQuery = true)
    int countByFollowerIdAndFollowedId(Long id1, Long id2);

    /**
     * update user profile
     * @param name
     * @param location
     * @param aboutMe
     * @param username
     * @return
     */
    @Modifying
    @Query("update User set name = :name, location = :location, aboutMe = :aboutMe where username = :username")
    int updateCommonProfile(@Param("name") String name, @Param("location") String location,
            @Param("aboutMe") String aboutMe, @Param("username") String username);

    User findByEmailAndUsername(String email, String username);

    int countByEmailOrUsername(@Param("email") String email, @Param("username") String username);

    @Modifying
    @Query("update User u set u.confirmed = true where u.username = :username")
    void updateConfirmStatus(@Param("username") String username);

    Optional<User> findByUsernameAndConfirmed(String username, boolean confrimed);

    @Modifying
    @Query("update User u set u.postCount = u.postCount + 1 where u.username = :username")
    int updatePostCount(@Param("username") String username);

    @Modifying
    @Query("update User u set u.followingCount = u.followingCount + 1 where u.id = ?1")
    void increaseFollowingCount(Long id);

    @Modifying
    @Query("update User u set u.followerCount = u.followerCount + 1 where u.id = ?1")
    void increaseFollowerCount(Long id);

    @Modifying
    @Query(value = "insert into follows (follower_id, followed_id) values (:id, :other)", nativeQuery = true)
    void insertFollowRelation(@Param("id") Long id, @Param("other") Long other);

    @Modifying
    @Query("update User u set u.followingCount = u.followingCount - 1 where u.id = ?1")
    void decreaseFollowingCount(Long id);

    @Modifying
    @Query("update User u set u.followerCount = u.followerCount - 1 where u.id = ?1")
    void decreaseFollowerCount(Long id);

    @Modifying
    @Query(value = "delete from follows where follower_id = :id and followed_id = :other", nativeQuery = true)
    void removeFollowRelation(@Param("id") Long id, @Param("other") Long other);

    boolean existsByEmail(String email);

    boolean existsByUsername(String username);

    @Modifying
    @Query("update User u set u.passwordHash = :password where u.username = :username")
    int updatePassswordByUsername(@Param("username") String username, @Param("password") String password);
}
