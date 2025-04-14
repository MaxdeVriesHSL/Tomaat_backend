package tomaat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomaat.model.Beer;
import tomaat.service.BeerService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping("/beer")
public class BeerController {
    @Autowired
    private BeerService beerService;

    @GetMapping("/getBeers")
    public ResponseEntity<List<Beer>> getAllBeers() {
        List<Beer> beers = beerService.getAllBeers();
        return ResponseEntity.ok(beers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Beer> getBeerById(@PathVariable String id) {
        return handleUuidOperation(id, uuid -> {
            Optional<Beer> beer = beerService.getById(uuid);
            return beer.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        });
    }

    @GetMapping("/type/{type}")
    public ResponseEntity<List<Beer>> getBeersByType(@PathVariable String type) {
        List<Beer> beers = beerService.getByType(type);
        return ResponseEntity.ok(beers);
    }

    @PostMapping("/newBeer")
    public ResponseEntity<Void> createBeer(@RequestBody Beer beer) {
        beerService.createBeer(beer);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBeer(@PathVariable String id, @RequestBody Beer beer) {
        return handleUuidOperation(id, uuid -> {
            Optional<Beer> existingBeer = beerService.getById(uuid);

            if (existingBeer.isPresent()) {
                beer.setUUID(uuid);
                beerService.updateBeer(beer);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeer(@PathVariable String id) {
        return handleUuidOperation(id, uuid -> {
            Optional<Beer> existingBeer = beerService.getById(uuid);

            if (existingBeer.isPresent()) {
                beerService.deleteBeer(uuid);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @PatchMapping("/{id}/stock")
    public ResponseEntity<Void> updateBeerStock(@PathVariable String id, @RequestParam int quantity) {
        return handleUuidOperation(id, uuid -> {
            Optional<Beer> existingBeer = beerService.getById(uuid);

            if (existingBeer.isPresent()) {
                beerService.updateBeerStock(uuid, quantity);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    private <T> ResponseEntity<T> handleUuidOperation(String id, Function<UUID, ResponseEntity<T>> operation) {
        try {
            UUID uuid = UUID.fromString(id);
            return operation.apply(uuid);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}