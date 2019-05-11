package info.ivicel.springflasky.web.service.impl;

import info.ivicel.springflasky.web.model.domain.Comment;
import info.ivicel.springflasky.web.model.dto.CommentView;
import info.ivicel.springflasky.web.repository.CommentRepository;
import info.ivicel.springflasky.web.service.CommentService;
import info.ivicel.springflasky.web.service.UserService;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

    private CommentRepository commentRepository;
    private UserService userService;

    @Autowired
    public CommentServiceImpl(CommentRepository commentRepository, UserService userService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
    }

    @Override
    public Page<CommentView> findAll(Long id, Pageable pageable) {
        return commentRepository.findAllByPostId(id, pageable);
    }

    @Override
    @Transactional
    public Comment addNewComment(Comment comment) {
        userService.updateNewCommentCount();
        return commentRepository.save(comment);
    }

    @Override
    public Optional<Comment> findById(Long commentId) {
        return commentRepository.findById(commentId);
    }

    @Override
    public Comment save(Comment comment) {
        return commentRepository.save(comment);
    }
}
