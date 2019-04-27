package info.ivicel.springflasky.web.controller;

import info.ivicel.springflasky.web.model.dto.PostView;
import info.ivicel.springflasky.web.repository.PostRepository;
import info.ivicel.springflasky.web.service.PostService;
import info.ivicel.springflasky.web.service.UserService;
import info.ivicel.springflasky.util.PageUtil;
import javax.servlet.http.HttpServletRequest;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Setter(onMethod = @__({@Autowired}))
@Controller
@RequestMapping("/")
public class HomeController {

    private PostService postService;
    private UserService userService;
    private PostRepository postRepository;

    @GetMapping
    public String index(Model model, HttpServletRequest request,
            @PageableDefault(page = 1, size = 20, sort = "createdDate",
                    direction = Direction.DESC) Pageable pageable) {
        pageable = PageUtil.parsePage(pageable);
        Page<PostView> posts = postService.findAllWithConfirmedAccount(pageable);
        model.addAttribute("posts", posts);

        return "index";
    }

    @GetMapping("/index")
    @ResponseBody
    public ResponseEntity auth(Authentication authentication) {
        return ResponseEntity.ok(authentication);
    }
}
