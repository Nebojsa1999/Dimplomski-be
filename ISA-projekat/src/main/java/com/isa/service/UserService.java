package com.isa.service;

import com.isa.domain.dto.ChangePasswordDTO;
import com.isa.domain.dto.UserDTO;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.Hospital;
import com.isa.domain.model.User;
import com.isa.domain.model.VerificationToken;
import com.isa.enums.AppointmentStatus;
import com.isa.enums.Gender;
import com.isa.enums.Role;
import com.isa.repository.HospitalRepository;
import com.isa.repository.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private static final Logger LOG = LoggerFactory.getLogger(UserService.class);

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final HospitalRepository hospitalRepository;
    private final AppointmentService appointmentService;
    private final VerificationTokenService verificationTokenService;
    private final EmailService emailService;

    @Autowired
    public UserService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       HospitalRepository hospitalRepository,
                       AppointmentService appointmentService,
                       VerificationTokenService verificationTokenService,
                       EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.hospitalRepository = hospitalRepository;
        this.appointmentService = appointmentService;
        this.verificationTokenService = verificationTokenService;
        this.emailService = emailService;
    }

    @Transactional
    public User register(UserDTO userDTO) {
        if (userRepository.findOneByEmail(userDTO.getEmail()).isPresent()) {
            throw new IllegalArgumentException("A user with this email already exists.");
        }

        final User user = new User();
        user.setEmail(userDTO.getEmail());
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole(userDTO.getRole());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setCountry(userDTO.getCountry());
        user.setPhone(userDTO.getPhone());
        user.setGender(Gender.valueOf(userDTO.getGender()));
        user.setOccupationInfo(userDTO.getOccupationInfo());
        user.setOccupation(userDTO.getOccupation());
        user.setPersonalId(userDTO.getPersonalId());
        user.setVerified(false);

        if (userDTO.getHospitalId() != null) {
            final Hospital hospital = hospitalRepository.findById(userDTO.getHospitalId())
                    .orElseThrow(() -> new IllegalArgumentException("Hospital not found."));
            user.setHospital(hospital);
        }

        final User saved = userRepository.save(user);

        try {
            final VerificationToken token = verificationTokenService.createToken(saved);
            emailService.sendVerificationEmail(saved.getEmail(), token.getToken());
        } catch (Exception e) {
            LOG.error("Failed to send verification email to {}: {}", saved.getEmail(), e.getMessage(), e);
        }

        return saved;
    }

    public List<User> list(String name) {
        return name != null ? userRepository.findAllByName(name) : userRepository.findAll();
    }

    public void delete(User user) {
        userRepository.delete(user);
    }

    public List<User> getAllByHospital(Hospital hospital, Role role, String name) {
        return role != null
                ? userRepository.findAllByHospitalIdAndRole(hospital.getId(), role, name)
                : userRepository.findAllByHospitalId(hospital.getId(), name);
    }

    public Optional<User> get(long userId) {
        return userRepository.findById(userId);
    }

    public User changePassword(ChangePasswordDTO changePasswordDTO, User user) {
        if (user == null) {
            return null;
        }
        user.setPassword(passwordEncoder.encode(changePasswordDTO.getPassword()));
        return userRepository.save(user);
    }

    public User updateProfile(UserDTO userDTO, User user) {
        user.setFirstName(userDTO.getFirstName());
        user.setLastName(userDTO.getLastName());
        user.setAddress(userDTO.getAddress());
        user.setCity(userDTO.getCity());
        user.setCountry(userDTO.getCountry());
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
