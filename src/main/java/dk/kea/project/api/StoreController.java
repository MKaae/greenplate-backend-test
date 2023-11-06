package dk.kea.project.api;

import dk.kea.project.dto.ProductResponse;
import dk.kea.project.dto.StoreResponse;
import dk.kea.project.service.SallingService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
    public List<StoreResponse> getStores(){
        return sallingService.getStores(8000);
    }
    @GetMapping("/foodwaste")
    public List<ProductResponse> getFoodWaste(){
        return sallingService.getFoodWaste("efba0457-090e-4132-81ba-c72b4c8e7fee");
    }
}
