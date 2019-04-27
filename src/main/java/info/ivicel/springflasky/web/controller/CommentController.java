package info.ivicel.springflasky.web.controller;

import info.ivicel.springflasky.web.model.domain.Comment;
import info.ivicel.springflasky.web.model.domain.Post;
import info.ivicel.springflasky.web.model.domain.User;
import info.ivicel.springflasky.web.service.CommentService;
import info.ivicel.springflasky.web.service.PostService;
import java.util.Optional;
import javax.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/comment")
public class CommentController {
    private CommentService commentService;
    private PostService postService;

    @Autowired
    public CommentController(CommentService commentService, PostService postService) {
        this.commentService = commentService;
        this.postService = postService;
    }

    @PostMapping("/post-new-comment")
    @ResponseBody
    @PreAuthorize("hasPermission(null, T(info.ivicel.springflasky.web.model.Permission).COMMENT)")
    public ResponseEntity postNewComment(@RequestParam("postId") Long postId,
            @RequestParam("body") String body, String bodyHtml,
            Authentication auth) {
        Optional<Post> post = postService.findById(postId);

        if (!post.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("post not exists.");
        }

        Comment comment = new Comment();
        comment.setBody(body);
        comment.setBodyHtml(bodyHtml);
        comment.setAuthor((User) auth.getPrincipal());
        comment.setPost(post.get());
        comment = commentService.addNewComment(comment);
        if (comment.getId() == null) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok("success");
    }

    @PostMapping("/moderate")
    @ResponseBody
    @PreAuthorize("hasPermission(null, T(info.ivicel.springflasky.web.model.Permission).MODERATE)")
    public ResponseEntity moderateComemnt(@RequestParam("commentId") @Positive @Validated Long commentId,
            @RequestParam(value = "disable", defaultValue = "true") boolean disable, Authentication auth) {
        Optional<Comment> comment = commentService.findById(commentId);
        if (!comment.isPresent()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(HttpStatus.BAD_REQUEST.getReasonPhrase());
        }
        comment.get().setDisabled(disable);
        commentService.save(comment.get());
        return ResponseEntity.ok("success");
    }
}
