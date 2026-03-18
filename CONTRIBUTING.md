# Инструкции по разработке и контрибьютингу

## Тип проекта: Курсовая работа (Academic Project)

Этот проект — это учебная курсовая работа. Основные разработчики могут принимать улучшения, но все изменения должны соответствовать методическим требованиям.

## Развертывание окружения разработки

### 1. Требования
- JDK 17+
- Maven 3.9+
- IntelliJ IDEA (рекомендуется) или Eclipse/VS Code
- Git

### 2. Форк и клонирование
```bash
git clone https://github.com/glashac/Nabirukhina-Glafira.git
cd Nabirukhina-Glafira
```

### 3. Импорт в IDE

**IntelliJ IDEA:**
- File → Open → выберите папку проекта
- Maven → Reload Projects

**Eclipse:**
- File → Import → Existing Maven Projects → выберите папку

### 4. Быстрый старт
```bash
# Сервер
cd server && mvn spring-boot:run

# Клиент (в новом терминале)
cd client && mvn javafx:run
```

## Структура кода

```
server/
  src/main/java/ru/fa/carrental/
    ├── CarRentalServerApplication       # Main класс
    ├── config/                          # Configuration
    │   └── DataInitializer.java         # Демо-данные
    ├── controller/                      # REST Endpoints
    │   └── CarController.java
    ├── model/                           # Database entities
    │   └── Car.java
    └── repository/                      # Data access
        └── CarRepository.java

client/
  src/main/java/ru/fa/carrental/client/
    ├── MainApp.java                     # JavaFX Application
    ├── CarFormDialog.java               # Form dialog
    ├── api/                             # HTTP client
    │   └── CarApi.java
    └── model/                           # UI models
        └── Car.java
```

## Стайл гайд (Code Style)

### Java код
- **Индентация**: 4 пробела (no tabs)
- **Оформленть кода**: Google Java Style Guide
- **Класс-патерн**: PascalCase (Car, CarController)
- **Методы/переменные**: camelCase (getCar, pricePerDay)
- **Константы**: UPPER_SNAKE_CASE (API_BASE_URL)

### Примеры

**Правильно:**
```java
/**
 * Retrieves a car by ID.
 *
 * @param id the car identifier
 * @return optional car or empty if not found
 */
public Optional<Car> findCar(Long id) {
    return carRepository.findById(id);
}
```

**Неправильно:**
```java
public Optional<Car> find_car(long car_id) {
    return carRepository.findById(car_id);
}
```

## JavaDoc документация

Все публичные классы и методы **должны** иметь JavaDoc:

```java
/**
 * Model representing a car available for rent.
 * <p>
 * This entity is persisted in the database via JPA.
 * </p>
 */
@Entity
public class Car {
    
    /**
     * Gets the car's unique identifier.
     *
     * @return the ID
     */
    public Long getId() {
        return id;
    }
}
```

## Обработка ошибок

### В сервере (Spring Boot)

```java
try {
    Car car = carRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Car not found"));
    return ResponseEntity.ok(car);
} catch (ResourceNotFoundException e) {
    return ResponseEntity.status(404).body(null);
}
```

### В клиенте (JavaFX)

```java
try {
    Car result = carApi.createCar(car);
    if (result != null) {
        refreshTable();
    } else {
        showAlert(Alert.AlertType.ERROR, "Error", "Failed to create car");
    }
} catch (Exception e) {
    showAlert(Alert.AlertType.ERROR, "Error", "Network error: " + e.getMessage());
}
```

## Тестирование

### Структура тестов

```
server/src/test/java/ru/fa/carrental/
  ├── controller/CarControllerTest.java
  ├── repository/CarRepositoryTest.java
  └── model/CarTest.java

client/src/test/java/ru/fa/carrental/client/
  ├── api/CarApiTest.java
  └── ...
```

### Написание теста

```java
@SpringBootTest
class CarControllerTest {
    
    @Autowired
    private CarRepository carRepository;
    
    @Autowired
    private MockMvc mockMvc;
    
    @Test
    void testListCars() throws Exception {
        mockMvc.perform(get("/api/cars"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(greaterThan(0))));
    }
    
    @Test
    void testCreateCar() throws Exception {
        String carJson = """
            {
                "model": "Test",
                "type": "Test",
                "year": 2026,
                "pricePerDay": 100.0,
                "available": true
            }
            """;
        
        mockMvc.perform(post("/api/cars")
            .contentType(MediaType.APPLICATION_JSON)
            .content(carJson))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id", notNullValue()));
    }
}
```

## Запуск тестов

```bash
# Все тесты
mvn test

# Только дбиочность сервера
cd server && mvn test

# Только клиента
cd client && mvn test

# С покрытием
mvn jacoco:report
# Report в target/site/jacoco/index.html
```

## Процесс разработки

### Добавить новую функцию

1. **Создайте ветку:**
   ```bash
   git checkout -b feature/my-feature
   ```

2. **Разработайте:**
   - Напишите код
   - Добавьте тесты
   - Обновите документацию (если нужно)

3. **Commit:**
   ```bash
   git add .
   git commit -m "feat: Add new feature description"
   # или
   git commit -m "fix: Fix bug description"
   git commit -m "docs: Update documentation"
   git commit -m "test: Add tests for feature"
   ```

4. **Push:**
   ```bash
   git push origin feature/my-feature
   ```

5. **Pull Request:**
   - Откройте GitHub
   - Нажмите "New Pull Request"
   - Описание: что изменилось
   - Request review

### Сообщить об ошибке

1. Откройте GitHub Issues
2. Нажмите "New Issue"
3. Заголовок: краткое описание
4. Описание включайте:
   - Шаги для воспроизведения
   - Ожидаемый результат
   - Фактический результат
   - Версия Java/Maven

## Методические требования

При разработке обязательно соблюдайте требования курсовой работы:

- Трёхзвенная архитектура (клиент-сервер-БД)
- ORM (JPA/Hibernate)
- REST API (HTTP методы)
- GUI (JavaFX)
- CRUD операции
- Фильтрация/сортировка
- Статистика
- Обработка ошибок
- JavaDoc
- ООП-принципы

## Ci/CD (опционально)

Проект готов к интеграции с GitHub Actions:

```yaml
name: Build and Test
on: [push, pull_request]
jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - uses: actions/setup-java@v2
        with:
          java-version: '17'
      - run: mvn clean test
```

## Чек-лист перед сабмитом

- [ ] Code компилируется без ошибок
- [ ] Все тесты проходят (`mvn test`)
- [ ] JavaDoc добавлен для новых методов
- [ ] Код следует style guide
- [ ] Нет неиспользуемого импорта
- [ ] Сообщения коммитов понятные
- [ ] Изменения документированы (если нужно)

## Вопросы?

- Issues: https://github.com/glashac/Nabirukhina-Glafira/issues
- Discussions: https://github.com/glashac/Nabirukhina-Glafira/discussions

---

Спасибо за вклад! 🙏
