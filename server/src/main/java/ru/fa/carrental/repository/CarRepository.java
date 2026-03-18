package ru.fa.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fa.carrental.model.Car;

import java.util.List;

@Repository
public interface CarRepository extends JpaRepository<Car, Long> {
    List<Car> findByTypeContainingIgnoreCase(String type);
    List<Car> findByModelContainingIgnoreCase(String model);
}
