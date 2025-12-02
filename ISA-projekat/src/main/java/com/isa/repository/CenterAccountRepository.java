package com.isa.repository;

import com.isa.domain.model.CenterAccount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CenterAccountRepository extends JpaRepository<CenterAccount, Long> {

    @Query("""
            SELECT centerAccount FROM CenterAccount centerAccount
            WHERE centerAccount.name = :searchFilter
            """)
    List<CenterAccount> findByName(@Param("searchFilter") String searchFilter);
}
