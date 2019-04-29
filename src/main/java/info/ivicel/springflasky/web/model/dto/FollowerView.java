package info.ivicel.springflasky.web.model.dto;

import info.ivicel.springflasky.web.model.dto.PostView.User;

public interface FollowerView extends BaseView {

    User getFollower();

    interface User {
        String getUsername();

        String getAvatarHash();
    }
}
