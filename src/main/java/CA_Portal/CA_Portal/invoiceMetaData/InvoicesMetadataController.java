package CA_Portal.CA_Portal.invoiceMetaData;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.*;

@RestController
@RequestMapping("/api/v1/invoice-metadata")
@CrossOrigin(origins = "*")
public class InvoicesMetadataController {
    @Autowired
    private InvoicesMetadataService metadataService;

    @GetMapping
    public ResponseEntity<List<InvoicesMetadataEntity>> getAllMetadata() {
        return ResponseEntity.ok(metadataService.getAllMetadata());
    }

    @GetMapping("/{id}")
    public ResponseEntity<InvoicesMetadataEntity> getMetadataById(@PathVariable Long id) {
        return metadataService.getMetadataById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/sales/{salesId}")
    public ResponseEntity<List<InvoicesMetadataEntity>> getMetadataBySalesId(@PathVariable Long salesId) {
        return ResponseEntity.ok(metadataService.getMetadataBySalesId(salesId));
    }

    @GetMapping("/expense/{expenseId}")
    public ResponseEntity<List<InvoicesMetadataEntity>> getMetadataByExpenseId(@PathVariable Long expenseId) {
        return ResponseEntity.ok(metadataService.getMetadataByExpenseId(expenseId));
    }

    @PostMapping
    public ResponseEntity<InvoicesMetadataEntity> createMetadata(@RequestBody InvoicesMetadataEntity metadata) {
        InvoicesMetadataEntity created = metadataService.createMetadata(metadata);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InvoicesMetadataEntity> updateMetadata(
            @PathVariable Long id,
            @RequestBody InvoicesMetadataEntity metadata) {
        InvoicesMetadataEntity updated = metadataService.updateMetadata(id, metadata);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMetadata(@PathVariable Long id) {
        metadataService.deleteMetadata(id);
        return ResponseEntity.noContent().build();
    }
}
