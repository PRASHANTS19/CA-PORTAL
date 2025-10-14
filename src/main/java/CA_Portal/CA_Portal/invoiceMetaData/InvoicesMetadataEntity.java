package CA_Portal.CA_Portal.invoiceMetaData;

import CA_Portal.CA_Portal.expenses.ExpensesEntity;
import CA_Portal.CA_Portal.sales.SalesEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDateTime;
@Entity
@Table(name = "invoices_metadata")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"sales", "expense"})
@EqualsAndHashCode(exclude = {"sales", "expense"})
public class InvoicesMetadataEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_id")
    private SalesEntity sales;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "expense_id")
    private ExpensesEntity expense;

    @Column(name = "invoice_number", nullable = false, length = 100)
    private String invoiceNumber;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity = BigDecimal.ONE;

    @Column(name = "unit_price", nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;

    @Column(name = "tax_rate", precision = 5, scale = 2)
    private BigDecimal taxRate = BigDecimal.ZERO;

    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public boolean isSalesLineItem() {
        return sales != null && expense == null;
    }

    public boolean isExpenseLineItem() {
        return expense != null && sales == null;
    }

    public BigDecimal getLineTotal() {
        return unitPrice.multiply(quantity);
    }

    public BigDecimal getTaxAmount() {
        BigDecimal lineTotal = getLineTotal();
        return lineTotal.multiply(taxRate).divide(new BigDecimal("100"));
    }

    public void calculateAmount() {
        BigDecimal lineTotal = getLineTotal();
        BigDecimal tax = getTaxAmount();
        this.amount = lineTotal.add(tax);
    }

    @PrePersist
    @PreUpdate
    protected void validate() {
        // Ensure only one parent is set
        if ((sales != null && expense != null) || (sales == null && expense == null)) {
            throw new IllegalStateException(
                    "InvoiceMetadata must have exactly one parent: either sales or expense"
            );
        }

        // Auto-calculate amount if not set
        if (amount == null) {
            calculateAmount();
        }
    }

    public void setSales(SalesEntity salesEntity) {
    }
}
