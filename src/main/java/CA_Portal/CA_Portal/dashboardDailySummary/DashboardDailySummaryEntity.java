package CA_Portal.CA_Portal.dashboardDailySummary;

import CA_Portal.CA_Portal.user.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "dashboard_daily_summary")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = {"customer"})
@EqualsAndHashCode(exclude = {"customer"})
public class DashboardDailySummaryEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customers_id", nullable = false)
    private UserEntity customer;

    @Column(nullable = false)
    private LocalDate date;

    @Column(name = "total_income", precision = 15, scale = 2)
    private BigDecimal totalIncome = BigDecimal.ZERO;

    @Column(name = "total_expenses", precision = 15, scale = 2)
    private BigDecimal totalExpenses = BigDecimal.ZERO;

    @Column(name = "net_cashflow", precision = 15, scale = 2)
    private BigDecimal netCashflow = BigDecimal.ZERO;

    @Column(name = "invoice_count")
    private Integer invoiceCount = 0;

    @Column(name = "expense_count")
    private Integer expenseCount = 0;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    // Helper methods
    public void calculateNetCashflow() {
        this.netCashflow = totalIncome.subtract(totalExpenses);
    }

    @PrePersist
    @PreUpdate
    protected void onSave() {
        calculateNetCashflow();
    }
}

