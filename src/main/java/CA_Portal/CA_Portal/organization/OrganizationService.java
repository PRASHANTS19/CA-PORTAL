package CA_Portal.CA_Portal.organization;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@Transactional
public class OrganizationService {

    @Autowired
    private OrganizationRepository organizationRepository;

    public List<OrganizationEntity> findAllOrganizations() {
        return organizationRepository.findAll();
    }

    public Optional<OrganizationEntity> findOrganizationById(Long id) {
        return organizationRepository.findById(id);
    }

    public Optional<OrganizationEntity> findOrganizationByEmail(String email) {
        return organizationRepository.findByEmail(email);
    }

    public List<OrganizationEntity> findActiveOrganizations() {
        return organizationRepository.findByIsActive(true);
    }

    public List<OrganizationEntity> searchOrganizationsByName(String name) {
        return organizationRepository.findByNameContainingIgnoreCase(name);
    }

    public OrganizationEntity createOrganization(OrganizationEntity organization) {
        // Validate email uniqueness
        if (organizationRepository.findByEmail(organization.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already exists: " + organization.getEmail());
        }

        // Set default values if not provided
        if (organization.getIsActive() == null) {
            organization.setIsActive(true);
        }

        return organizationRepository.save(organization);
    }

    public OrganizationEntity updateOrganization(Long id, OrganizationEntity organizationDetails) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));

        // Update basic fields
        if (organizationDetails.getName() != null) {
            organization.setName(organizationDetails.getName());
        }

        if (organizationDetails.getEmail() != null &&
                !organizationDetails.getEmail().equals(organization.getEmail())) {
            // Check email uniqueness
            if (organizationRepository.findByEmail(organizationDetails.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists: " + organizationDetails.getEmail());
            }
            organization.setEmail(organizationDetails.getEmail());
        }

        if (organizationDetails.getPhone() != null) {
            organization.setPhone(organizationDetails.getPhone());
        }

        if (organizationDetails.getAddress() != null) {
            organization.setAddress(organizationDetails.getAddress());
        }

        if (organizationDetails.getLogoUrl() != null) {
            organization.setLogoUrl(organizationDetails.getLogoUrl());
        }

        if (organizationDetails.getIsActive() != null) {
            organization.setIsActive(organizationDetails.getIsActive());
        }

        return organizationRepository.save(organization);
    }

    public OrganizationEntity partialUpdateOrganization(Long id, Map<String, Object> updates) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));

        updates.forEach((key, value) -> {
            switch (key) {
                case "name":
                    organization.setName((String) value);
                    break;
                case "email":
                    String newEmail = (String) value;
                    if (!newEmail.equals(organization.getEmail()) &&
                            organizationRepository.findByEmail(newEmail).isPresent()) {
                        throw new IllegalArgumentException("Email already exists: " + newEmail);
                    }
                    organization.setEmail(newEmail);
                    break;
                case "phone":
                    organization.setPhone((String) value);
                    break;
                case "address":
                    organization.setAddress((String) value);
                    break;
                case "logoUrl":
                    organization.setLogoUrl((String) value);
                    break;
                case "isActive":
                    organization.setIsActive((Boolean) value);
                    break;
            }
        });

        return organizationRepository.save(organization);
    }

    public void deleteOrganization(Long id) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        organizationRepository.delete(organization);
    }

    public Long countActiveOrganizations() {
        return organizationRepository.countActiveOrganizations();
    }

    public OrganizationEntity activateOrganization(Long id) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        organization.setIsActive(true);
        return organizationRepository.save(organization);
    }

    public OrganizationEntity deactivateOrganization(Long id) {
        OrganizationEntity organization = organizationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Organization not found with id: " + id));
        organization.setIsActive(false);
        return organizationRepository.save(organization);
    }
}

