package dk.kea.project.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ProductResponse {
    public List<Clearance> clearances;

//    public String toString(){
//        return clearances.get(0).product.description;
//    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Clearance{
        public Offer offer;
        public Product product;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Offer{
        public double originalPrice;
        public double newPrice;
        public double discount;
        public double percentDiscount;
    }
    @Getter
    @Setter
    @NoArgsConstructor
    public static class Product{
        public String description;
        public String ean;
        public String image;
    }
}
