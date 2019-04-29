package info.ivicel.springflasky.web.model.dto;

public interface FollowedView extends BaseView {

    User getFollowed();

    interface User {
        String getUsername();

        String getAvatarHash();
    }
}
