package tomaat.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tomaat.DAO.BeerTypeRepository;
import tomaat.model.BeerType;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class BeerTypeService {
    private final BeerTypeRepository beerTypeRepository;

    @Autowired
    public BeerTypeService(BeerTypeRepository beerTypeRepository) {
        this.beerTypeRepository = beerTypeRepository;
    }

    public List<BeerType> getAllBeerTypes() {
        return beerTypeRepository.findAll();
    }

    public Optional<BeerType> getById(UUID id) {
        return beerTypeRepository.findById(id);
    }

    public Optional<BeerType> getByName(String name) {
        return beerTypeRepository.findByName(name);
    }

    public void createBeerType(BeerType beerType) {
        beerTypeRepository.save(beerType);
    }

    public void updateBeerType(BeerType beerType) {
        beerTypeRepository.save(beerType);
    }

    public void deleteBeerType(UUID id) {
        beerTypeRepository.delete(id);
    }
}