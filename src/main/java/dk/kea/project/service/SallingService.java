package dk.kea.project.service;

import dk.kea.project.dto.ProductResponse;
import dk.kea.project.dto.StoreResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.ArrayList;
import java.util.List;

@Service
public class SallingService{

    private final static String SALLING_API_URL_V1 = "https://api.sallinggroup.com/v1";
    private final static String SALLING_API_URL_V2 = "https://api.sallinggroup.com/v2";

    @Value("${SALLING_API_KEY}")
    private String SALLING_API_KEY;

    private WebClient webClient;

    public SallingService() {
        this.webClient = WebClient.create();
    }
    public List<StoreResponse> getStores(int zipcode){
        List<StoreResponse> stores = new ArrayList<>();
        var res = webClient.method(HttpMethod.GET)
                .uri(SALLING_API_URL_V2 + "/stores?zip="+ zipcode)
                .header("Authorization", "Bearer " + SALLING_API_KEY)
                .retrieve()
                .bodyToFlux(StoreResponse.class)
                .collectList()
                .doOnError(e -> System.out.println(e.getMessage()));
        res.subscribe(storeResponses -> {
            for(StoreResponse response : storeResponses){
                stores.add(response);
            }
        });
        return stores;
    }
    public List<ProductResponse> getFoodWaste(String id){
        List<ProductResponse> products = new ArrayList<>();
        var res = webClient.method(HttpMethod.GET)
                .uri(SALLING_API_URL_V1 + "/food-waste/" + id)
                .header("Authorization", "Bearer " + SALLING_API_KEY)
                .retrieve()
                .bodyToFlux(ProductResponse.class)
                .collectList()
                .doOnError(e -> System.out.println(e.getMessage()));

        res.subscribe(productResponse -> {
            for(ProductResponse response : productResponse){
                System.out.println(response);
                products.add(response);
            }
        });
        return products;
    }
}
