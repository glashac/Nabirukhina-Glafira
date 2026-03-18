# Подробная инструкция установки и запуска

## Содержание
1. [Требования](#требования)
2. [Установка JDK 17](#установка-jdk-17)
3. [Установка Maven](#установка-maven)
4. [Клонирование репозитория](#клонирование-репозитория)
5. [Запуск сервера](#запуск-сервера)
6. [Запуск клиента](#запуск-клиента)
7. [Проверка работоспособности](#проверка-работоспособности)
8. [Решение проблем](#решение-проблем)

## Требования

### Windows 10/11
- Интернет-соединение
- ~2 GB свободного места на диске
- Права администратора (для установки ПО)

### macOS
- Xcode Command Line Tools (установить: `xcode-select --install`)

### Linux (Ubuntu/Debian)
- build-essential (установить: `sudo apt-get install build-essential`)

## Установка JDK 17

### Windows

1. Скачайте JDK 17 с [Oracle](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) или используйте OpenJDK:
   ```powershell
   winget install --id EclipseAdoptium.Temurin.17
   ```

2. Проверьте установку:
   ```powershell
   java -version
   javac -version
   ```

   Должно вывести что-то типа:
   ```
   java version "17.0.x"
   javac 17.0.x
   ```

3. Установите переменную окружения (если не установлена):
   - Откройте "Переменные окружения" (Environment Variables)
   - Кликните "Новая" (New) → Переменная пользователя
   - Имя: `JAVA_HOME`
   - Значение: `C:\Program Files\Eclipse Adoptium\jdk-17.0.x` (путь к вашему JDK)
   - OK → OK

### macOS

```bash
brew install openjdk@17
# Создайте символическую ссылку:
sudo ln -sfn /usr/local/opt/openjdk@17/libexec/openjdk.jdk /Library/Java/JavaVirtualMachines/openjdk-17.jdk
java -version
```

### Linux

```bash
sudo apt-get update
sudo apt-get install openjdk-17-jdk
java -version
```

## Установка Maven

### Windows

1. Скачайте Maven 3.9+ с [apache.org](https://maven.apache.org/download.cgi)
2. Распакуйте в папку (например, `C:\apache-maven-3.9.x`)
3. Добавьте путь в PATH:
   - Переменные окружения → Путь (Path) → Изменить (Edit)
   - Добавить: `C:\apache-maven-3.9.x\bin`
   - OK → OK → Перезагрузитесь

4. Проверьте установку:
   ```powershell
   mvn -version
   ```

### macOS

```bash
brew install maven
mvn -version
```

### Linux

```bash
sudo apt-get install maven
mvn -version
```

## Клонирование репозитория

### С помощью Git (рекомендуется)

1. Установите Git:
   - Windows: https://git-scm.com/download/win
   - macOS: `brew install git`
   - Linux: `sudo apt-get install git`

2. Клонируйте репозиторий:
   ```bash
   git clone https://github.com/glashac/Nabirukhina-Glafira.git
   cd Nabirukhina-Glafira
   ```

### Без Git (просто скачайте ZIP)

1. Откройте https://github.com/glashac/Nabirukhina-Glafira
2. Нажмите "Code" → "Download ZIP"
3. Распакуйте в папку
4. Откройте терминал в этой папке

## Запуск сервера

### Способ 1: Maven (рекомендуется для разработки)

```bash
cd server
mvn spring-boot:run
```

**Ожидаемый вывод:**
```
...
Application 'Car Rental Server' is running! Access URLs:
  Local:   http://localhost:8080
...
```

Сервер будет доступен на `http://localhost:8080`.

### Способ 2: Сборка JAR и запуск

```bash
cd server
mvn clean package -DskipTests
java -jar target/car-rental-server-0.1.0.jar
```

### Проверка сервера

В новом терминале:
```bash
curl http://localhost:8080/api/cars
```

Или откройте в браузере: `http://localhost:8080/api/cars`

Должен вернуться JSON:
```json
[
  {
    "id": 1,
    "model": "Midnight Marauder",
    "type": "Новинки",
    "year": 2012,
    "pricePerDay": 499014700.0,
    "available": true
  },
  ...
]
```

## Запуск клиента

### Способ 1: Maven (рекомендуется)

В **новом терминале** (сервер должен работать):

```bash
cd client
mvn javafx:run
```

**Ожидаемый результат:**
- Откроется окно JavaFX приложения
- Таблица с 4 автомобилями

### Способ 2: Запуск из IDE

**IntelliJ IDEA:**
1. File → Open → выберите папку `client`
2. Дождитесь индексации
3. Right-click на `MainApp.java`
4. Run 'MainApp.main()'

**Eclipse:**
1. File → Import → Existing Maven Projects
2. Выберите папку `client`
3. Finish, дождитесь сборки
4. Right-click на `MainApp.java`
5. Run As → Java Application

**VS Code:**
1. Установите Extension Pack for Java
2. Откройте проект
3. Click на "Run/Debug" над методом `main` в `MainApp.java`

## Проверка работоспособности

### Тест CRUD операций

1. **Добавить авто:**
   - Нажмите "Добавить"
   - Заполните: Модель: "Test", Тип: "Test", Год: 2025, Цена: 1000
   - Нажмите OK
   - Проверьте, появилось ли в таблице

2. **Редактировать авто:**
   - Выберите авто в таблице
   - Нажмите "Редактировать"
   - Измените цену на 2000
   - Нажмите OK
   - Проверьте обновление

3. **Удалить авто:**
   - Выберите авто
   - Нажмите "Удалить"
   - Подтвердите
   - Проверьте удаление

4. **Фильтация:**
   - Введите в поле поиска: "Velocity"
   - Нажмите Enter
   - Должно остаться только 1 авто (Velocity Vortex)

5. **Статистика:**
   - Нажмите "Обновить статистику"
   - Должна отобразиться статистика

### Тест API (curl/Postman)

```bash
# Получить все авто
curl http://localhost:8080/api/cars

# Получить авто по ID
curl http://localhost:8080/api/cars/1

# Получить статистику
curl http://localhost:8080/api/cars/stats

# Создать авто (JSON body)
curl -X POST http://localhost:8080/api/cars \
  -H "Content-Type: application/json" \
  -d '{"model":"Test","type":"Test","year":2025,"pricePerDay":1000,"available":true}'
```

## Решение проблем

### "Java: command not found" (macOS/Linux)

```bash
# Проверьте путь к Java
which java

# Если не найдено, установите:
sudo apt-get install openjdk-17-jdk  # Linux
brew install openjdk@17              # macOS
```

### "Maven: command not found"

```bash
# Проверьте путь
which mvn

# Если не работает, переустановите PATH:
# Windows: добавьте C:\apache-maven-x.x.x\bin в PATH
# macOS: brew install maven
# Linux: sudo apt-get install maven
```

### "Не могу клонировать репозиторий" (ошибка GitHub SSH)

```bash
# Используйте HTTPS вместо SSH:
git clone https://github.com/glashac/Nabirukhina-Glafira.git

# Если ошибка "Permission denied", авторизуйтесь:
git config --global user.name "Your Name"
git config --global user.email "your@email.com"
```

### "Port 8080 already in use" (сервер не запускается)

```bash
# Убейте процесс на порту 8080
# Windows (PowerShell):
$p = Get-Process | Where-Object { $_.Name -eq 'java' }; $p | Stop-Process -Force

# macOS/Linux:
lsof -ti:8080 | xargs kill -9

# Или используйте другой порт:
# отредактируйте server/src/main/resources/application.properties
# server.port=8081
```

### "JavaFX: no suitable driver found" (проблема с графикой)

```bash
# Если используете WSL на Windows:
# Установите XServer (например, VcXsrv) и запустите:
export DISPLAY=:0
mvn javafx:run

# Или используйте WSL 2 с WSLg (в новых Windows 11)
```

### "Connection refused: http://localhost:8080" (клиент не подключается)

1. Убедитесь, что сервер запущен (`mvn spring-boot:run` в первом терминале)
2. Проверьте логи сервера
3. Проверьте порт: `curl http://localhost:8080/api/cars`
4. Если порт занят, измените в `application.properties`: `server.port=8081`

### "Error: could not find or load main class"

```bash
# Проверьте структуру проекта
ls -la server/src/main/java/ru/fa/carrental/

# Пересоберите:
mvn clean compile
mvn spring-boot:run
```

## Логирование и отладка

### Включить DEBUG логирование

Отредактируйте `server/src/main/resources/application.properties`:
```properties
logging.level.root=INFO
logging.level.ru.fa.carrental=DEBUG
logging.level.org.springframework.web=DEBUG
```

### Просмотреть H2 console

1. Откройте: `http://localhost:8080/h2-console`
2. Подключитесь:
   - JDBC URL: `jdbc:h2:mem:car_rental`
   - User Name: `sa`
   - Password: (пусто)
3. Нажмите "Connect"
4. Просмотрите таблицу `cars`

## Дополнительные команды Maven

```bash
# Только скомпилировать (без запуска)
mvn compile

# Запустить тесты
mvn test

# Собрать JAR
mvn package

# Полная переборка (удалить target/)
mvn clean

# Запустить IntelliIJ IDE инспектор (если установлена)
mvn sonar:sonar
```

## Как выйти из приложения

### Сервер
Нажмите `Ctrl+C` в терминале где работает Maven

### Клиент
Нажмите кнопку закрытия окна (X) или `Alt+F4`

---

**Успешно установили?** Поздравляем!  
Теперь выполняйте CRUD операции, фильтруйте, смотрите статистику и готовьтесь к защите!
