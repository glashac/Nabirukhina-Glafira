package ru.fa.carrental.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.fa.carrental.model.Car;
import ru.fa.carrental.model.CarCategory;
import ru.fa.carrental.repository.CarCategoryRepository;
import ru.fa.carrental.repository.CarRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(CarRepository carRepository, CarCategoryRepository categoryRepository) {
        return args -> {
            CarCategory sport = categoryRepository.save(new CarCategory("Спортивные"));
            CarCategory suv = categoryRepository.save(new CarCategory("Внедорожники"));
            CarCategory sedan = categoryRepository.save(new CarCategory("Седан"));
            CarCategory premium = categoryRepository.save(new CarCategory("Premium"));

            carRepository.save(new Car("Midnight Marauder", "Новинки", sport, 2012, 499_014_700.00, true));
            carRepository.save(new Car("Velocity Vortex", "Внедорожники", suv, 2014, 23_700.00, true));
            carRepository.save(new Car("The Celestial Cruiser", "Седан", sedan, 2007, 12_700.00, true));
            carRepository.save(new Car("Stellar Stallion", "Premium", premium, 2018, 15_700.00, true));
        };
    }
}
