# Monitoring-Service

## Функциональность

Пользователь :

- Регистарция пользователей.
- Аунтификация пользователей.
- Получения актуальных показаний счетчиков.
- Подачи показаний счетчиков.
- Просмотр показаний счетчика за конкретный месяц.
- Просмотра истории подачи показаний.

Администратор :

- Просмотр всех пользователей системы
- Просмотр аудита пользователя

## Использование

Клонируем проект

 ```bash
git fetch origin pull/3/head:<local_branch>
```

 ```bash
git switch <local_branch>
```

Поднимаем базу, вызвав команду в корне проекта

 ```bash
docker compose up
```

Создаем две схемы : 'liquibase' для хранения служебных таблиц, monitoring_service для хранения сущностей

 ```bash
mvn liquibase:update
```

Упаковываем проект в .war файл

 ```bash
mvn package
```

| Method   | URI                                                                          | Description                                            |
|----------|------------------------------------------------------------------------------|--------------------------------------------------------|
| get      | /monitoring-service/api/v1/water-meter-reading/users/{id}?monthValue=value   | поиск показаьтеля счетчика воды пользователя за месяц  |
| get      | /monitoring-service/api/v1/thermal-meter-reading/users/{id}?monthValue=value | поиск показаьтеля счетчика тепла пользователя за месяц |
| get      | /monitoring-service/api/v1/users/{id}                                        | отображает конкретного пользователя                    |
| get      | /monitoring-service/api/v1/water-meter-readings/all/users/{id}               | все показания счетчика воды пользователя               |
| get      | /monitoring-service/api/v1/water-meter-readings/actual/users/{id}            | последнее показание счетчика воды пользователя         |
| get      | /monitoring-service/api/v1/thermal-meter-readings/actual/users/{id}          | последнее показание счетчика тепла пользователя        |
| get      | /monitoring-service/api/v1/thermal-meter-readings/all/users/{id}             | все показания счетчика тепла пользователя              |
| post (1) | /monitoring-service/api/v1/water-meter-readings                              | отправить показание счетчика воды                      |
| post (2) | /monitoring-service/api/v1/thermal-meter-readings                            | отправить показание счетчика тепла                     |
| post (3) | /monitoring-service/api/v1/users/registration                                | регистрация пользователя                               |
| post (4) | /monitoring-service/api/v1/users/authentication                              | аутентификация пользователя                            |
| post     | /monitoring-service/api/v1/users/logout                                      | разлогирование пользователя                            |

Examples of request (1)

```
{
	"userId": "2",
    "coldWater" : "100",
    "hotWater" : "50"
}
```

Examples of request (2)

```
{
	"userId": "2",
    "gigaCalories" : "1000"
}
```

Examples of request (3)

```
{
	"firstname": "firstname",
    "email" : "someUnique@gmail.com",
    "password" : "pass",
    "personalAccount" : "111111111",
    "city" : "City",
    "street" : "street",
    "houseNumber" : "16"
}
```

Examples of request (4)

```
{
    "email" : "uniqueEmail@gmail.com",
    "password" : "pass"
}
```

## Технологии

* Java core
* JUnit 5 (assertJ, Mockito)
* JDBC API
