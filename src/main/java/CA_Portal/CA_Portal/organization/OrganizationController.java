package CA_Portal.CA_Portal.organization;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/organizations")
@CrossOrigin(origins = "*")
public class OrganizationController {

    @Autowired
    private OrganizationService organizationService;

    @GetMapping
    public ResponseEntity<List<OrganizationEntity>> getAllOrganizations() {
        List<OrganizationEntity> organizations = organizationService.findAllOrganizations();
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrganizationEntity> getOrganizationById(@PathVariable Long id) {
        return organizationService.findOrganizationById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<OrganizationEntity> getOrganizationByEmail(@PathVariable String email) {
        return organizationService.findOrganizationByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/active")
    public ResponseEntity<List<OrganizationEntity>> getActiveOrganizations() {
        List<OrganizationEntity> organizations = organizationService.findActiveOrganizations();
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OrganizationEntity>> searchOrganizations(
            @RequestParam String name) {
        List<OrganizationEntity> organizations = organizationService.searchOrganizationsByName(name);
        return ResponseEntity.ok(organizations);
    }

    @GetMapping("/count/active")
    public ResponseEntity<Map<String, Long>> countActiveOrganizations() {
        Long count = organizationService.countActiveOrganizations();
        Map<String, Long> response = new HashMap<>();
        response.put("activeCount", count);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<OrganizationEntity> createOrganization(
            @Valid @RequestBody OrganizationEntity organization) {
        try {
            OrganizationEntity createdOrganization = organizationService.createOrganization(organization);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdOrganization);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrganizationEntity> updateOrganization(
            @PathVariable Long id,
            @Valid @RequestBody OrganizationEntity organization) {
        try {
            OrganizationEntity updatedOrganization = organizationService.updateOrganization(id, organization);
            return ResponseEntity.ok(updatedOrganization);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{id}")
    public ResponseEntity<OrganizationEntity> partialUpdateOrganization(
            @PathVariable Long id,
            @RequestBody Map<String, Object> updates) {
        try {
            OrganizationEntity updatedOrganization = organizationService.partialUpdateOrganization(id, updates);
            return ResponseEntity.ok(updatedOrganization);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Boolean>> deleteOrganization(@PathVariable Long id) {
        try {
            organizationService.deleteOrganization(id);
            Map<String, Boolean> response = new HashMap<>();
            response.put("deleted", Boolean.TRUE);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/activate")
    public ResponseEntity<OrganizationEntity> activateOrganization(@PathVariable Long id) {
        try {
            OrganizationEntity organization = organizationService.activateOrganization(id);
            return ResponseEntity.ok(organization);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("/{id}/deactivate")
    public ResponseEntity<OrganizationEntity> deactivateOrganization(@PathVariable Long id) {
        try {
            OrganizationEntity organization = organizationService.deactivateOrganization(id);
            return ResponseEntity.ok(organization);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
