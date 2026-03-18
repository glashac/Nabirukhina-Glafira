# Информационно‑справочная система компании по аренде автомобилей 

Клиент-серверная система для управления арендой автомобилей: Spring Boot 3 (Java 17) + H2 + JPA; фронт — JavaFX. Покрывает CRUD-операции с автомобилями, фильтрацию, сортировку, статистику, а также графический интерфейс для администратора.

## Быстрый страт

1. Убедитесь, что установлены JDK 17 и Maven.
2. Запустите сервер:
'cd server'
'mvn spring-boot:run  # Сервер на http://localhost:8080'
3. В новом терминале запустите клиент:
'cd client'
'mvn javafx:run  # JavaFX GUI'
Сервер использует H2 (in-memory), данные сохраняются между запусками. Для продакшена замените на PostgreSQL в 'application.properties'.

### Основные эндпоинты (REST API)

'GET /api/cars' — список автомобилей (с фильтром '?q=модель' или '?type=тип').
'GET /api/cars/{id}' — детальный просмотр автомобиля.
'POST /api/cars' — добавление нового автомобиля.
'PUT /api/cars/{id}' — редактирование автомобиля.
'DELETE /api/cars/{id}' — удаление автомобиля.
'GET /api/cars/stats' — статистика (количество, средняя/мин/макс цена).

API документировано JavaDoc в коде. Для тестирования используйте Postman или curl.

#### Архитектура backend
'CarRentalServerApplication' — главный класс Spring Boot.
'model/Car' — JPA-сущность автомобиля (id, модель, тип, год, цена, доступность).
repository/CarRepository — интерфейс репозитория с методами поиска (findByModelContainingIgnoreCase, findByTypeContainingIgnoreCase).
controller/CarController — REST-контроллер с CRUD и статистикой (Stream API для агрегации).
config/DataInitializer — инициализация демо-данных (4 автомобиля при старте).
БД: H2 (in-memory), DDL-auto=create-drop. Миграции не нужны, т.к. JPA генерирует схему.
