package tomaat.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@Service
public class FirebaseConfig {

    @PostConstruct
    public void configureFirebaseConnection() throws IOException {

        File file = new File("src/main/java/tomaat/config/iprwc-max-firebase-adminsdk-ltwlx-80eb958e51.json");
        FileInputStream serviceAccount =
                new FileInputStream(file);

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .build();

        FirebaseApp.initializeApp(options);
    }
}


//@Configuration
//public class FirebaseConfig {
//    @PostConstruct
//    public void init() throws IOException {
//        if (FirebaseApp.getApps().isEmpty()) {
//            InputStream serviceAccount = new ClassPathResource("src/main/resources/iprwc-max-firebase-adminsdk-ltwlx-80eb958e51.json").getInputStream();
//
//            FirebaseOptions options = FirebaseOptions.builder()
//                    .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                    .setDatabaseUrl("https://IPRWC-max.firebaseio.com/")
//                    .build();
//
//            FirebaseApp.initializeApp(options);
//        }
//    }
//}