package tomaat.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import tomaat.DAO.UserRepository;
import tomaat.model.User;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Service
public class UserService {
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {this.userRepository = userRepository;}

    public Optional<User> getById(UUID id) {
        return this.userRepository.findById(id);
    }

    public void createUser(User user) {
        Firestore fireStore = FirestoreClient.getFirestore();

        DocumentReference docReference = fireStore.collection("customer").document();

        user.setId(UUID.fromString(docReference.getId()));
        ApiFuture<WriteResult> apiFuture = docReference.set(user);
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        Firestore fireStore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> apiFuture = fireStore.collection("customer").get();
        try {
            QuerySnapshot querySnapshot = apiFuture.get();
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                User user = documentSnapshot.toObject(User.class);
                users.add(user);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return users;
    }
}
