# Инструкция по загрузке проекта на GitHub

## Шаг за шагом: Отправка проекта в репозиторий

### Предварительно

Убедитесь, что:
1. Git установлен: `git --version` → должна быть версия 2.30+
2. Репозиторий на GitHub создан: https://github.com/glashac/Nabirukhina-Glafira
3. Вы залогинены в GitHub (или сгенерировали Personal Access Token)

---

## Способ 1: Инициализация нового репозитория (если локально еще нет git)

### Если проект еще не в git

```bash
# Перейдите в папку проекта
cd C:\Users\81090370\car-rental-java

# Или используйте полный путь
cd Nabirukhina-Glafira
```

### Инициализируйте Git

```bash
# Создайте .git директорию
git init

# Проверьте статус
git status
```

---

## Способ 2: Если уже есть локальный git, свяжите с GitHub

### Проверьте текущий remote

```bash
git remote -v
```

**Если вывод пустой** → remote не настроен, добавьте его:

```bash
git remote add origin https://github.com/glashac/Nabirukhina-Glafira.git
```

**Если remote уже существует**:

```bash
git remote set-url origin https://github.com/glashac/Nabirukhina-Glafira.git
```

### Проверьте, что все правильно

```bash
git remote -v
# Должно вывести:
# origin  https://github.com/glashac/Nabirukhina-Glafira.git (fetch)
# origin  https://github.com/glashac/Nabirukhina-Glafira.git (push)
```

---

## Шаг 1: Подготовка файлов

### Убедитесь, что есть .gitignore

```bash
# Проверьте, есть ли файл
ls -la | grep ".gitignore"  # на macOS/Linux
dir | findstr ".gitignore"   # на Windows

# Если нет, файл уже создан в docs/.gitignore в этом проекте
```

### Проверьте структуру проекта

```bash
# Должны быть папки:
ls -la
# server/
# client/
# docs/
# README.md
# .gitignore
# LICENSE
# CONTRIBUTING.md
```

---

## Шаг 2: Добавьте файлы в индекс Git

### Добавьте все файлы

```bash
# Добавить все файлы
git add .

# Проверить статус
git status
```

**Ожидаемый вывод:**
```
On branch master

No commits yet

Changes to be committed:
  new file:   .gitignore
  new file:   CONTRIBUTING.md
  new file:   LICENSE
  new file:   README.md
  new file:   client/pom.xml
  new file:   client/src/main/java/...
  ...
```

### Если нужно исключить файлы

```bash
# Удалить target/ директории (не нужны в git)
git reset HEAD target/

# Или отредактируйте .gitignore и снова сделайте add
```

---

## Шаг 3: Создайте первый коммит

```bash
git commit -m "Initial commit: Car Rental System coursework project

- Three-tier architecture (Client-Server-DB)
- Server: Spring Boot 3 + REST API + JPA/H2
- Client: JavaFX GUI with CRUD operations
- Documentation: README, API, ARCHITECTURE, SETUP guides
- Fully compliant with coursework requirements"
```

**Проверьте коммит:**
```bash
git log
```

---

## Шаг 4: Установите ветку main (или master)

```bash
# Проверьте текущую ветку
git branch

# Если ветка называется 'master', переименуйте на 'main' (GitHub standard)
git branch -M main

# Проверьте результат
git branch
# Должна быть: * main
```

---

## Шаг 5: Отправьте на GitHub

### Первый раз (с установкой tracking)

```bash
git push -u origin main
```

**Если Git просит авторизацию:**
- Windows: выберите "Sign in with your browser" (откроется браузер)
- macOS/Linux: введите Personal Access Token (как пароль)

**Если ошибка "fatal: couldn't read... Permission denied":**
```bash
# Попробуйте HTTPS вместо SSH
git remote set-url origin https://github.com/glashac/Nabirukhina-Glafira.git
git push -u origin main
```

### Последующие обновления

```bash
git push origin main
# или просто
git push
```

---

## Шаг 6: Проверьте на GitHub

