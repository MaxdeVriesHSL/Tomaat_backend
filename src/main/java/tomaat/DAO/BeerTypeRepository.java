package tomaat.DAO;

import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tomaat.model.BeerType;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class BeerTypeRepository {
    private final CollectionReference beerTypesCollection;
    private static final String COLLECTION_NAME = "beerTypes";
    private static final String NAME_FIELD = "name";

    @Autowired
    public BeerTypeRepository(Firestore firestore) {
        this.beerTypesCollection = firestore.collection(COLLECTION_NAME);
    }

    public List<BeerType> findAll() {
        List<BeerType> beerTypes = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = beerTypesCollection.get().get();
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                BeerType beerType = documentSnapshot.toObject(BeerType.class);
                beerTypes.add(beerType);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching beer types", e);
        }
        return beerTypes;
    }

    public Optional<BeerType> findById(UUID id) {
        try {
            DocumentSnapshot doc = beerTypesCollection.document(id.toString()).get().get();
            if (doc.exists()) {
                BeerType beerType = doc.toObject(BeerType.class);
                beerType.setUUID(id);
                return Optional.of(beerType);
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching beer type", e);
        }
    }

    public Optional<BeerType> findByName(String name) {
        try {
            QuerySnapshot querySnapshot = beerTypesCollection
                    .whereEqualTo(NAME_FIELD, name)
                    .limit(1)
                    .get()
                    .get();

            if (!querySnapshot.isEmpty()) {
                BeerType beerType = querySnapshot.getDocuments().get(0).toObject(BeerType.class);
                return Optional.of(beerType);
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching beer type by name", e);
        }
    }

    public void save(BeerType beerType) {
        try {
            if (beerType.getUUID() == null) {
                beerType.setUUID(UUID.randomUUID());
            }
            beerTypesCollection.document(beerType.getId()).set(beerType).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error saving beer type", e);
        }
    }

    public void delete(UUID id) {
        try {
            beerTypesCollection.document(id.toString()).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error deleting beer type", e);
        }
    }
}