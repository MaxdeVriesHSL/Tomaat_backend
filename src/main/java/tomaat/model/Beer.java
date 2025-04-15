package tomaat.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.cloud.firestore.annotation.Exclude;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Map;
import java.util.UUID;

@Entity
@Table(name = "beers")
@Getter
@Setter
@NoArgsConstructor
public class Beer {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;
    private String description;

    // Store the beer type ID as a reference
    private String beerTypeId;

    // Type name for backward compatibility and ease of querying
    private String type;

    private double alcoholPercentage;
    private double price;
    private int stockQuantity;
    private String imageUrl;

    @JsonIgnore
    private Map<String, Long> uuidMap;

    @Exclude
    public UUID getUUID() {
        try {
            return id != null ? UUID.fromString(id) : null;
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid UUID format in id field", e);
        }
    }

    @Exclude
    public void setUUID(UUID uuid) {
        this.id = uuid != null ? uuid.toString() : null;
    }

    @Exclude
    public UUID getBeerTypeUUID() {
        try {
            return beerTypeId != null ? UUID.fromString(beerTypeId) : null;
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    @Exclude
    public void setBeerTypeUUID(UUID uuid) {
        this.beerTypeId = uuid != null ? uuid.toString() : null;
    }

    public Beer(String name, String description, String type, String beerTypeId,
                double alcoholPercentage, double price, int stockQuantity, String imageUrl) {
        this.name = name;
        this.description = description;
        this.type = type;
        this.beerTypeId = beerTypeId;
        this.alcoholPercentage = alcoholPercentage;
        this.price = price;
        this.stockQuantity = stockQuantity;
        this.imageUrl = imageUrl;
    }
}