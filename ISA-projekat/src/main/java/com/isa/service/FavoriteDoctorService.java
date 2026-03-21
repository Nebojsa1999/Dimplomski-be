package com.isa.service;

import com.isa.domain.model.FavoriteDoctor;
import com.isa.repository.FavoriteDoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FavoriteDoctorService {

    private final FavoriteDoctorRepository favoriteDoctorRepository;

    @Autowired
    public FavoriteDoctorService(FavoriteDoctorRepository favoriteDoctorRepository) {
        this.favoriteDoctorRepository = favoriteDoctorRepository;
    }

    @Transactional
    public FavoriteDoctor create(FavoriteDoctor dto) {
        final FavoriteDoctor favoriteDoctor = new FavoriteDoctor();
        favoriteDoctor.setDoctor(dto.getDoctor());
        favoriteDoctor.setPatient(dto.getPatient());

        return favoriteDoctorRepository.save(favoriteDoctor);
    }
}
