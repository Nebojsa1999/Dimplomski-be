package com.isa.repository;

import com.isa.domain.model.User;
import com.isa.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * JPA repository for management of the User entity.
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findOneByEmail(String email);

    @Query("""
            select user
                        FROM User user
                                    WHERE :searchFilter IS NULL OR :searchFilter = '' OR  LOWER(user.firstName) LIKE LOWER(CONCAT('%', :searchFilter, '%'))
            """)
    List<User> findAllByName(@Param("searchFilter") String searchFilter);

    @Query("""
            SELECT user
                   FROM User user
                     WHERE user.hospital.id = :hospitalId AND ( :searchFilter IS NULL OR :searchFilter = '' OR  LOWER(user.firstName) LIKE LOWER(CONCAT('%', :searchFilter, '%')))
            """)
    List<User> findAllByHospitalId(Long hospitalId, @Param("searchFilter") String searchFilter);

    @Query("""
            SELECT user
                   FROM User user
                     WHERE user.hospital.id = :hospitalId AND user.role = :role AND ( :searchFilter IS NULL
                                                                                           OR :searchFilter = '' OR  LOWER(user.firstName) LIKE LOWER(CONCAT('%', :searchFilter, '%')))
            """)
    List<User> findAllByHospitalIdAndRole(@Param("hospitalId") long hospitalId, @Param("role") Role role, @Param("searchFilter") String searchFilter);
}