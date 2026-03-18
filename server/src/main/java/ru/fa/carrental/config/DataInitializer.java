package ru.fa.carrental.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.fa.carrental.model.Car;
import ru.fa.carrental.repository.CarRepository;

@Configuration
public class DataInitializer {

    @Bean
    CommandLineRunner init(CarRepository carRepository) {
        return args -> {
            carRepository.save(new Car("Midnight Marauder", "Новинки", 2012, 499_014_700.00, true));
            carRepository.save(new Car("Velocity Vortex", "Внедорожники", 2014, 23_700.00, true));
            carRepository.save(new Car("The Celestial Cruiser", "Седан", 2007, 12_700.00, true));
            carRepository.save(new Car("Stellar Stallion", "Premium", 2018, 15_700.00, true));
        };
    }
}
