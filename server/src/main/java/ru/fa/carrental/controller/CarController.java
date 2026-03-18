package ru.fa.carrental.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fa.carrental.model.Car;
import ru.fa.carrental.repository.CarRepository;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing cars.
 * <p>
 * Exposes CRUD operations for the {@link ru.fa.carrental.model.Car} entity.
 * </p>
 */
@RestController
@RequestMapping("/api/cars")
public class CarController {

    private final CarRepository carRepository;

    /**
     * @param carRepository DAO for car persistence
     */
    public CarController(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    /**
     * Returns a list of cars.
     *
     * @param q    optional query filter by model
     * @param type optional filter by type
     * @return list of cars matching the filter criteria
     */
    @GetMapping
    public List<Car> listCars(@RequestParam(required = false) String q,
                              @RequestParam(required = false) String type) {
        if (q != null && !q.isBlank()) {
            return carRepository.findByModelContainingIgnoreCase(q);
        }
        if (type != null && !type.isBlank()) {
            return carRepository.findByTypeContainingIgnoreCase(type);
        }
        return carRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Car> getCar(@PathVariable Long id) {
        Optional<Car> car = carRepository.findById(id);
        return car.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Car createCar(@RequestBody Car car) {
        car.setId(null);
        return carRepository.save(car);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car updated) {
        return carRepository.findById(id)
                .map(existing -> {
                    existing.setModel(updated.getModel());
                    existing.setType(updated.getType());
                    existing.setYear(updated.getYear());
                    existing.setPricePerDay(updated.getPricePerDay());
                    existing.setAvailable(updated.isAvailable());
                    return ResponseEntity.ok(carRepository.save(existing));
                })
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCar(@PathVariable Long id) {
        if (carRepository.existsById(id)) {
            carRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }

    @GetMapping("/stats")
    public ResponseEntity<Stats> stats() {
        List<Car> all = carRepository.findAll();
        if (all.isEmpty()) {
            return ResponseEntity.ok(new Stats(0, 0, 0, 0));
        }
        double sum = all.stream().mapToDouble(Car::getPricePerDay).sum();
        double avg = sum / all.size();
        double min = all.stream().mapToDouble(Car::getPricePerDay).min().orElse(0);
        double max = all.stream().mapToDouble(Car::getPricePerDay).max().orElse(0);
        return ResponseEntity.ok(new Stats(all.size(), avg, min, max));
    }

    public record Stats(int count, double avgPrice, double minPrice, double maxPrice) {
    }
}
