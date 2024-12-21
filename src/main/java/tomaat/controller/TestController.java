//package iprwc.max.controller;
//
//import iprwc.max.DAO.TestDAO;
//import iprwc.max.model.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.concurrent.CompletableFuture;
//
//@RestController
//@RequestMapping(value = "/api/test")
//public class TestController {
//    @Autowired
//    private TestDAO testDAO;
//
//    @PostMapping
//    public CompletableFuture<ResponseEntity<Test>> createTest(@RequestBody Test test) {
//        return testDAO.createTest(test)
//                .thenApply(createdTest -> new ResponseEntity<>(createdTest, HttpStatus.CREATED));
//    }
//
//    // Implement getAllTests() and other methods as needed
//}