1. Откройте https://github.com/glashac/Nabirukhina-Glafira
2. Должны появиться файлы:
   - server/
   - client/
   - docs/ (с файлами: poyasnitelnaya_zapiska.md, API.md, ARCHITECTURE.md, SETUP.md)
   - README.md
   - .gitignore
   - LICENSE
   - CONTRIBUTING.md

3. Проверьте, что README.md отображается как главная страница (зеленая галочка ✓)

---

## Дополнительные команды Git

### Проверить логи

```bash
git log --oneline
```

### Сделать еще один коммит (после изменений)

```bash
# Измените файлы...
git add .
git commit -m "feat: Add feature description"
git push
```

### Если случайно коммитили что-то лишнее

```bash
# Отменить последний коммит (но сохранить файлы)
git reset --soft HEAD~1

# Отменить последний коммит (удалить файлы)
git reset --hard HEAD~1
```

### Посмотреть изменения перед коммитом

```bash
git diff
```

### Удалить файл из git (но оставить локально)

```bash
git rm --cached filename
```

---

## Проверка корректности структуры

### После первого push в GitHub, проверьте:

1. **Все ли папки загружены:**
   ```bash
   git ls-remote --heads origin
   ```

2. **Проверьте содержимое:**
   - GitHub → Code → выберите файлы
   - Должны быть: server/pom.xml, client/pom.xml, docs/

3. **Проверьте README стилизацию:**
   - GitHub должен автоматически отформатировать README.md
   - Заголовки #, списки, таблицы должны отображаться правильно

---

## Решение проблем

### "fatal: remote origin already exists"

```bash
git remote remove origin
git remote add origin https://github.com/glashac/Nabirukhina-Glafira.git
```

### "Permission denied (publickey)"

```bash
# Используйте HTTPS вместо SSH
git remote set-url origin https://github.com/glashac/Nabirukhina-Glafira.git

# Если не помогло, сгенерируйте Personal Access Token:
# GitHub Settings → Developer settings → Personal access tokens → Generate new token
# Скопируйте токен и используйте как пароль при git push
```

### "Everything is in sync" (но файлы не видны)

```bash
# Обновите локально
git pull origin main

# Проверьте статус
git status
```

### ".gitignore не работает (файлы все равно в git)"

```bash
# Удалите из git кэша
git rm -r --cached .
git add .
git commit -m "Build: Remove cached compiled files"
git push
```

---

## Финальная проверка

После успешного push выполните:

```bash
# Клонируйте свежую копию проекта (имитация нового пользователя)
cd /tmp  # или C:\temp
git clone https://github.com/glashac/Nabirukhina-Glafira.git test-clone
cd test-clone

# Проверьте структуру
ls -la
# Должны быть все папки и файлы

# Попробуйте запустить
cd server && mvn compile  # должно скомпилироваться без ошибок
```

**Если все работает — проект успешно загружен на GitHub!**

---

## Что дальше?

1. **Добавьте топ README для быстрого старта:**
   - Пользователи смогут за 3 минуты запустить проект

2. **Настройте GitHub Actions (CI/CD):**
   - Автоматические тесты при push

3. **Используйте Projects для отслеживания:**
   - GitHub Projects → New project → отслеживайте задачи

4. **Добавьте Issues и Discussions:**
   - Дайте возможность сообщать об ошибках

5. **Защитите main-ветку:**
   - Settings → Branches → Require pull request reviews

---

## Командн Git на шпоргалке

```bash
# Начало
git init                                    # Инициализация
git remote add origin <URL>                 # Добавить remote
git clone <URL>                             # Клонирование

# Работа
git status                                  # Статус
git add .                                   # Добавить все файлы
git commit -m "message"                     # Коммит
git push                                    # Отправить
git pull                                    # Получить изменения

# Ветки
git branch                                  # Список веток
git checkout -b <name>                      # Создать ветку
git checkout <branch>                       # Переключиться
git merge <branch>                          # Слить ветки

# История
git log                                     # Логи коммитов
git diff                                    # Различия
git reset --hard HEAD~1                     # Отменить последний коммит
```

---

**Поздравляем!**
Ваш проект теперь на GitHub и готов к защите!

Если возникли вопросы — свяжитесь через Issues на GitHub.
