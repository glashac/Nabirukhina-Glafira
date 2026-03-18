package ru.fa.carrental.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import ru.fa.carrental.repository.CarRepository;
import ru.fa.carrental.service.EmailService;

@Controller
public class WebController {

    private final CarRepository carRepository;
    private final EmailService emailService;

    public WebController(CarRepository carRepository, EmailService emailService) {
        this.carRepository = carRepository;
        this.emailService = emailService;
    }

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("cars", carRepository.findAll());
        return "index";
    }

    @GetMapping("/send-test-email")
    public String sendTestEmail(Model model) {
        // Этот эндпоинт демонстрирует работу JavaMailSender.
        // Замените адрес получателя на реальный перед запуском.
        emailService.sendSimpleMessage("recipient@example.com", "Car Rental: test mail", "Это тестовое письмо из приложения Car Rental.");
        model.addAttribute("cars", carRepository.findAll());
        model.addAttribute("mailSent", true);
        return "index";
    }
}
