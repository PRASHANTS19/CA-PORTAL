package CA_Portal.CA_Portal.user;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Transactional
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<UserEntity> findAllUsers() {
        return userRepository.findAll();
    }

    public Optional<UserEntity> findUserById(Long id) {
        return userRepository.findById(id);
    }

    public List<UserEntity> findUserByOrgId(Long orgId) {
        return userRepository.findByOrgId(orgId);
    }

    public List<UserEntity> findUserByUserType(UserType userType) {
        return userRepository.findByUserType(userType);
    }

    public Optional<UserEntity> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public UserEntity createUser(UserEntity user) {
        // Validate email uniqueness
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + user.getEmail());
        }

        // Hash password for internal users
        if (user.getUserType() == UserType.INTERNAL_USER && user.getPasswordHash() != null) {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }

        return userRepository.save(user);
    }

    public UserEntity updateUser(Long id, UserEntity userDetails) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        // Update basic fields
        if (userDetails.getName() != null) {
            user.setName(userDetails.getName());
        }

        if (userDetails.getEmail() != null && !userDetails.getEmail().equals(user.getEmail())) {
            // Check email uniqueness
            if (userRepository.findByEmail(userDetails.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists: " + userDetails.getEmail());
            }
            user.setEmail(userDetails.getEmail());
        }

        if (userDetails.getPhone() != null) {
            user.setPhone(userDetails.getPhone());
        }

        if (userDetails.getAddress() != null) {
            user.setAddress(userDetails.getAddress());
        }

        if (userDetails.getRole() != null) {
            user.setRole(userDetails.getRole());
        }

        if (userDetails.getIsActive() != null) {
            user.setIsActive(userDetails.getIsActive());
        }

        // Update fields specific to INTERNAL_USER
        if (user.getUserType() == UserType.INTERNAL_USER) {
            if (userDetails.getFirstName() != null) {
                user.setFirstName(userDetails.getFirstName());
            }

            if (userDetails.getLastName() != null) {
                user.setLastName(userDetails.getLastName());
            }

            // Update password only if provided and hash it
            if (userDetails.getPasswordHash() != null && !userDetails.getPasswordHash().isEmpty()) {
                user.setPasswordHash(passwordEncoder.encode(userDetails.getPasswordHash()));
            }
        }

        // Update fields specific to CUSTOMER
        if (user.getUserType() == UserType.USER) {
            if (userDetails.getGstin() != null) {
                user.setGstin(userDetails.getGstin());
            }
        }

        return userRepository.save(user);
    }

    public UserEntity partialUpdateUser(Long id, Map<String, Object> updates) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    user.setName((String) value);
                    break;
                case "email":
                    String newEmail = (String) value;
                    if (!newEmail.equals(user.getEmail()) &&
                            userRepository.findByEmail(newEmail).isPresent()) {
                        throw new IllegalArgumentException("Email already exists: " + newEmail);
                    }
                    user.setEmail(newEmail);
                    break;
                case "phone":
                    user.setPhone((String) value);
                    break;
                case "address":
                    user.setAddress((String) value);
                    break;
                case "firstName":
                    if (user.getUserType() == UserType.INTERNAL_USER) {
                        user.setFirstName((String) value);
                    }
                    break;
                case "lastName":
                    if (user.getUserType() == UserType.INTERNAL_USER) {
                        user.setLastName((String) value);
                    }
                    break;
                case "role":
                    user.setRole((String) value);
                    break;
                case "isActive":
                    user.setIsActive((Boolean) value);
                    break;
                case "gstin":
                    if (user.getUserType() == UserType.USER) {
                        user.setGstin((String) value);
                    }
                    break;
                case "password":
                    if (user.getUserType() == UserType.INTERNAL_USER) {
                        user.setPasswordHash(passwordEncoder.encode((String) value));
                    }
                    break;
            }
        });

        return userRepository.save(user);
    }

    public void deleteUser(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    public List<UserEntity> findActiveUsers() {
        return userRepository.findByIsActive(true);
    }

    public List<UserEntity> findInternalUsersByOrg(Long orgId) {
        return userRepository.findByOrganizationIdAndUserType(orgId, UserType.INTERNAL_USER);
    }

    public List<UserEntity> findCustomersByOrg(Long orgId) {
        return userRepository.findByOrganizationIdAndUserType(orgId, UserType.USER);
    }

    public boolean validatePassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }
}
