package info.ivicel.springflasky.web.service;

import info.ivicel.springflasky.web.model.domain.Comment;
import info.ivicel.springflasky.web.model.dto.CommentView;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommentService {

    Page<CommentView> findAll(Long id, Pageable pageable);

    Comment addNewComment(Comment comment);

    Optional<Comment> findById(Long commentId);

    Comment save(Comment comment);
}
