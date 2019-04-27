package info.ivicel.springflasky.web.service.impl;

import info.ivicel.springflasky.web.model.domain.Post;
import info.ivicel.springflasky.web.model.dto.PostView;
import info.ivicel.springflasky.web.repository.PostRepository;
import info.ivicel.springflasky.web.service.PostService;
import info.ivicel.springflasky.web.service.UserService;
import info.ivicel.springflasky.util.CommonUtil;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("postService")
public class PostServiceImpl implements PostService {

    private PostRepository postRepository;
    private UserService userService;

    @Autowired
    public PostServiceImpl(PostRepository postRepository, UserService userService) {
        this.postRepository = postRepository;
        this.userService = userService;
    }

    @Override
    public Page<PostView> findAllWithConfirmedAccount(Pageable pageable) {
        return postRepository.findAllByAuthorConfirmedOrderByCreatedDateDesc(true, pageable);
    }

    @Override
    public Page<Post> findByAuthorId(Long authorId, Pageable pageable) {
        return postRepository.findByAuthorIdOrderByCreatedDateDesc(authorId, pageable);
    }

    @Override
    public Optional<Post> findById(Long postId) {
        return postRepository.findById(postId);
    }

    @Override
    @Transactional
    @PreAuthorize("@webAuth.canModfiyPost(authentication, #post)")
    public Post updatePost(Post post) {
        return postRepository.save(post);
    }

    @Override
    @Transactional
    @PreAuthorize("@webAuth.canModfiyPost(authentication, #post)")
    public Post insert(Post post) {
        userService.updatePostCount(post.getAuthor());
        return postRepository.save(post);
    }

    @PostAuthorize("returnObject ne null and (#returnObject.author eq #authentication or hasRole('ADMIN'))")
    private Post checkModifyPost(Post post) {
        return postRepository.findById(post.getId()).orElse(null);
    }
}
