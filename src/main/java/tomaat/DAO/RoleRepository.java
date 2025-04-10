package tomaat.DAO;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.CollectionReference;
import com.google.cloud.firestore.DocumentSnapshot;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.QuerySnapshot;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import tomaat.model.Role;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class RoleRepository {

    private final Firestore firestore;

    @Autowired
    public RoleRepository(Firestore firestore) {
        this.firestore = firestore;
    }

    private CollectionReference getRoleCollection() {
        return firestore.collection("roles");
    }

    public Optional<Role> findByName(String name) {
        try {
            QuerySnapshot snapshot = getRoleCollection()
                    .whereEqualTo("name", name)
                    .get()
                    .get();

            if (snapshot.isEmpty()) return Optional.empty();

            DocumentSnapshot doc = snapshot.getDocuments().get(0);
            Role role = doc.toObject(Role.class);
            role.setId(UUID.fromString(doc.getId()));
            return Optional.of(role);

        } catch (Exception e) {
            throw new RuntimeException("Error fetching role by name", e);
        }
    }

    public Role save(Role role) {
        try {
            UUID id = role.getId() != null ? role.getId() : UUID.randomUUID();
            role.setId(id);
            getRoleCollection().document(id.toString()).set(role).get();
            return role;
        } catch (Exception e) {
            throw new RuntimeException("Error saving role", e);
        }
    }

    public List<Role> findAll() {
        try {
            List<Role> roles = new ArrayList<>();
            ApiFuture<QuerySnapshot> future = getRoleCollection().get();
            for (DocumentSnapshot doc : future.get().getDocuments()) {
                Role role = doc.toObject(Role.class);
                role.setId(UUID.fromString(doc.getId()));
                roles.add(role);
            }
            return roles;
        } catch (Exception e) {
            throw new RuntimeException("Error fetching all roles", e);
        }
    }

    public void deleteById(UUID id) {
        try {
            getRoleCollection().document(id.toString()).delete().get();
        } catch (Exception e) {
            throw new RuntimeException("Error deleting role", e);
        }
    }

    public boolean existsById(UUID id) {
        try {
            DocumentSnapshot doc = getRoleCollection().document(id.toString()).get().get();
            return doc.exists();
        } catch (Exception e) {
            throw new RuntimeException("Error checking if role exists", e);
        }
    }

    public Optional<String> findRoleNameByUserId(UUID userId) {
        try {
            DocumentSnapshot userDoc = firestore.collection("users").document(userId.toString()).get().get();
            if (!userDoc.exists()) return Optional.empty();

            Map<String, Object> data = userDoc.getData();
            if (data == null || !data.containsKey("role")) return Optional.empty();

            Object roleObj = data.get("role");

            if (roleObj instanceof Map) {
                Map<?, ?> roleMap = (Map<?, ?>) roleObj;
                return Optional.ofNullable((String) roleMap.get("name"));
            } else if (roleObj instanceof String) {
                return Optional.of((String) roleObj);
            } else {
                return Optional.empty();
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching role by user ID", e);
        }
    }
}