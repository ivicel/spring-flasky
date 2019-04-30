package info.ivicel.springflasky.web.model.dto;

import info.ivicel.springflasky.web.model.domain.Post;
import javax.validation.constraints.NotEmpty;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class PostDTO {
    @NotEmpty
    private String body;

    private String bodyHtml;

    public Post toPost() {
        Post post = new Post();

        return toPost(post);
    }

    public Post toPost(@NonNull Post post) {
        post.setBody(getBody());
        post.setBodyHtml(getBodyHtml());

        return post;
    }
}
