package CA_Portal.CA_Portal.user;


import CA_Portal.CA_Portal.expenses.ExpensesEntity;
import CA_Portal.CA_Portal.organization.OrganizationEntity;
import CA_Portal.CA_Portal.sales.SalesEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import jakarta.validation.constraints.*;


@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"organizations", "sales", "expenses"})
@EqualsAndHashCode(exclude = {"organizations", "sales", "expenses"})
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "org_id", nullable = false)
    private OrganizationEntity organization;

    @Enumerated(EnumType.STRING)
    @Column(name = "column_type", nullable = false, length = 50)
    private UserType userType;

    // Add these to relevant fields:
    @NotBlank(message = "Email is required")
    @Email(message = "Email should be valid")
    @Column(nullable = false, unique = true, length = 255)
    private String email;

    @NotBlank(message = "Name is required")
    @Size(min = 2, max = 255, message = "Name must be between 2 and 255 characters")
    @Column(nullable = false, length = 255)
    private String name;

    @Pattern(regexp = "^[0-9]{10,15}$", message = "Phone number should be valid")
    @Column(length = 50)
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String address;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(name = "password_hash", length = 255)
    private String passwordHash;

    @Column(name = "first_name", length = 100)
    private String firstName;

    @Column(name = "last_name", length = 100)
    private String lastName;

    @Column(length = 50)
    private String role = "USER";

    @Column(name = "is_active")
    private Boolean isActive = true;

    @Column(length = 50)
    private String gstin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<SalesEntity>sale = new ArrayList<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @Builder.Default
    private List<ExpensesEntity> expenses = new ArrayList<>();

    public boolean isInternalUser() {
        return userType == UserType.INTERNAL_USER;
    }

    public boolean isCustomer() {
        return userType == UserType.USER;
    }

    public String getFullName() {
        if (isInternalUser() && firstName != null) {
            return lastName != null ? firstName + " " + lastName : firstName;
        }
        return name;
    }

    @PrePersist
    @PreUpdate
    protected void validateUserType() {
        if (userType == UserType.INTERNAL_USER) {
            if (passwordHash == null || firstName == null) {
                throw new IllegalStateException(
                        "Internal users must have password_hash and first_name"
                );
            }
        }
    }

}
