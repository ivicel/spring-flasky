package info.ivicel.springflasky.web.repository;


import info.ivicel.springflasky.web.model.domain.Post;
import info.ivicel.springflasky.web.model.dto.PostView;
import info.ivicel.springflasky.web.repository.base.BaseRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface PostRepository extends BaseRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    Page<Post> findByAuthorIdOrderByCreatedDateDesc(Long authorId, Pageable pageable);

    Post findByIdAndAuthorId(Long id, Long authorId);

    @Modifying
    @Query("update Post set body = :body, bodyHtml = :bodyHtml where id = :id")
    int updatePost(@Param("body") String body, @Param("bodyHtml") String bodyHtml, @Param("id") Long id);

    Page<PostView> findAllByAuthorConfirmedOrderByCreatedDateDesc(Boolean confirmed, Pageable pageable);

}
