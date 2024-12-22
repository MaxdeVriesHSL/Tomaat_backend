package tomaat.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tomaat.DAO.RoleRepository;
import tomaat.model.Role;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleService {
    private final RoleRepository roleRepository;

    public Role getByName(String name) {
        return roleRepository.findByName(name)
                .orElseThrow(() -> new EntityNotFoundException("Role not found with name: " + name));
    }

    public String getRoleNameByUserId(UUID userId) {
        return roleRepository.findRoleNameByUserId(userId)
                .orElseThrow(() -> new EntityNotFoundException("Role not found for user: " + userId));
    }

    public Role createRole(String name) {
        Role role = new Role(name);
        return roleRepository.save(role);
    }

    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    public void deleteRole(UUID id) {
        if (!roleRepository.existsById(id)) {
            throw new EntityNotFoundException("Role not found with id: " + id);
        }
        roleRepository.deleteById(id);
    }
}