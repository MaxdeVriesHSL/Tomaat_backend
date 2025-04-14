package tomaat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomaat.DAO.BeerRepository;
import tomaat.model.Beer;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BeerService {
    private final BeerRepository beerRepository;

    @Autowired
    public BeerService(BeerRepository beerRepository) {
        this.beerRepository = beerRepository;
    }

    public List<Beer> getAllBeers() {
        return beerRepository.findBeers();
    }

    public Optional<Beer> getById(UUID id) {
        return beerRepository.findById(id);
    }

    public List<Beer> getByType(String type) {
        return beerRepository.findByType(type);
    }

    public void createBeer(Beer beer) {
        beerRepository.save(beer);
    }

    public void updateBeer(Beer beer) {
        beerRepository.save(beer);
    }

    public void deleteBeer(UUID id) {
        beerRepository.delete(id);
    }

    public void updateBeerStock(UUID id, int newQuantity) {
        Optional<Beer> beerOptional = beerRepository.findById(id);
        if (beerOptional.isPresent()) {
            beerRepository.updateStock(id, newQuantity);
        } else {
            throw new RuntimeException("Beer not found with id: " + id);
        }
    }
}