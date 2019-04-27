package info.ivicel.springflasky.web.model.dto;

import java.util.Date;

public interface PostView extends BaseView {

    String getBody();

    String getBodyHtml();

    User getAuthor();

    interface User {

        String getUsername();

        String getEmail();

        String getName();

        String getAboutMe();

        Role getRole();

        Date getCreatedDate();

        Date getLastSeen();

        String getAvatarHash();

        Long getFollowingCount();

        Long getFollowerCount();

        Long getPostCount();

        Long getCommentCount();
    }

    interface Role {

        String getName();

        Integer getPermissions();
    }
}