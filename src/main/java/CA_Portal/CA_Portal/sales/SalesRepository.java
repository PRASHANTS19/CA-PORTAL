package CA_Portal.CA_Portal.sales;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.*;

@Repository
public interface SalesRepository extends JpaRepository<SalesEntity, Long> {
    List<SalesEntity>findByUser_Id(Long userId);
    List<SalesEntity>findByStatus(SalesStatus status);
    Optional<SalesEntity>findByInvoiceNumber(String invoiceNumber);

    List<SalesEntity> findByUserIdAndStatus(Long userId, SalesStatus status);
}
