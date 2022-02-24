package isu.volunteer.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import isu.volunteer.api.role.Role;
import isu.volunteer.api.role.RoleRepository;

@Component
public class SetUpDataLoader implements ApplicationListener<ContextRefreshedEvent> {
    @Autowired
    private RoleRepository roleRepository;

    private boolean already = false;

    @Override
    @Transactional
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if(!already) {
            this.createRoleIfNotExist("ROLE_USER");
            this.createRoleIfNotExist("ROLE_MEDIC");
        }

        already = true;
    }

    @Transactional
    public void createRoleIfNotExist(String roleName) {
        Role role = this.roleRepository.findByName(roleName);
        if(role == null) {
            role = new Role();
            role.setName(roleName);
            this.roleRepository.save(role);
        }
    }
}
