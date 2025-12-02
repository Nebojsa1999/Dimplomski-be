package com.isa.repository;

import com.isa.domain.model.User;
import com.isa.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for management of the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);

    User getByEmail(String email);

    List<User> findAllByRole(Role role);

    List<User> findAllByRoleAndFirstNameContainsOrLastNameContaining(Role role, String firstName, String lastName);

    List<User> findAllByCenterAccountId(long centerAccountId);
}