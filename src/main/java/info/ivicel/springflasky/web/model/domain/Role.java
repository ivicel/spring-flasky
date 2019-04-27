package info.ivicel.springflasky.web.model.domain;


import info.ivicel.springflasky.web.model.Permission;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role extends BaseDomain implements GrantedAuthority, Serializable {

    private static final long serialVersionUID = -6830098959594362217L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", unique = true, length = 64, nullable = false)
    private String name;

    @Column(name = "default_role", columnDefinition = "tinyint(1) not null default 0", length = 1)
    private boolean defaultRole = false;

    /**
     * 权限
     * FOLLOW = 0x01
     * COMMENT = 0x02
     * WRITE = 0x04
     * MODERATE = 0x08
     * ADMIN = 0xf0
     */
    @Column(name = "permissions")
    private Integer permissions = 0;

    public void setPermissions(Permission...permissions) {
        for (Permission permission : permissions) {
            this.permissions |= permission.getValue();
        }
    }

    @Override
    public String getAuthority() {
        return "ROLE_" + name.toUpperCase();
    }
}
