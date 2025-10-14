package CA_Portal.CA_Portal.organization;


import CA_Portal.CA_Portal.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import java.util.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;

@Entity
@Table(name = "organizations")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"users"})
@EqualsAndHashCode(exclude = {"users"})
public class    OrganizationEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false, length = 255)
    private String name;

    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @Column(length = 15)
    private String phone;

    @Column(columnDefinition = "Text")
    private String address;

    @Column(name = "logo_url", length = 500)
    private String logoUrl;

    @Column(name = "is_active")
    private boolean isActive;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private String createdAt;

    @CreationTimestamp
    @Column(name = "updated_at", updatable = false)
    private String updatedAt;

    @OneToMany(mappedBy = "organizations", cascade = CascadeType.ALL)
    @Builder.Default
    private List<UserEntity> users = new ArrayList<>();

}
