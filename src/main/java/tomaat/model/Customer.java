package tomaat.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

@Component
@Getter
@Setter
public class Customer {

    private String name;
    private String email;
    private Long mobile;
    private String id;
}
