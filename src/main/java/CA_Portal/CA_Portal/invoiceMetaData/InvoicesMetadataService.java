package CA_Portal.CA_Portal.invoiceMetaData;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
public class InvoicesMetadataService {

    @Autowired
    private InvoicesMetadataRepository invoicesMetadataRepository;

    public List<InvoicesMetadataEntity> getAllMetadata() {
        return invoicesMetadataRepository.findAll();
    }

    public Optional<InvoicesMetadataEntity> getMetadataById(Long id){
        return invoicesMetadataRepository.findById(id);
    }

    public List<InvoicesMetadataEntity> getMetadataBySalesId(Long salesId) {
        return invoicesMetadataRepository.findBySalesId(salesId);
    }

    public List<InvoicesMetadataEntity> getMetadataByExpenseId(Long expenseId) {
        return invoicesMetadataRepository.findByExpenseId(expenseId);
    }

    public InvoicesMetadataEntity createMetadata(InvoicesMetadataEntity metadata) {
        metadata.setCreatedAt(java.time.LocalDateTime.now());
        metadata.setUpdatedAt(java.time.LocalDateTime.now());
        return invoicesMetadataRepository.save(metadata);
    }

    public InvoicesMetadataEntity updateMetadata(Long id, InvoicesMetadataEntity metadataDetails) {
        InvoicesMetadataEntity metadata = invoicesMetadataRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Metadata not found with id: " + id));

        metadata.setDescription(metadataDetails.getDescription());
        metadata.setQuantity(metadataDetails.getQuantity());
        metadata.setUnitPrice(metadataDetails.getUnitPrice());
        metadata.setTaxRate(metadataDetails.getTaxRate());
        metadata.setAmount(metadataDetails.getAmount());
        metadata.setUpdatedAt(java.time.LocalDateTime.now());

        return invoicesMetadataRepository.save(metadata);
    }

    public void deleteMetadata(Long id) {
        invoicesMetadataRepository.deleteById(id);
    }


}
