package CA_Portal.CA_Portal.organization;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationEntity, Long> {

    Optional<OrganizationEntity> findByEmail(String email);

    List<OrganizationEntity> findByIsActive(Boolean isActive);

    List<OrganizationEntity> findByNameContainingIgnoreCase(String name);

    @Query("SELECT o FROM OrganizationEntity o WHERE o.isActive = true")
    List<OrganizationEntity> findAllActiveOrganizations();

    @Query("SELECT COUNT(o) FROM OrganizationEntity o WHERE o.isActive = true")
    Long countActiveOrganizations();
}
