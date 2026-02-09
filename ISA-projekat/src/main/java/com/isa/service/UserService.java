package com.isa.service;

import com.isa.domain.dto.ChangePasswordDTO;
import com.isa.domain.dto.UserDTO;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.Hospital;
import com.isa.domain.model.User;
import com.isa.enums.AppointmentStatus;
import com.isa.enums.Gender;
import com.isa.enums.Role;
import com.isa.repository.HospitalRepository;
import com.isa.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    private final HospitalRepository hospitalRepository;

    private final AppointmentService appointmentService;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder, HospitalRepository hospitalRepository, AppointmentService appointmentService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.hospitalRepository = hospitalRepository;
        this.appointmentService = appointmentService;
    }

    @Transactional
    public User register(UserDTO userDTO) {
        final User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        final double latitude = userDTO.getLatitude() == null ? 0 : Double.parseDouble(userDTO.getLatitude());
        final double longitude = userDTO.getLongitude() == null ? 0 : Double.parseDouble(userDTO.getLongitude());
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setCountry(userDTO.getCountry());
        user.setFirstLogin(true);
        user.setPhone(userDTO.getPhone());
        user.setGender(Gender.valueOf(userDTO.getGender()));
        user.setOccupationInfo(userDTO.getOccupationInfo());
        user.setOccupation(userDTO.getOccupation());
        user.setPersonalId(userDTO.getPersonalId());

        if (userDTO.getDoctorType() != null){
            user.setDoctorType(userDTO.getDoctorType());
        }

        user.setHospital(hospitalRepository.findById(userDTO.getHospitalId()).orElseThrow());

        return userRepository.save(user);
    }

    public List<User> list(String name) {
        return name != null ? userRepository.findAllByName(name) : userRepository.findAll();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> getAllByHospital(Hospital hospital, Role role, String name) {
        return role != null ? userRepository.findAllByHospitalIdAndRole(hospital.getId(), role, name) : userRepository.findAllByHospitalId(hospital.getId(), name);
    }

    public Optional<User> get(long userId) {
        return userRepository.findById(userId);
    }

    public User changePassword(ChangePasswordDTO changePasswordDTO, User user) {
        if (user == null) {
            return null;
        }

        user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        user.setFirstLogin(false);

        return userRepository.save(user);
    }

    public User updateProfile(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        final double longitude = userDTO.getLongitude() == null ? 0 : Double.parseDouble(userDTO.getLongitude());
        user.setCountry(userDTO.getCountry());
        final double latitude = userDTO.getLatitude() == null ? 0 : Double.parseDouble(userDTO.getLatitude());
        user.setLatitude(latitude);
        user.setLongitude(longitude);
        user.setFirstLogin(true);
        user.setPhone(userDTO.getPhone());
        user.setOccupationInfo(userDTO.getOccupationInfo());
        user.setOccupation(userDTO.getOccupation());
        user.setPersonalId(userDTO.getPersonalId());
        user.setGender(Gender.valueOf(userDTO.getGender()));
        return userRepository.save(user);
    }

    @Transactional
    public void lowerUserPoints(Appointment appointment) {
        final User patient = appointment.getPatient();
        patient.setPoints(patient.getPoints() - 1);
        userRepository.save(patient);
        appointment.setPatient(null);
        appointment.setAppointmentStatus(AppointmentStatus.CANCELLED);
        appointmentService.save(appointment);
    }
}
