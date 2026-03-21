package com.isa.service;

import com.isa.repository.LabDocumentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class LabDocumentService {

    private final LabDocumentRepository labDocumentRepository;

    @Autowired
    public LabDocumentService(LabDocumentRepository labDocumentRepository) {
        this.labDocumentRepository = labDocumentRepository;
    }
}
