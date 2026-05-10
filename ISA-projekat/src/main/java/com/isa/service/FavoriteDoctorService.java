package com.isa.service;

import com.isa.domain.model.FavoriteDoctor;
import com.isa.domain.model.User;
import com.isa.repository.FavoriteDoctorRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FavoriteDoctorService {

    private final FavoriteDoctorRepository favoriteDoctorRepository;

    @Autowired
    public FavoriteDoctorService(FavoriteDoctorRepository favoriteDoctorRepository) {
        this.favoriteDoctorRepository = favoriteDoctorRepository;
    }

    public List<FavoriteDoctor> getFavoritesByPatient(User patient) {
        return favoriteDoctorRepository.findAllByPatient(patient);
    }

    @Transactional
    public FavoriteDoctor addFavorite(User patient, User doctor) {
        if (favoriteDoctorRepository.existsByPatientAndDoctor(patient, doctor)) {
            throw new IllegalArgumentException("Doctor is already in your favorites.");
        }
        final FavoriteDoctor favoriteDoctor = new FavoriteDoctor();
        favoriteDoctor.setDoctor(doctor);
        favoriteDoctor.setPatient(patient);
        return favoriteDoctorRepository.save(favoriteDoctor);
    }

    @Transactional
    public void removeFavorite(User patient, User doctor) {
        favoriteDoctorRepository.findByPatientAndDoctor(patient, doctor).ifPresent(favoriteDoctorRepository::delete);
    }

    @Transactional
    public FavoriteDoctor create(FavoriteDoctor dto) {
        final FavoriteDoctor favoriteDoctor = new FavoriteDoctor();
        favoriteDoctor.setDoctor(dto.getDoctor());
        favoriteDoctor.setPatient(dto.getPatient());
        return favoriteDoctorRepository.save(favoriteDoctor);
    }
}
