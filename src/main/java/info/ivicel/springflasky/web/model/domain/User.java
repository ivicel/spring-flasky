package info.ivicel.springflasky.web.model.domain;


import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

import com.fasterxml.jackson.annotation.JsonIgnore;
import info.ivicel.springflasky.core.listener.LastSeenListener;
import info.ivicel.springflasky.web.model.Permission;
import info.ivicel.springflasky.web.service.UserService;
import java.io.Serializable;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.ForeignKey;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@Setter
@Entity
@EntityListeners(LastSeenListener.class)
@Table(name = "users", indexes = {@Index(columnList = "email"), @Index(columnList = "username")})
public class User extends BaseDomain implements UserDetails, Serializable {

    private static final long serialVersionUID = -7454919476277951359L;

    @Column(name = "email", length = 64, nullable = false, unique = true)
    private String email;

    @Column(name = "username", length = 64, nullable = false, unique = true)
    private String username;

    @Column(name = "password_hash", nullable = false)
    @JsonIgnore
    private String passwordHash;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "role_id", nullable = false, foreignKey = @ForeignKey(NO_CONSTRAINT))
    private Role role;

    @Column(name = "confirmed", columnDefinition = "tinyint(1) not null default 0", length = 1)
    private boolean confirmed = false;

    @Column(name = "name", length = 30)
    private String name;

    private String location;

    // @JsonIgnore
    // @ManyToMany(mappedBy = "followings")
    // private List<User> followers = new ArrayList<>();
    //
    // @JsonIgnore
    // @ManyToMany
    // @JoinTable(name = "follows", joinColumns = {@JoinColumn(name = "follower_id", nullable = false)},
    //     inverseJoinColumns = {@JoinColumn(name = "followed_id", nullable = false)},
    //     indexes = {@Index(columnList = "follower_id"), @Index(columnList = "followed_id")},
    //     foreignKey = @ForeignKey(NO_CONSTRAINT), inverseForeignKey = @ForeignKey(NO_CONSTRAINT))
    // private List<User> followings = new ArrayList<>();

    @Lob
    @Column(name = "about_me")
    private String aboutMe;

    @Column(name = "last_seen")
    private Date lastSeen;

    private String avatarHash;

    @Column(name = "following_count")
    private Long followingCount = 0L;

    @Column(name = "follower_count")
    private Long followerCount = 0L;

    @Column(name = "post_count")
    private Long postCount = 0L;

    @Column(name = "comment_count")
    private Long commentCount = 0L;

    /**
     * check user get the permission
     * @param permission
     * @return
     */
    public boolean can(Permission permission) {
        return (role.getPermissions() & permission.getValue()) == permission.getValue();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.singleton(role);
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    /**
     * check whether this user follow other user
     * @param userService
     * @param other
     * @return
     */
    public boolean isFollowing(UserService userService, User other) {
        return userService.checkFollowStatus(this, other);
    }

    /**
     * check whether this user is followed by other user
     * @param userService
     * @param other
     * @return
     */
    public boolean isFollowedBy(UserService userService, User other) {
        return userService.checkFollowStatus(other, this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        User user = (User) o;

        return new org.apache.commons.lang3.builder.EqualsBuilder()
                .append(email, user.email)
                .append(username, user.username)
                .isEquals();
    }

    @Override
    public int hashCode() {
        return new org.apache.commons.lang3.builder.HashCodeBuilder(17, 37)
                .append(email)
                .append(username)
                .toHashCode();
    }
}
