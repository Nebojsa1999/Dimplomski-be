package com.isa.repository;

import com.isa.domain.model.LabDocument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LabDocumentRepository extends JpaRepository<LabDocument, Long> {
}
