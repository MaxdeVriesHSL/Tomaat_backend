package tomaat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import tomaat.controller.BeerController;
import tomaat.model.Beer;
import tomaat.service.BeerService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class BeerControllerTest {

    private BeerController beerController;
    private TestBeerService testBeerService;
    private Beer testBeer;
    private List<Beer> testBeerList;
    private final UUID testUuid = UUID.randomUUID();

    @BeforeEach
    public void setup() {
        // Create a test beer
        testBeer = new Beer();
        testBeer.setName("Test IPA");
        testBeer.setDescription("A test beer");
        testBeer.setPrice(4.99);
        testBeer.setType("IPA");
        testBeer.setAlcoholPercentage(5.5);
        testBeer.setStockQuantity(100);
        testBeer.setUUID(testUuid);

        // Create a list of test beers
        testBeerList = new ArrayList<>();
        testBeerList.add(testBeer);

        // Create a test beer service implementation
        testBeerService = new TestBeerService();

        // Create controller with test service
        beerController = new BeerController();
        beerController.beerService = testBeerService;
    }

    @Test
    public void getAllBeers_ShouldReturnListOfBeers() {
        // Arrange
        testBeerService.setBeersToReturn(testBeerList);

        // Act
        ResponseEntity<List<Beer>> response = beerController.getAllBeers();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test IPA", response.getBody().get(0).getName());
    }

    @Test
    public void getBeerById_WhenBeerExists_ShouldReturnBeer() {
        // Arrange
        testBeerService.setBeerToReturn(testBeer);

        // Act
        ResponseEntity<Beer> response = beerController.getBeerById(testUuid.toString());

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test IPA", response.getBody().getName());
        assertEquals(4.99, response.getBody().getPrice());
    }

    private class TestBeerService extends BeerService {

        private List<Beer> beersToReturn = new ArrayList<>();
        private Beer beerToReturn = null;

        public TestBeerService() {
            super(null, null);
        }

        public void setBeersToReturn(List<Beer> beers) {
            this.beersToReturn = beers;
        }

        public void setBeerToReturn(Beer beer) {
            this.beerToReturn = beer;
        }

        @Override
        public List<Beer> getAllBeers() {
            return beersToReturn;
        }

        @Override
        public Optional<Beer> getById(UUID id) {
            if (beerToReturn != null && beerToReturn.getUUID().equals(id)) {
                return Optional.of(beerToReturn);
            }
            return Optional.empty();
        }
    }
}