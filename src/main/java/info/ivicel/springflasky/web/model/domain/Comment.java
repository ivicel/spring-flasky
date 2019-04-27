package info.ivicel.springflasky.web.model.domain;


import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

import info.ivicel.springflasky.core.Sanitize;
import info.ivicel.springflasky.core.listener.SanitizerListener;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@EntityListeners(SanitizerListener.class)
@Table(name = "comments")
public class Comment extends BaseDomain implements Serializable {

    private static final long serialVersionUID = -1029418689058348694L;

    @Sanitize
    @Lob
    private String body;

    @Sanitize
    @Lob
    private String bodyHtml;

    @Column(name = "disabled", columnDefinition = "tinyint(1) not null default 0", length = 1)
    private boolean disabled = false;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(NO_CONSTRAINT))
    private User author;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false, columnDefinition = "bigint",
            foreignKey = @ForeignKey(NO_CONSTRAINT))
    private Post post;
}
