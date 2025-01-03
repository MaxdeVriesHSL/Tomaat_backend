package tomaat.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.UUID;

@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    private String name;
    private String email;
    private Role role;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    public UUID getUUID() {
        return id != null ? UUID.fromString(id) : null;
    }

    public void setUUID(UUID uuid) {
        this.id = uuid != null ? uuid.toString() : null;
    }
}