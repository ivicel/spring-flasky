package info.ivicel.springflasky.web.service.impl;

import info.ivicel.springflasky.exception.AccountExistsException;
import info.ivicel.springflasky.web.model.domain.Role;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.model.dto.RegisterDto;
import info.ivicel.springflasky.web.repository.PostRepository;
import info.ivicel.springflasky.web.repository.RoleRepository;
import info.ivicel.springflasky.web.repository.UserRepository;
import info.ivicel.springflasky.web.service.UserService;
import java.util.Date;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("userService")
public class UserServiceImpl implements UserService, UserDetailsService {

    private UserRepository userRepository;
    private PostRepository postRepository;
    private RoleRepository roleRepository;
    private PasswordEncoder passwordEncoder;
    private static final Pattern emailPattern = Pattern.compile("^[-.\\w\\d]+@[-.\\w\\d]+$");

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PostRepository postRepository,
            RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.postRepository = postRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public boolean hasOwnPost(User user, Long postId) {
        return postRepository.findByIdAndAuthorId(postId, user.getId()) != null;
    }

    @Override
    public boolean checkFollowStatus(User u1, User u2) {
        return userRepository.countByFollowerIdAndFollowedId(u1.getId(), u2.getId()) > 0;
    }

    @Override
    @Transactional
    public int updateCommonProfile(User user) {
        return userRepository.updateCommonProfile(user.getName(), user.getLocation(), user.getAboutMe(),
                user.getUsername());
    }

    @Override
    @Transactional
    public User register(RegisterDto dto) throws AccountExistsException {
        int count = userRepository.countByEmailOrUsername(dto.getEmail(), dto.getUsername());
        if (count > 0) {
            throw new AccountExistsException("account already exists with [" + dto.getEmail() +
                    ", " + dto.getUsername() + "]");
        }

        Role defaultRole = roleRepository.findByDefaultRole(true);
        return userRepository.save(dto.toUser(defaultRole, passwordEncoder));
    }

    @Override
    @Transactional
    public void confirm(User user) {
        userRepository.save(user);
    }

    @Override
    public Optional<User> findByUsernameAndNotConfrim(String username) {
        return userRepository.findByUsernameAndConfirmed(username, false);
    }

    @Override
    @Transactional
    public int updatePostCount(User author) {
        return userRepository.updatePostCount(author.getUsername());
    }

    @Override
    @Transactional
    public void follow(User user, User other) {
        // don't use the following counts in current authentication object, because
        // we don't promise it sync to database in time
        if (!user.isFollowing(this, other)) {
            userRepository.increaseFollowingCount(user.getId());
            userRepository.increaseFollowerCount(other.getId());
            userRepository.insertFollowRelation(user.getId(), other.getId());
        }
    }

    @Override
    public void updateLastSeen(User user) {
        user.setLastSeen(new Date());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void unfollow(User user, User other) {
        if (user.isFollowing(this, other)) {
            // don't use the follower counts in current authentication object, because
            // we don't promise it sync to database in time
            userRepository.decreaseFollowingCount(user.getId());
            userRepository.decreaseFollowerCount(other.getId());
            userRepository.removeFollowRelation(user.getId(), other.getId());
        }
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> user;
        if (emailPattern.matcher(username).matches()) {
            user = findByEmail(username);
        } else {
            user = findByUsername(username);
        }
        if (!user.isPresent()) {
            throw new UsernameNotFoundException(String.format("username of %s not found", username));
        }

        return user.get();
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
