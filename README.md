# Информационно-справочная система компании по аренде автомобилей

Клиент-серверное приложение для управления арендой автомобилей.  
**Технологии**: Java 17, Spring Boot 3, JavaFX, JPA/Hibernate, H2, Maven.

## Содержание

- [Быстрый старт](#быстрый-старт)
- [Структура проекта](#структура-проекта)
- [Архитектура](#архитектура)
- [API Эндпоинты](#api-эндпоинты)
- [Функциональность](#функциональность)
- [Разработка](#разработка)
- [Тестирование](#тестирование)

## Быстрый старт

### Требования
- JDK 17+
- Maven 3.9+
- Git

### Запуск

1. **Клонируйте репозиторий**:
   ```bash
   git clone https://github.com/glashac/Nabirukhina-Glafira.git
   cd Nabirukhina-Glafira
   ```

2. **Запустите сервер** (в первом терминале):
   ```bash
   cd server
   mvn spring-boot:run
   ```
   Сервер будет доступен на `http://localhost:8080`

3. **Запустите клиент** (во втором терминале):
   ```bash
   cd client
   mvn javafx:run
   ```
   Откроется GUI приложение JavaFX.

### Демо-данные

При старте сервера автоматически создаются 4 тестовых автомобиля. После первого запуска данные сохраняются.

## Структура проекта

```
car-rental/
├── server/                           # Сервер (Spring Boot)
│   ├── pom.xml
│   ├── src/main/java/ru/fa/carrental/
│   │   ├── CarRentalServerApplication.java
│   │   ├── config/DataInitializer.java
│   │   ├── controller/CarController.java
│   │   ├── model/Car.java
│   │   └── repository/CarRepository.java
│   └── src/main/resources/application.properties
│
├── client/                           # Клиент (JavaFX)
│   ├── pom.xml
│   ├── src/main/java/ru/fa/carrental/client/
│   │   ├── MainApp.java
│   │   ├── CarFormDialog.java
│   │   ├── api/CarApi.java
│   │   └── model/Car.java
│   └── src/test/java/...
│
├── docs/                             # Документация
│   ├── poyasnitelnaya_zapiska.md     # Пояснительная записка (~30 стр.)
│   ├── API.md                        # Описание API
│   ├── ARCHITECTURE.md               # Архитектура
│   ├── SETUP.md                      # Подробная инструкция запуска
│   └── presentation/slides.md
│
├── README.md                         # Этот файл
├── .gitignore
├── LICENSE
└── CONTRIBUTING.md
```

## Архитектура

### Трёхзвенная архитектура
```
┌─────────────────────────┐
│   JavaFX Client (GUI)   │
├─────────────────────────┤
│  HTTP/REST (HttpClient) │
├─────────────────────────┤
│  Spring Boot Server     │
├─────────────────────────┤
│  JPA/Hibernate ↔ H2 БД  │
└─────────────────────────┘
```

**Подробнее**: см. [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md)

## API Эндпоинты

| Метод | Эндпоинт | Описание |
|-------|----------|---------|
| `GET` | `/api/cars` | Все авто (фильтры: `?q=модель`, `?type=тип`) |
| `GET` | `/api/cars/{id}` | Авто по ID |
| `POST` | `/api/cars` | Создать авто |
| `PUT` | `/api/cars/{id}` | Обновить авто |
| `DELETE` | `/api/cars/{id}` | Удалить авто |
| `GET` | `/api/cars/stats` | Статистика |

**Подробный формат**: см. [docs/API.md](docs/API.md)

## Функциональность

- CRUD операции
- Фильтрация (по модели/типу)
- Сортировка (по любой колонке)
- Статистика (ср./мин./макс. цена)
- Обработка ошибок (Alert-диалоги)
- Сохранение состояния окна
- JavaDoc документация
- ООП-принципы  

## Разработка

### Локальная сборка
```bash
mvn clean package -DskipTests
```

### IDE
- IntelliJ IDEA Community
- Eclipse
- VS Code + Extension Pack for Java

## Тестирование

```bash
cd server && mvn test
cd ../client && mvn test
```

## Документация

- [docs/poyasnitelnaya_zapiska.md](docs/poyasnitelnaya_zapiska.md) — Полная записка (~30 стр.)
- [docs/API.md](docs/API.md) — REST API
- [docs/ARCHITECTURE.md](docs/ARCHITECTURE.md) — Дизайн системы
- [docs/SETUP.md](docs/SETUP.md) — Установка

## Технологический стек

| Компонент | Технология | Версия |
|-----------|-----------|--------|
| JDK | OpenJDK | 17+ |
| Сервер | Spring Boot | 3.1.4 |
| БД | H2 + JPA | встроено |
| Клиент | JavaFX | 22 |
| Сборка | Maven | 3.9+ |

## Лицензия

MIT License

## Автор

**Студент**: Группа [ваша группа]  
**Факультет**: ИТ и анализа больших данных  
**ВУЗ**: Финансовый университет при Правительстве РФ

---

**Март 2026** | Готово к защите
