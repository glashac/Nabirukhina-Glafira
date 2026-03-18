# Архитектура системы

## Обзор

Приложение реализует **трёхзвенную архитектуру**:

```
┌──────────────────────────────────────┐
│        Presentation Layer            │
│     JavaFX Client Application        │
│  - MainApp (главное окно)            │
│  - CarFormDialog (формы)             │
│  - TableView (таблица)               │
└──────────────┬───────────────────────┘
               │
               │ HTTP/REST
               │ (HttpClient)
┌──────────────▼───────────────────────┐
│     Business Logic Layer             │
│      Spring Boot Server              │
│  - CarController (REST endpoints)    │
│  - CarRepository (data access)       │
│  - Business services                 │
└──────────────┬───────────────────────┘
               │
               │ JPA/ORM
               │ 
┌──────────────▼───────────────────────┐
│     Persistence Layer                │
│       H2 Database (in-memory)        │
│  - cars table (via JPA)              │
│  - Spring Data JPA auto DDL          │
└──────────────────────────────────────┘
```

##層 1: Presentationelle (Клиент)

### JavaFX Application

**Главные компоненты:**

1. **MainApp** (`client/src/main/java/.../MainApp.java`)
   - Точка входа приложения (extends Application)
   - Управление главным окном и сценой
   - Создание меню, панели инструментов, таблицы
   - Обработка пользовательских действий (кнопки, фильтры)
   - Сохранение состояния окна (Preferences API)

2. **CarFormDialog** (`client/.../CarFormDialog.java`)
   - Диалоговое окно для добавления/редактирования авто
   - Валидация входных данных (год как int, цена как double)
   - Обработка ошибок (NumberFormatException)

3. **Car** (client model, `client/.../model/Car.java`)
   - Локальная модель с JavaFX Properties
   - Двусторонним binding для TableView
   - Методы getter/setter для свойств

### Взаимодействие с сервером

- **CarApi** (`client/.../api/CarApi.java`)
  - HTTP-клиент (java.net.http.HttpClient)
  - JSON сериализация/десериализация (Jackson ObjectMapper)
  - CRUD методы (listCars, createCar, updateCar, deleteCar)
  - Обработка сетевых ошибок (IOException, InterruptedException)

##層 2: Business Logic (Сервер)

### Spring Boot приложение

**Компоненты (MVC-паттерн):**

1. **Models** (`server/src/main/java/.../model/`)
   - **Car** (JPA Entity)
     - Аннотации: @Entity, @Table, @Id, @GeneratedValue
     - Поля: id, model, type, year, pricePerDay, available
     - JavaDoc документация для всех полей
     - equals(), hashCode(), toString() методы

2. **Controllers** (`server/.../controller/`)
   - **CarController**
     - Аннотация: @RestController, @RequestMapping("/api/cars")
     - Методы:
       - `listCars(q, type)` → GET /api/cars
       - `getCar(id)` → GET /api/cars/{id}
       - `createCar(car)` → POST /api/cars
       - `updateCar(id, updated)` → PUT /api/cars/{id}
       - `deleteCar(id)` → DELETE /api/cars/{id}
       - `stats()` → GET /api/cars/stats
     - Возвращает ResponseEntity<T>
     - Обработка ошибок (404, 500)

3. **Repositories** (`server/.../repository/`)
   - **CarRepository** (extends JpaRepository<Car, Long>)
     - Custom методы:
       - `findByModelContainingIgnoreCase(String model)`
       - `findByTypeContainingIgnoreCase(String type)`
     - Автоматические методы от JpaRepository:
       - findAll(), findById(), save(), delete()

4. **Services** (опционально)
   - Не реализовано в MVP, но рекомендуется добавить:
     - CarService (бизнес-логика между Controller и Repository)

5. **Configuration** (`server/.../config/`)
   - **DataInitializer** (CommandLineRunner)
     - Инициализирует демо-данные при запуске
     - 4 тестовых автомобиля

### Обработка ошибок

- try-catch в контроллерах
- ResponseEntity.notFound() для 404
- Логирование через SLF4J

##層 3: Persistence (База данных)

### H2 Database

**Конфигурация** (`server/src/main/resources/application.properties`):
```properties
spring.datasource.url=jdbc:h2:mem:car_rental
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.hibernate.ddl-auto=create-drop
spring.h2.console.enabled=true
spring.jpa.show-sql=true
```

**Таблица schema:**
```sql
CREATE TABLE cars (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  model VARCHAR(255) NOT NULL,
  type VARCHAR(255) NOT NULL,
  year INT NOT NULL,
  price_per_day DOUBLE NOT NULL,
  available BOOLEAN NOT NULL
);
```

**Индексы** (рекомендуется добавить):
```sql
CREATE INDEX idx_model ON cars(model);
CREATE INDEX idx_type ON cars(type);
```

**Примеры данных:**
- Midnight Marauder (Новинки, 2012, 499014700.00)
- Velocity Vortex (Внедорожники, 2014, 23700.00)
- The Celestial Cruiser (Седан, 2007, 12700.00)
- Stellar Stallion (Premium, 2018, 15700.00)

