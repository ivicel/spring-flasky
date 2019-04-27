package info.ivicel.springflasky.web.model.dto;

import java.util.Date;

public interface BaseView {

    Long getId();

    Date getCreatedDate();

    Date getLastModifiedDate();
}
