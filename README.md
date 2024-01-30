## Использованные технологии

### Язык программирования
- **Java 17**

### Фреймворки
- **Spring Boot**  (версия **3.0.8**)
  - **Spring Boot Starter Data JPA**
  - **Spring Boot Starter Security**
  - **Spring Boot Starter Web**
  - **Spring Boot Starter Test**

### Библиотеки для работы с БД
- **Liquibase** (версия **4.25.0**)
- **PostgreSQL** (версия **42.7.1**)
- **H2 Database** (версия **2.2.224**), используется только в тестах
- **Hibernate** (версия **6.1.7.Final**), в составе *Spring Boot Starter Data JPA*

### Прочие библиотеки
- **Jackson** (версия **2.14.3**), в составе *Spring Boot Starter Web* и *Spring Boot Starter Security*
- **Mockito** (версия **4.8.1**), в составе *Spring Boot Starter Test*
- **Lombok** (версия **1.18.30**)
- **Logback** (версия **1.4.8**), в составе *Spring Boot Starter Logging* (входит в *Spring Boot Starter Security*)
- **Springdoc OpenAPI** (версия **2.3.0**)

### Прочие технологии
- **Docker**
  - **Контейнер с PostgreSQL** (образ **postgres:14.8-alpine3.18**)
  - **Контейнер с pgAdmin** (образ **dpage/pgadmin4:7.2**)
  - **Контейнер с Maven** (образ **maven:3.9.5-amazoncorretto-17**)
  - **Контейнер с Apache Tomcat** (образ **tomcat:10-jre17**)

<br><br>
## Инструкция по запуску

1. Убедиться, что на устройстве установлен и запущен Docker - он необходим для развёртывания приложения.
1. Открыть консоль и выполнить команду ```git clone https://github.com/Ridireacht/SenlaProjectFinal.git```, чтобы скопировать данный репозиторий на своё устройство.
1. Выполнить команду ```cd SenlaProjectFinal```, чтобы перейти в директорию приложения.
1. Выполнить команду ```docker-compose up -d```, чтобы создать контейнеры приложения и запустить их в фоновом режиме.
1. Выполнить команду ```docker-compose logs -f```, чтобы в консоль выводились логи от контейнеров. Изменение логов отслеживается в реальном времени. Чтобы приостановить вывод логов, нужно нажать Ctrl+C.
1. Для прекращения работы нужно выполнить команду ```docker-compose down```, что уничтожит контейнеры, но сохранит данные.

Доступ к приложению осуществляется по адресу ```localhost:8080```. На ```localhost:5050``` расположен pgAdmin4, который позволяет взаимодействовать с используемой в проекте БД. По адресу ```localhost:8080/v3/api-docs``` расположена API-документация в формате JSON, по адресу ```localhost:8080/swagger-ui/index.html``` находится Swagger UI, обе страницы доступны неаутентифицированным пользователям.

<br><br>
## Диаграмма БД
