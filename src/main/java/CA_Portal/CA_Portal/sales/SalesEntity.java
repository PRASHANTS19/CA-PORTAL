package CA_Portal.CA_Portal.sales;

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
@Table(name = "sales")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"customer", "lineItems"})
@EqualsAndHashCode(exclude = {"customer", "lineItems"})

public class SalesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false )
    private UserEntity customer;


    @Column(name = "invoice_number", nullable = false, length = 100)
    private String invoiceNumber;

    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal subtotal = BigDecimal.ZERO;

    @Column(name = "tax_amount", precision = 15, scale = 2)
    private BigDecimal taxAmount = BigDecimal.ZERO;

    @Column(name = "total_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "paid_amount", precision = 15, scale = 2)
    private BigDecimal paidAmount = BigDecimal.ZERO;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private SalesStatus status = SalesStatus.DRAFT;

    @Column(name = "pdf_url", length = 500)
    private String pdfUrl;

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Relationships
    @OneToMany(mappedBy = "sales", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<InvoicesMetadataEntity> lineItems = new ArrayList<>();

    // Helper methods
    public void addLineItem(InvoicesMetadataEntity item) {
        lineItems.add(item);
        item.setSales(this);
    }

    public void removeLineItem(InvoicesMetadataEntity item) {
        lineItems.remove(item);
        item.setSales(null);
    }

    public BigDecimal getOutstandingAmount() {
        return totalAmount.subtract(paidAmount);
    }

    public boolean isOverdue() {
        return dueDate.isBefore(LocalDate.now()) &&
                status != SalesStatus.PAID;
    }

    public boolean isFullyPaid() {
        return paidAmount.compareTo(totalAmount) >= 0;
    }

    public void recalculateTotals() {
        subtotal = lineItems.stream()
                .map(item -> item.getUnitPrice().multiply(item.getQuantity()))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        taxAmount = lineItems.stream()
                .map(InvoicesMetadataEntity::getTaxAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        totalAmount = subtotal.add(taxAmount);
    }
}
