package CA_Portal.CA_Portal.sales;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
@Transactional
public class SalesService {

    @Autowired
    private SalesRepository salesRepository;

    public List<SalesEntity> getAllSales(){
        return salesRepository.findAll();
    }

    public Optional<SalesEntity> getSalesById(Long id){
        return salesRepository.findById(id);
    }

    public List<SalesEntity>getSalesByUserID(Long UserID){
        return salesRepository.findByUser_Id(UserID);
    }
    public List<SalesEntity>getSalesByStatus(SalesStatus status){
        return salesRepository.findByStatus(status);
    }

    public SalesEntity createSales(SalesEntity sales){
        sales.setCreatedAt(java.time.LocalDateTime.now());
        sales.setUpdatedAt(java.time.LocalDateTime.now());
        return salesRepository.save(sales);
    }
    public SalesEntity updateSales(Long id, SalesEntity salesDetails){
        SalesEntity sales = salesRepository.findById(id)
                .orElseThrow(()-> new RuntimeException("Sales not found with id: "+ id));

        sales.setInvoiceNumber(salesDetails.getInvoiceNumber());
        sales.setIssueDate(salesDetails.getIssueDate());
        sales.setDueDate(salesDetails.getDueDate());
        sales.setSubtotal(salesDetails.getSubtotal());
        sales.setTaxAmount(salesDetails.getTaxAmount());
        sales.setTotalAmount(salesDetails.getTotalAmount());
        sales.setPaidAmount(salesDetails.getPaidAmount());
        sales.setStatus(salesDetails.getStatus());
        sales.setUpdatedAt(java.time.LocalDateTime.now());

        return salesRepository.save(sales);
    }
    public void deleteSales(Long id){
        salesRepository.deleteById(id);
    }

}
