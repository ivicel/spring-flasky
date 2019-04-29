package info.ivicel.springflasky.web.model.domain;

import javax.persistence.ConstraintMode;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "follows")
public class Follow extends BaseDomain {

    private static final long serialVersionUID = -119359227790097339L;

    @ManyToOne
    @JoinColumn(name = "follower_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User follower;

    @ManyToOne
    @JoinColumn(name = "followed_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private User followed;
}
