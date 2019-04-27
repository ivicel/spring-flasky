package info.ivicel.springflasky.web.model.dto;

import info.ivicel.springflasky.web.model.dto.PostView.User;

public interface CommentView extends BaseView {
    String getBody();

    String getBodyHtml();

    boolean getDisabled();



    User getAuthor();

    PostView getPost();
}
