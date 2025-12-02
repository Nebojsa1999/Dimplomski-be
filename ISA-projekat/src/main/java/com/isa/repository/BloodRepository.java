package com.isa.repository;

import com.isa.domain.model.Blood;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BloodRepository extends JpaRepository<Blood, Long> {

    List<Blood> findBloodByCenterAccountId(long centerAccountId);
}
