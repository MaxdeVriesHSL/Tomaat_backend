package tomaat.DAO;

import com.google.cloud.firestore.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import tomaat.model.Beer;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Repository
public class BeerRepository {
    private final CollectionReference beersCollection;
    private static final String COLLECTION_NAME = "beers";
    private static final String STOCK_FIELD = "stockQuantity";
    private static final String TYPE_FIELD = "type";

    @Autowired
    public BeerRepository(Firestore firestore) {
        this.beersCollection = firestore.collection(COLLECTION_NAME);
    }

    public List<Beer> findBeers() {
        List<Beer> beers = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = beersCollection.get().get();
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Beer beer = documentSnapshot.toObject(Beer.class);
                beers.add(beer);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching beers", e);
        }
        return beers;
    }

    public Optional<Beer> findById(UUID id) {
        try {
            DocumentSnapshot doc = beersCollection.document(id.toString()).get().get();
            if (doc.exists()) {
                Beer beer = doc.toObject(Beer.class);
                beer.setUUID(id);
                return Optional.of(beer);
            }
            return Optional.empty();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching beer", e);
        }
    }

    public List<Beer> findByType(String type) {
        List<Beer> beers = new ArrayList<>();
        try {
            QuerySnapshot querySnapshot = beersCollection
                    .whereEqualTo(TYPE_FIELD, type)
                    .get()
                    .get();

            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Beer beer = documentSnapshot.toObject(Beer.class);
                beers.add(beer);
            }
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error fetching beers by type", e);
        }
        return beers;
    }

    public void save(Beer beer) {
        try {
            if (beer.getUUID() == null) {
                beer.setUUID(UUID.randomUUID());
            }
            beersCollection.document(beer.getId()).set(beer).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error saving beer", e);
        }
    }

    public void delete(UUID id) {
        try {
            beersCollection.document(id.toString()).delete().get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error deleting beer", e);
        }
    }

    public void updateStock(UUID id, int newQuantity) {
        try {
            beersCollection.document(id.toString()).update(STOCK_FIELD, newQuantity).get();
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException("Error updating beer stock", e);
        }
    }
}