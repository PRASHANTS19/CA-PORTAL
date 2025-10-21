package CA_Portal.CA_Portal.invoiceMetaData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public interface InvoicesMetadataRepository extends JpaRepository<InvoicesMetadataEntity, Long> {
    List<InvoicesMetadataEntity> findBySalesId(Long salesId);
    List<InvoicesMetadataEntity> findByExpenseId(Long expenseId);
    List<InvoicesMetadataEntity> findByInvoiceNumber(String invoiceNumber);
}
