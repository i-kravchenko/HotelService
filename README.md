# Бронирование отелей

## Описание проекта
Сервис для администрирования и бронирования отелей

## Стэк используемых технологий
* Java
* Gradle
* Postgresql
* Redis
* MongoDB
* Kafka
* Zookeeper

## Инструкция
### Конфигурация проекта 
[Пример файла](src/main/resources/application.yaml)
#### Основные настройки
* spring.kafka.bootstrap-servers - адрес брокера Kafka 
* spring.data.redis.host - адрес сервера Redis
* spring.data.mongodb.uri - адрес сервера MongoDB
* spring.datasource.username - Пользователь базы данных
* spring.datasource.password - Пароль базы данных
* spring.datasource.url - Адрес сервера базы данных

Остальные параметры можно оставить без изменений

### Сборка и запуск приложения
После того как приложение было сконфигурировано, его нужно собрать.
Для этого введите в терминале команду ниже:
```shell
gradle build bootJar
```
После завершение сборки можно запустить проект.
Для запуска проекта внутри docker, подготовлен docker-compose.yaml файлы:
```shell
docker-compose -f ./docker/docker-compose.yaml up -d
```
