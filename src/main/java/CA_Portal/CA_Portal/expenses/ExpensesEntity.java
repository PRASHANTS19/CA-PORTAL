package CA_Portal.CA_Portal.expenses;
import CA_Portal.CA_Portal.invoiceMetaData.InvoicesMetadataEntity;
import CA_Portal.CA_Portal.user.UserEntity;
import jakarta.persistence.*;
        import lombok.*;
        import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
@Entity
@Table(name = "expenses")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"user", "lineItems"})
@EqualsAndHashCode(exclude = {"user", "lineItems"})
public class ExpensesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "invoice_number", nullable = false, length = 100)
    private String invoiceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @Column(length = 255)
    private String merchant;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 100)
    private ExpenseCategory category = ExpenseCategory.OTHER;

    @Column(name = "receipt_url", length = 500)
    private String receiptUrl;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(name = "payment_method", length = 50)
    private String paymentMethod;

    @Column(name = "ocr_extracted")
    private Boolean ocrExtracted = false;

    @Column(name = "ocr_confidence", precision = 5, scale = 2)
    private BigDecimal ocrConfidence;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "expense", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoicesMetadataEntity> lineItems = new ArrayList<>();

    // Helper methods
    public void addLineItem(InvoicesMetadataEntity item) {
        lineItems.add(item);
        item.setExpense(this);
    }

    public void removeLineItem(InvoicesMetadataEntity item) {
        lineItems.remove(item);
        item.setExpense(null);
    }

    public void recalculateAmount() {
        amount = lineItems.stream()
                .map(InvoicesMetadataEntity::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
