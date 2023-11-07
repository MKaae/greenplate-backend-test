package dk.kea.project.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StoreResponse {
    private String id;
    private String brand;
    private String name;
    private Address address;

    @Getter
    @Setter
    @NoArgsConstructor
    private static class Address{
        public String city;
        public String street;
        public int zip;
    }
}
