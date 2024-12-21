//package iprwc.max.DAO;
//
//import iprwc.max.model.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.concurrent.CompletableFuture;
//
//@Component
//public class TestDAO {
//    @Autowired
//    private TestRepository testRepository;
//
//    public CompletableFuture<Test> createTest(Test test) {
//        return testRepository.save(test);
//    }
//
//    // Implement getAllTests() and other methods as needed
//}