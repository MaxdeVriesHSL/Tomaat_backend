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
@Table(name = "beertypes")
@Getter
@Setter
@NoArgsConstructor
public class BeerType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;

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

    public BeerType(String name) {
        this.name = name;
    }
}