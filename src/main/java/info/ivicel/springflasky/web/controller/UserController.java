package info.ivicel.springflasky.web.controller;

import static org.springframework.data.domain.Sort.Direction.DESC;

import info.ivicel.springflasky.exception.PageNotFoundException;
import info.ivicel.springflasky.web.model.domain.Post;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.model.dto.UserProfileDto;
import info.ivicel.springflasky.web.service.PostService;
import info.ivicel.springflasky.web.service.UserService;
import info.ivicel.springflasky.util.PageUtil;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Slf4j
@Controller
@RequestMapping("/user")
public class UserController {

    private UserService userService;
    private PostService postService;

    @Autowired
    public UserController(UserService userService, PostService postService) {
        this.userService = userService;
        this.postService = postService;
    }

    /**
     * user profile page
     * @param username
     * @param auth
     * @param pageable
     * @param model
     * @return
     */
    @GetMapping("/{username}")
    public String user(@PathVariable("username") String username, Authentication auth,
            @PageableDefault(page = 1, sort = "createdDate", direction = DESC) Pageable pageable, Model model) {
        pageable = PageUtil.parsePage(pageable);
        User user = getOrThrowException(username, model);
        boolean showFollow = false;
        boolean showUnfollow = false;
        boolean followedBy = false;

        // anonymous check, and do not let us follow ourselves
        if (auth != null) {
            try {
                User currentUser = (User) auth.getPrincipal();
                if (!currentUser.equals(user)) {
                    showFollow = currentUser.isFollowing(userService, user);
                    showUnfollow = !showFollow;
                    followedBy = currentUser.isFollowedBy(userService, user);
                }
            } catch (ClassCastException e) {
                log.error("current user should implements by User: {}", e.getMessage());
                throw e;
            }
        }

        pageable = PageUtil.parsePage(pageable);
        Page<Post> posts = postService.findByAuthorId(user.getId(), pageable);
        model.addAttribute("posts", posts);
        model.addAttribute("showFollow", showFollow);
        model.addAttribute("showUnfollow", showUnfollow);
        model.addAttribute("followedBy", followedBy);

        return "user/user";
    }

    /**
     * modify user profile page, protect with profile of ownner, or admin
     * @param username
     * @param authentication
     * @param model
     * @return
     */
    @GetMapping("/{username}/edit-profile")
    @PreAuthorize("hasRole('ADMIN') or @webAuth.canEditProfile(authentication, #username)")
    public String editProfile(@PathVariable("username") String username,
            Authentication authentication, Model model) {
        User user = getOrThrowException(username, model);

        return "user/edit_profile";
    }

    @PostMapping("/{username}/edit-profile")
    @PreAuthorize("hasRole('ADMIN') or @webAuth.canEditProfile(authentication, #username)")
    public String editProfile(@PathVariable("username") String username, UserProfileDto userProfile,
            RedirectAttributes ra) {
        User user = new User();
        user.setUsername(username);
        user.setName(userProfile.getName());
        user.setLocation(userProfile.getLocation());
        user.setAboutMe(userProfile.getAboutMe());
        int count = userService.updateCommonProfile(user);
        if (count == 1) {
            ra.addFlashAttribute("flashMessage", "Update user's profile successfully.");
            ra.addFlashAttribute("classappend", "alert-info");
        } else {
            ra.addFlashAttribute("flashMessage", "Can not update user's profile");
            ra.addFlashAttribute("classappend", "alert-error");
        }

        return "redirect:/user";
    }

    // todo: follow
    @PostMapping("/follow/{username}")
    @ResponseBody
    @PreAuthorize("@webAuth.loginRequired(authentication)")
    public ResponseEntity follow(Authentication auth, @PathVariable("username") String username) {
        User currentUser = (User) auth.getPrincipal();
        if (currentUser.getUsername().equals(username)) {
            log.info("user cannot follow itself.");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("can't follow myself");
        }

        Optional<User> other = userService.findByUsername(username);
        if (!other.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(String.format("%s not found", username));
        }

        int count = userService.follow(currentUser, other.get());

        return count > 0 ? ResponseEntity.status(HttpStatus.OK).body("ok") :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("internal server error");
    }

    // todo: unfollow
    @PostMapping("/unfollow/{username}")
    @ResponseBody
    @PreAuthorize("@webAuth.loginRequired(authentication)")
    public ResponseEntity unfollow(@PathVariable("username") String username) {
        return ResponseEntity.ok("ok");
    }

    // todo: admin edit profile
    @GetMapping("/admin/{username}/edit-profile")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminEditProfile(@PathVariable("username") String username, Model model) {
        getOrThrowException(username, model);

        return "admin_edit_profile";
    }

    private User getOrThrowException(String username, Model model) {
        Optional<User> user = userService.findByUsername(username);
        if (!user.isPresent()) {
            throw new PageNotFoundException();
        }
        if (model != null) {
            model.addAttribute("user", user.get());
        }
        return user.get();
    }
}
