package info.ivicel.springflasky.web.repository;

import info.ivicel.springflasky.web.model.domain.Comment;
import info.ivicel.springflasky.web.model.dto.CommentView;
import info.ivicel.springflasky.web.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentRepository extends BaseRepository<Comment, Long> {

    Page<CommentView> findAllByPostId(Long id, Pageable pageable);
}
