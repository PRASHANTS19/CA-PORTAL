package CA_Portal.CA_Portal.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {

    Optional<UserEntity> findByEmail(String email);

    List<UserEntity> findByOrganizationId(Long orgId);

    List<UserEntity> findByUserType(UserType userType);

    List<UserEntity> findByIsActive(Boolean isActive);

    @Query("SELECT u FROM UserEntity u WHERE u.organization.id = ?1 AND u.userType = ?2")
    List<UserEntity> findByOrganizationIdAndUserType(Long orgId, UserType userType);

    @Query("SELECT u FROM UserEntity u WHERE u.organization.id = ?1 AND u.isActive = ?2")
    List<UserEntity> findByOrganizationIdAndIsActive(Long orgId, Boolean isActive);

    // For org_id compatibility
    default List<UserEntity> findByOrgId(Long orgId) {
        return findByOrganizationId(orgId);
    }
}
