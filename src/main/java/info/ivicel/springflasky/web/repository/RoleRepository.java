package info.ivicel.springflasky.web.repository;

import info.ivicel.springflasky.web.model.domain.Role;
import info.ivicel.springflasky.web.repository.base.BaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role, Long> {

    Role findByDefaultRole(boolean isDefault);
}
