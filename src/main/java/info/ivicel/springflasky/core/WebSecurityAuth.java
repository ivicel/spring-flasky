package info.ivicel.springflasky.core;

import info.ivicel.springflasky.web.model.Permission;
import info.ivicel.springflasky.web.model.domain.Post;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.service.PostService;
import info.ivicel.springflasky.web.service.UserService;
import lombok.Data;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;


@Data
public class WebSecurityAuth {

    private UserService userService;
    private PostService postService;

    /**
     * only admin or post of owner can modify it
     * @param auth
     * @param post
     * @return
     */
    public boolean canModfiyPost(Authentication auth, Post post) {
        // check anonymous
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return false;
        }

        return AuthorityUtils.authorityListToSet(auth.getAuthorities()).contains("ROLE_ADMIN") ||
                (post.getAuthor().equals(auth.getPrincipal()) && hasPermission(auth, Permission.WRITE));
    }

    public boolean canEditProfile(Authentication auth, String username) {
        if (!(auth instanceof User)) {
            return false;
        }

        return auth.getName().equals(username) ||
                AuthorityUtils.authorityListToSet(auth.getAuthorities()).contains("ROLE_ADMIN") ||
                AuthorityUtils.authorityListToSet(auth.getAuthorities()).contains("ROLE_MODERATOR");
    }

    public boolean hasPermission(Authentication auth, Permission permission) {
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return false;
        }
        User user = (User) auth.getPrincipal();
        return user.can(permission);
    }

    public boolean loginRequired(Authentication auth) {
        return auth.getPrincipal() instanceof User;
    }

    public boolean isConfirmed(Authentication auth) {
        if (auth == null || !(auth.getPrincipal() instanceof User)) {
            return false;
        }

        return ((User) auth.getPrincipal()).isConfirmed();
    }
}
