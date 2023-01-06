package com.hoangvn.home24h.controllers.auth;

import java.util.Map;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import com.hoangvn.home24h.models.user.Role;
import com.hoangvn.home24h.repository.user.IRoleRepository;
import com.hoangvn.home24h.services.RoleService;

@RestController
@CrossOrigin
public class RoleController {

    @Autowired
    IRoleRepository roleRepository;

    @Autowired
    RoleService roleService;

    @GetMapping(path = "/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> getRoles() {
        try {
            return new ResponseEntity<>(roleRepository.findAll(), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PostMapping(path = "/role")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> createRole(@Valid @RequestBody Map<String, Object> roleMap) {
        try {
            Role created = roleService.createRoleFromMap(roleMap);
            return new ResponseEntity<>(roleRepository.save(created), HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @PutMapping(path = "/role/{roleKey}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> updateRole(@PathVariable String roleKey, @RequestBody Map<String, Object> roleMap) {
        try {
            Role updatedRole = roleService.updateFromMap(roleMap, roleKey);
            return new ResponseEntity<>(roleRepository.save(updatedRole), HttpStatus.OK);
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }

    @DeleteMapping(path = "/role/{id}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Object> deleteRole(@PathVariable Long id) {
        try {
            Optional<Role> optional = roleRepository.findById(id);
            if (optional.isPresent()) {
                roleRepository.deleteById(id);
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.unprocessableEntity().body(e.getMessage());
        }
    }
}
