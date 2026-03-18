# REST API документация

## Базовый URL
```
http://localhost:8080/api/cars
```

## Эндпоинты

### 1. Получить список всех автомобилей
**Запрос:**
```http
GET /api/cars
Content-Type: application/json
```

**Параметры запроса:**
- `q` (optional) — фильтр по модели (case-insensitive)
- `type` (optional) — фильтр по типу

**Примеры:**
```bash
# Все автомобили
curl -X GET "http://localhost:8080/api/cars"

# Фильтр по модели
curl -X GET "http://localhost:8080/api/cars?q=velocity"

# Фильтр по типу
curl -X GET "http://localhost:8080/api/cars?type=Premium"
```

**Ответ (200 OK):**
```json
[
  {
    "id": 1,
    "model": "Velocity Vortex",
    "type": "Внедорожники",
    "year": 2014,
    "pricePerDay": 23700.0,
    "available": true
  },
  {
    "id": 2,
    "model": "Stellar Stallion",
    "type": "Premium",
    "year": 2018,
    "pricePerDay": 15700.0,
    "available": true
  }
]
```

---

### 2. Получить автомобиль по ID
**Запрос:**
```http
GET /api/cars/{id}
Content-Type: application/json
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/cars/1"
```

**Ответ (200 OK):**
```json
{
  "id": 1,
  "model": "Velocity Vortex",
  "type": "Внедорожники",
  "year": 2014,
  "pricePerDay": 23700.0,
  "available": true
}
```

**Ошибка (404 Not Found):**
```json
{
  "message": "Car not found"
}
```

---

### 3. Создать новый автомобиль
**Запрос:**
```http
POST /api/cars
Content-Type: application/json
```

**Тело запроса:**
```json
{
  "model": "Tesla Model S",
  "type": "Premium",
  "year": 2024,
  "pricePerDay": 45000.0,
  "available": true
}
```

**Пример:**
```bash
curl -X POST "http://localhost:8080/api/cars" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "Tesla Model S",
    "type": "Premium",
    "year": 2024,
    "pricePerDay": 45000.0,
    "available": true
  }'
```

**Ответ (201 Created):**
```json
{
  "id": 5,
  "model": "Tesla Model S",
  "type": "Premium",
  "year": 2024,
  "pricePerDay": 45000.0,
  "available": true
}
```

---

### 4. Обновить автомобиль
**Запрос:**
```http
PUT /api/cars/{id}
Content-Type: application/json
```

**Тело запроса:**
```json
{
  "model": "Tesla Model S",
  "type": "Premium",
  "year": 2024,
  "pricePerDay": 50000.0,
  "available": false
}
```

**Пример:**
```bash
curl -X PUT "http://localhost:8080/api/cars/5" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "Tesla Model S",
    "type": "Premium",
    "year": 2024,
    "pricePerDay": 50000.0,
    "available": false
  }'
```

**Ответ (200 OK):**
```json
{
  "id": 5,
  "model": "Tesla Model S",
  "type": "Premium",
  "year": 2024,
  "pricePerDay": 50000.0,
  "available": false
}
```

---

### 5. Удалить автомобиль
**Запрос:**
```http
DELETE /api/cars/{id}
```

**Пример:**
```bash
curl -X DELETE "http://localhost:8080/api/cars/5"
```

**Ответ (204 No Content):**
```
(пустое тело)
```

**Ошибка (404 Not Found):**
```json
{
  "message": "Car not found"
}
```

---

### 6. Получить статистику
**Запрос:**
```http
GET /api/cars/stats
Content-Type: application/json
```

**Пример:**
```bash
curl -X GET "http://localhost:8080/api/cars/stats"
```

**Ответ (200 OK):**
```json
{
  "count": 4,
  "avgPrice": 136775.0,
  "minPrice": 12700.0,
  "maxPrice": 499014700.0
}
```

---

## Коды ответов HTTP

| Код | Описание |
|-----|---------|
| 200 | OK — успешно |
| 201 | Created — ресурс создан |
| 204 | No Content — успешно удалено |
| 400 | Bad Request — неверный формат |
| 404 | Not Found — ресурс не найден |
| 500 | Internal Server Error — ошибка сервера |

---

## Типы данных

### Car объект
```json
{
  "id": integer,           // Автогенерируемый ID
  "model": string,         // Модель (обязателен)
  "type": string,          // Тип (обязателен)
  "year": integer,         // Год (обязателен)
  "pricePerDay": number,   // Цена в день (обязателен)
  "available": boolean     // Доступність (обязателен)
}
```

### Stats объект
```json
{
  "count": integer,        // Количество автомобилей
  "avgPrice": number,      // Средняя цена
  "minPrice": number,      // Минимальная цена
  "maxPrice": number       // Максимальная цена
}
```

---

## Примеры использования

### Получить все премиум-авто
```bash
curl -X GET "http://localhost:8080/api/cars?type=Premium" \
  -H "Content-Type: application/json"
```

### Добавить авто и получить ID
```bash
curl -X POST "http://localhost:8080/api/cars" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "BMW M5",
    "type": "Premium",
    "year": 2023,
    "pricePerDay": 35000.0,
    "available": true
  }' | jq '.id'  # вернёт: 5
```

### Обновить nur цену авто
```bash
# Получить текущие данные
curl -X GET "http://localhost:8080/api/cars/1" | jq '.'

# Обновить цену
curl -X PUT "http://localhost:8080/api/cars/1" \
  -H "Content-Type: application/json" \
  -d '{
    "model": "Velocity Vortex",
    "type": "Внедорожники",
    "year": 2014,
    "pricePerDay": 25000.0,
    "available": true
  }'
```

---

## Лимиты и нормы

- **Максимальный размер запроса**: 1 MB
- **Таймаут соединения**: 30 сек
- **Максимум записей в ответе**: не ограничено (H2 in-memory)

---

## Отладка

### Включить логирование запросов
Отредактируйте `server/src/main/resources/application.properties`:
```properties
logging.level.org.springframework.web=DEBUG
logging.level.ru.fa.carrental=DEBUG
```

### Просмотреть консоль БД H2
```
http://localhost:8080/h2-console
```
- JDBC URL: `jdbc:h2:mem:car_rental`
- User: `sa`
- Password: (пусто)
