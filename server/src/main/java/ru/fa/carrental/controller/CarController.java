package ru.fa.carrental.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.fa.carrental.model.Car;
import ru.fa.carrental.repository.CarCategoryRepository;
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
    private final CarCategoryRepository categoryRepository;

    /**
     * @param carRepository      DAO for car persistence
     * @param categoryRepository DAO for car categories
     */
    public CarController(CarRepository carRepository, CarCategoryRepository categoryRepository) {
        this.carRepository = carRepository;
        this.categoryRepository = categoryRepository;
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
                              @RequestParam(required = false) String type,
                              @RequestParam(required = false) Long categoryId) {
        if (q != null && !q.isBlank()) {
            return carRepository.findByModelContainingIgnoreCase(q);
        }
        if (type != null && !type.isBlank()) {
            return carRepository.findByTypeContainingIgnoreCase(type);
        }
        if (categoryId != null) {
            return carRepository.findByCategoryId(categoryId);
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
        if (car.getCategory() != null && car.getCategory().getId() != null) {
            categoryRepository.findById(car.getCategory().getId()).ifPresent(car::setCategory);
        }
        return carRepository.save(car);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Car> updateCar(@PathVariable Long id, @RequestBody Car updated) {
        return carRepository.findById(id)
                .map(existing -> {
                    existing.setModel(updated.getModel());
                    existing.setType(updated.getType());
                    if (updated.getCategory() != null && updated.getCategory().getId() != null) {
                        categoryRepository.findById(updated.getCategory().getId()).ifPresent(existing::setCategory);
                    } else {
                        existing.setCategory(null);
                    }
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

    @GetMapping("/category-stats")
    public ResponseEntity<Map<String, Integer>> categoryStats() {
        List<Car> all = carRepository.findAll();
        Map<String, Integer> stats = new HashMap<>();
        for (Car car : all) {
            String category = car.getCategory() != null ? car.getCategory().getName() : "Без категории";
            stats.put(category, stats.getOrDefault(category, 0) + 1);
        }
        return ResponseEntity.ok(stats);
    }

    public record Stats(int count, double avgPrice, double minPrice, double maxPrice) {
    }
}