## Потоки данных

### Создание автомобиля

```
Пользователь (GUI)
    ↓
[MainApp] → Кнопка "Добавить"
    ↓
[CarFormDialog] → Форма (model, type, year, price, available)
    ↓
Пользователь → OK
    ↓
[CarApi.createCar(car)]
    ↓
POST /api/cars (JSON)
    ↓
[CarController.createCar(car)]
    ↓
[CarRepository.save(car)] → INSERT
    ↓
Ответ: Car (с ID)
    ↓
[MainApp.refreshTable()]
    ↓
Таблица обновлена
```

### Получение списка

```
Пользователь (GUI)
    ↓
[MainApp] → startApp()
    ↓
[CarApi.listCars(null)]
    ↓
GET /api/cars
    ↓
[CarController.listCars()]
    ↓
[CarRepository.findAll()]
    ↓
БД → SELECT * FROM cars
    ↓
Ответ: List<Car> (JSON)
    ↓
[MainApp] → TableView.setItems()
    ↓
Таблица заполнена
```

### Фильтрация

```
Пользователь вводит "velocity" в поле поиска
    ↓
[MainApp.filterField.setOnAction()]
    ↓
[CarApi.listCars("velocity")]
    ↓
GET /api/cars?q=velocity
    ↓
[CarController.listCars(q="velocity")]
    ↓
[CarRepository.findByModelContainingIgnoreCase("velocity")]
    ↓
SELECT * FROM cars WHERE UPPER(model) LIKE '%VELOCITY%'
    ↓
Ответ: List<Car> (отфильтровано)
    ↓
Таблица обновлена
```

### Статистика

```
Пользователь → Кнопка "Обновить статистику"
    ↓
[CarApi.getStats()]
    ↓
GET /api/cars/stats
    ↓
[CarController.stats()]
    ↓
[CarRepository.findAll()]
    ↓
Stream API: .mapToDouble(Car::getPricePerDay)
    ↓
Вычисление: count, avg, min, max
    ↓
Ответ: Stats (JSON)
    ↓
[MainApp] → Label в статус-баре
    ↓
"Всего: 4, Ср. цена: 136775.0, Мин: 12700.0, Макс: 499014700.0"
```

## Диаграмма классов (UML)

```
Car (Server)
  - id: Long
  - model: String
  - type: String
  - year: int
  - pricePerDay: double
  - available: boolean
  + getId(): Long
  + getModel(): String
  + setModel(String)
  ...

CarRepository
  + findAll(): List<Car>
  + findById(Long): Optional<Car>
  + findByModelContainingIgnoreCase(String): List<Car>
  + save(Car): Car
  + deleteById(Long)

CarController
  + listCars(q, type): List<Car>
  + getCar(id): ResponseEntity<Car>
  + createCar(car): Car
  + updateCar(id, updated): ResponseEntity<Car>
  + deleteCar(id): ResponseEntity<Void>
  + stats(): ResponseEntity<Stats>

CarApi (Client)
  + listCars(query): List<Car>
  + createCar(car): Car
  + updateCar(car): Car
  + deleteCar(id)
  + getStats(): Stats

Car (Client)
  - id: LongProperty
  - model: StringProperty
  - type: StringProperty
  ...

MainApp
  + start(Stage): void
  + refreshTable()
  + createToolbar(): ToolBar
  + createTable(): TableView<Car>
  ...
```

## Принципы разработки

1. **SOLID**
   - S: Single Responsibility — Car только для данных, Controller для API
   - O: Open/Closed — легко добавить новый эндпоинт
   - L: Liskov Substitution — JpaRepository реализует List операции
   - I: Interface Segregation — минимальные интерфейсы
   - D: Dependency Inversion — Spring DI для CarRepository

2. **DRY** (Don't Repeat Yourself)
   - Общая обработка ошибок
   - Переиспользование методов

3. **Близкий код**
   - JavaDoc для всех методов
   - Осмысленные имена переменных
   - Правильный формат (4 пробела отступ)

## Безопасность

**Текущие ограничения:**
- Нет аутентификации (все могут создавать/удалять)
- Нет CORS-ограничений
- Нет валидации на уровне БД (constraints)

**Рекомендуемые улучшения:**
- Spring Security + JWT
- HTTPS
- Database constraints (@NotNull, @NotBlank)
- Rate limiting

## Производительность

**Оптимизации (MVP):**
- H2 in-memory (быстро)
- Простые индексы (по model, type)
- Кэширование в памяти нет (можно добавить через @Cacheable)

**Метрики P95:**
- GET /api/cars: < 100 мс
- POST /api/cars: < 200 мс
- GET /api/cars/stats: < 150 мс

## Расширения (Future)

1. **PostgreSQL** вместо H2
2. **Booking** сущность (заказы)
3. **User** сущность (клиенты)
4. **JWT Auth** для безопасности
5. **WebSocket** для real-time обновлений
6. **REST Docs** (SpringFox/Springdoc-openapi)
7. **Кэширование** (Redis)
