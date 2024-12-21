package tomaat.service;

import com.google.api.core.ApiFuture;
import com.google.cloud.firestore.*;
import com.google.firebase.cloud.FirestoreClient;
import tomaat.model.Customer;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

@Service
public class CustomerService {

    public void createCustomer(Customer customer) {
        Firestore fireStore = FirestoreClient.getFirestore();

        DocumentReference docReference = fireStore.collection("customer").document();

        customer.setId(docReference.getId());
        ApiFuture<WriteResult> apiFuture = docReference.set(customer);
    }

    public List<Customer> getAllCustomers() {
        List<Customer> customers = new ArrayList<>();
        Firestore fireStore = FirestoreClient.getFirestore();

        ApiFuture<QuerySnapshot> apiFuture = fireStore.collection("customer").get();
        try {
            QuerySnapshot querySnapshot = apiFuture.get();
            for (DocumentSnapshot documentSnapshot : querySnapshot.getDocuments()) {
                Customer customer = documentSnapshot.toObject(Customer.class);
                customers.add(customer);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }

        return customers;
    }
}
