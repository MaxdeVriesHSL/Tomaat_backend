package tomaat.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tomaat.model.BeerType;
import tomaat.service.BeerTypeService;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Function;

@RestController
@RequestMapping("/beerType")
public class BeerTypeController {
    @Autowired
    private BeerTypeService beerTypeService;

    @GetMapping("/all")
    public ResponseEntity<List<BeerType>> getAllBeerTypes() {
        List<BeerType> beerTypes = beerTypeService.getAllBeerTypes();
        return ResponseEntity.ok(beerTypes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeerType> getBeerTypeById(@PathVariable String id) {
        return handleUuidOperation(id, uuid -> {
            Optional<BeerType> beerType = beerTypeService.getById(uuid);
            return beerType.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        });
    }

    @PostMapping("/new")
    public ResponseEntity<Void> createBeerType(@RequestBody BeerType beerType) {
        Optional<BeerType> existingType = beerTypeService.getByName(beerType.getName());
        if (existingType.isPresent()) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        beerTypeService.createBeerType(beerType);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBeerType(@PathVariable String id, @RequestBody BeerType beerType) {
        return handleUuidOperation(id, uuid -> {
            Optional<BeerType> existingBeerType = beerTypeService.getById(uuid);

            if (existingBeerType.isPresent()) {
                Optional<BeerType> nameConflict = beerTypeService.getByName(beerType.getName());
                if (nameConflict.isPresent() && !nameConflict.get().getId().equals(id)) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).build();
                }

                beerType.setUUID(uuid);
                beerTypeService.updateBeerType(beerType);
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        });
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBeerType(@PathVariable String id) {
        return handleUuidOperation(id, uuid -> {
            Optional<BeerType> existingBeerType = beerTypeService.getById(uuid);

            if (existingBeerType.isPresent()) {
                beerTypeService.deleteBeerType(uuid);
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