//package iprwc.max.DAO;
//
//import iprwc.max.model.Test;
//import org.springframework.stereotype.Repository;
//import com.google.firebase.database.DatabaseReference;
//import com.google.firebase.database.FirebaseDatabase;
//import com.google.api.core.ApiFuture;
//import com.google.api.core.ApiFutureCallback;
//import com.google.api.core.ApiFutures;
//import com.google.common.util.concurrent.MoreExecutors;
//
//import java.util.concurrent.CompletableFuture;
//
//@Repository
//public class TestRepository {
//    private final DatabaseReference database;
//
//    public TestRepository() {
//        this.database = FirebaseDatabase.getInstance().getReference("tests");
//    }
//
//    public CompletableFuture<Test> save(Test test) {
//        CompletableFuture<Test> future = new CompletableFuture<>();
//        String key = database.push().getKey();
//        test.setId(key);
//        ApiFuture<Void> apiFuture = database.child(key).setValueAsync(test);
//
//        ApiFutures.addCallback(apiFuture, new ApiFutureCallback<Void>() {
//            @Override
//            public void onSuccess(Void result) {
//                future.complete(test);
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                future.completeExceptionally(t);
//            }
//        }, MoreExecutors.directExecutor());
//
//        return future;
//    }
//
//    // Implement other methods as needed
//}