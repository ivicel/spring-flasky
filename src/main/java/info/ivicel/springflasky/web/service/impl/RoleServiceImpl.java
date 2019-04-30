package info.ivicel.springflasky.web.service.impl;

import info.ivicel.springflasky.web.model.domain.Role;
import info.ivicel.springflasky.web.repository.RoleRepository;
import info.ivicel.springflasky.web.service.RoleService;
import java.util.List;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    private RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public List<Role> findAll() {
        return roleRepository.findAll(Sort.by(Direction.ASC, "name"));
    }
}
