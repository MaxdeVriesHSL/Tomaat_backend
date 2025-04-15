package tomaat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomaat.DAO.BeerRepository;
import tomaat.DAO.BeerTypeRepository;
import tomaat.model.Beer;
import tomaat.model.BeerType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BeerService {
    private final BeerRepository beerRepository;
    private final BeerTypeRepository beerTypeRepository;

    @Autowired
    public BeerService(BeerRepository beerRepository, BeerTypeRepository beerTypeRepository) {
        this.beerRepository = beerRepository;
        this.beerTypeRepository = beerTypeRepository;
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
        if (beer.getBeerTypeId() != null) {
            Optional<BeerType> beerType = beerTypeRepository.findById(beer.getBeerTypeUUID());
            beerType.ifPresent(type -> beer.setType(type.getName()));
        }

        beerRepository.save(beer);
    }

    public void updateBeer(Beer beer) {
        if (beer.getBeerTypeId() != null) {
            Optional<BeerType> beerType = beerTypeRepository.findById(beer.getBeerTypeUUID());
            beerType.ifPresent(type -> beer.setType(type.getName()));
        }

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