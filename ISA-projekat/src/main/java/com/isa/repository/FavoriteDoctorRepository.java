package com.isa.repository;

import com.isa.domain.model.FavoriteDoctor;
import com.isa.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteDoctorRepository extends JpaRepository<FavoriteDoctor, Long> {

    List<FavoriteDoctor> findAllByPatient(User patient);

    Optional<FavoriteDoctor> findByPatientAndDoctor(User patient, User doctor);

    boolean existsByPatientAndDoctor(User patient, User doctor);
}
