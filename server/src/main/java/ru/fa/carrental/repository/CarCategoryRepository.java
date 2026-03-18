package ru.fa.carrental.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.fa.carrental.model.CarCategory;

@Repository
public interface CarCategoryRepository extends JpaRepository<CarCategory, Long> {
}
