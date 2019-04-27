package info.ivicel.springflasky.web.service;

import info.ivicel.springflasky.web.model.domain.Post;
import info.ivicel.springflasky.web.model.dto.PostView;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PostService {

    Page<PostView> findAllWithConfirmedAccount(Pageable pageable);

    Page<Post> findByAuthorId(Long authorId, Pageable pageable);

    Optional<Post> findById(Long postId);

    Post updatePost(Post post);

    Post insert(Post post);
}
