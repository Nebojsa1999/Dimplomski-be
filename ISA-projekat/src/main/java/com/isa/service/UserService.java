package com.isa.service;

import com.isa.domain.dto.ChangePasswordDTO;
import com.isa.domain.dto.UserDTO;
import com.isa.domain.model.Appointment;
import com.isa.domain.model.CenterAccount;
import com.isa.domain.model.User;
import com.isa.enums.Gender;
import com.isa.enums.Role;
import com.isa.repository.CenterAccountRepository;
import com.isa.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private CenterAccountRepository centerAccountRepository;

    @Autowired
    private AppointmentService appointmentService;

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
        final CenterAccount centerAccount = new CenterAccount();
        centerAccount.setAddress("Address");
        centerAccount.setName("Name");
        centerAccount.setCity("City");
        centerAccount.setDescription("Description");
        centerAccount.setCountry("Country");
        centerAccount.setLatitude(41);
        centerAccount.setLongitude(45);
        centerAccount.setStartTime(LocalTime.MIDNIGHT);
        centerAccount.setEndTime(LocalTime.NOON);

        centerAccountRepository.save(centerAccount);

        user.setCenterAccount(centerAccount);

        return userRepository.save(user);
    }

    public List<User> getAllByCenterAccount(CenterAccount centerAccount) {
        return userRepository.findAllByCenterAccountId(centerAccount.getId());
    }

    public Role stringToRole(String role) {

        return Role.valueOf(role);
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

    public List<User> findAllByRole(Role role) {
        return userRepository.findAllByRole(role);
    }

    public List<User> search(String term, Role role) {
        return userRepository.findAllByRoleAndFirstNameContainsOrLastNameContaining(role, term, term);
    }

    @Transactional
    public void lowerUserPoints(Appointment appointment) {
        final User patient = appointment.getPatient();
        patient.setPoints(patient.getPoints() - 1);
        userRepository.save(patient);
        appointment.setPatient(null);
        appointmentService.save(appointment);
    }
}
