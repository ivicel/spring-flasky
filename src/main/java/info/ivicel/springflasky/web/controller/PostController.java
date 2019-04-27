package info.ivicel.springflasky.web.controller;

import info.ivicel.springflasky.core.WebSecurityAuth;
import info.ivicel.springflasky.exception.PageNotFoundException;
import info.ivicel.springflasky.web.model.domain.Post;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.model.dto.CommentView;
import info.ivicel.springflasky.web.model.dto.PostDto;
import info.ivicel.springflasky.web.service.CommentService;
import info.ivicel.springflasky.web.service.PostService;
import info.ivicel.springflasky.util.PageUtil;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/post")
public class PostController {
    private PostService postService;
    private CommentService commentService;

    @Autowired
    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @PostMapping
    @ResponseBody
    public ResponseEntity addNewPost(Authentication auth, @Validated PostDto postDto) {
        Post post = postDto.toPost();
        post.setAuthor((User) auth.getPrincipal());

        Map<String, String> result = new HashMap<>();
        post = postService.insert(post);

        if (post.getId() != null) {
            result.put("message", "Add new post successfully.");
            result.put("class", "alert-info");
        } else {
            result.put("message", "Can not add new post");
            result.put("class", "alert-error");
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{postId}")
    public String getPost(@PathVariable("postId") Long postId, Model model,
            @PageableDefault(page = 1, size = 10, sort = "createdDate") Pageable pageable) {
        Optional<Post> post = postService.findById(postId);
        if (!post.isPresent()) {
            throw new PageNotFoundException();
        }

        pageable = PageUtil.parsePage(pageable);
        Page<CommentView> comments = commentService.findAll(post.get().getId(), pageable);

        model.addAttribute("posts", Collections.singletonList(post.get()));
        model.addAttribute("post", post.get());
        model.addAttribute("comments", comments);

        return "post";
    }

    @GetMapping("/edit/{postId}")
    public String editPost(@PathVariable("postId") Long postId, Model model,
            WebSecurityAuth webSecurity, Authentication auth) {
        Post post = getOrThrowException(postId);
        if (!webSecurity.canModfiyPost(auth, post)) {
            throw new AccessDeniedException("Post[id = " + postId + "] can not access.");
        }

        model.addAttribute("post", post);

        return "edit_post";
    }

    /**
     * we should protect modify post method, only post of owner, or admin can modify it
     * @param postId
     * @param postDto
     * @return
     */
    @PostMapping("/edit/{postId}")
    @ResponseBody
    public ResponseEntity updatePost(@PathVariable("postId") Long postId,@Validated PostDto postDto) {
        Post post = getOrThrowException(postId);
        post = postDto.toPost(post);

        post = postService.updatePost(post);
        Map<String, String> result = new HashMap<>();

        if (post == null) {
            result.put("message", "Update post successfully.");
            result.put("class", "alert-info");
        } else {
            result.put("message", "Can not update post");
            result.put("class", "alert-error");
        }

        return ResponseEntity.ok(result);
    }

    private Post getOrThrowException(Long postId) {
        Optional<Post> post = postService.findById(postId);
        if (!post.isPresent()) {
            throw new PageNotFoundException();
        }

        return post.get();
    }
}
