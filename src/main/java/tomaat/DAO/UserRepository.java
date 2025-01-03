package tomaat.DAO;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tomaat.model.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

@Repository
public class UserRepository {
    private final CollectionReference usersCollection;

    @Autowired
    public UserRepository(Firestore firestore) {
        this.usersCollection = firestore.collection("users");
    }


    public List<User> findUsers() {
        List<User> users = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = usersCollection.get().get();
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                User user = documentSnapshot.toObject(User.class);
                users.add(user);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching users", e);
        }
        return users;
    }

    public Optional<User> findById(UUID id) {
        try {
            DocumentSnapshot doc = usersCollection.document(id.toString()).get().get();
            if (doc.exists()) {
                User user = doc.toObject(User.class);
                user.setUUID(id);
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching user", e);
        }
    }

    public Optional<User> findByEmail(String email) {
        try {
            QuerySnapshot querySnapshot = usersCollection
                    .whereEqualTo("email", email)
                    .get()
                    .get();

            if (!querySnapshot.isEmpty()) {
                User user = querySnapshot.getDocuments().get(0).toObject(User.class);
                user.setUUID(UUID.fromString(user.getId()));
                return Optional.of(user);
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching user by email", e);
        }
    }

    public void save(User user) {
        try {
            if (user.getUUID() == null) {
                user.setUUID(UUID.randomUUID());
            }
            usersCollection.document(user.getId()).set(user).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error saving user", e);
        }
    }
}
