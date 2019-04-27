package info.ivicel.springflasky.web.service.impl;

import info.ivicel.springflasky.web.repository.RoleRepository;
import info.ivicel.springflasky.web.service.RoleService;
import org.springframework.stereotype.Service;

@Service("roleService")
public class RoleServiceImpl implements RoleService {
    private RoleRepository repository;

    public RoleServiceImpl(RoleRepository repository) {
        this.repository = repository;
    }
}
