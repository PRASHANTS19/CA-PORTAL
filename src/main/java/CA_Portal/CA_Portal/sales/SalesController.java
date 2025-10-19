package CA_Portal.CA_Portal.sales;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("api/v1/sales")
@CrossOrigin(origins = "*")
public class SalesController {

    @Autowired
    private SalesService salesService;

    @GetMapping
    public ResponseEntity<List<SalesEntity>>getAllSales(){
        return ResponseEntity.ok(salesService.getAllSales());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesEntity> getSaleByID(@PathVariable Long id){
        return salesService.getSalesById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
    @GetMapping("/user/{userId}")
    public  ResponseEntity<List<SalesEntity>>getSalesByUserId(@PathVariable Long userId){
        return ResponseEntity.ok(salesService.getSalesByUserID(userId));
    }

    @PostMapping
    public ResponseEntity<SalesEntity>setSale(@RequestBody SalesEntity sales){
        SalesEntity created = salesService.createSales(sales);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    @PutMapping("/{id}")
    public ResponseEntity<SalesEntity> updateSales(@PathVariable Long id, @RequestBody SalesEntity sales){
        SalesEntity updated = salesService.updateSales(id, sales);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSales(@PathVariable Long id) {
        salesService.deleteSales(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<SalesEntity>> getSalesByStatus(@PathVariable SalesStatus status) {
        return ResponseEntity.ok(salesService.getSalesByStatus(status));
    }
}

