package info.ivicel.springflasky.web.model.domain;


import static javax.persistence.ConstraintMode.NO_CONSTRAINT;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Getter
@Setter
@EntityListeners({SanitizerListener.class})
@Entity
@Table(name = "posts")
public class Post extends BaseDomain implements Serializable {

    private static final long serialVersionUID = -6830098959594362217L;

    @Sanitize
    @Lob
    @Column(name = "body", nullable = false)
    private String body;

    @Sanitize
    @Lob
    @Column(name = "body_html")
    private String bodyHtml;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(NO_CONSTRAINT))
    private User author;
}
