package dk.kea.project.api;

import dk.kea.project.dto.ProductResponse;
import dk.kea.project.dto.StoreResponse;
import dk.kea.project.service.SallingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/stores")
public class StoreController {
    SallingService sallingService;
    public StoreController(SallingService sallingService) {
        this.sallingService = sallingService;
    }
    @GetMapping
    public List<StoreResponse> getStores(@RequestParam int zipcode){
        return sallingService.getStores(zipcode);
    }
}
