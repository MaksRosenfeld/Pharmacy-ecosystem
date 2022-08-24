# Pharmacy-ecosystem
ENG/RU

**Demo**
https://budget-pharmacy-statistic.herokuapp.com/

**login**:guest **pw**:iamtheguest

**[ENG]**

This is my first project and not everything is finished. Trying to add smthg or refactor every day. 
The main idea is to count all the financial indicators of the organization that deals with pharmacies. 

So the service using WebFlux connects with API to bank and 1C endpoints. As statements are ordered there are consistent checking of the readiness of
the bank statement. As it's done service fetches data from both, parses it and then works with the results, extracting and distribuing 
costs to pharmacies. When PharmacyResults are collected there starts the process of counting main financial indicators such as gross profit,
net profit, return of sales, salary fond.

As all is ready data is shown via ChartJS and also there is the possibility to keep the data in DB or leave it unsafe.

**[RU]**

Мой первый проект, в данный момент выложен на Heroku, пока что без привязки к домену. В планах дорабатывать и рефакторить.
Основная идея - подсчет финансовых показателей аптечной сети, используя базу 1С и Банк. С помощью WebFlux идет асинхронное подключение
к API банка и 1С базы (1С принимает только локальные запросы внутри сети организации). Идет постоянная отправка запросов на эндпоинты банка, так
как выписка готовится в течение какого-то времени. Далее данные собираются и парсятся. Идет распределение расходов по аптечной базе, проверка на 
недостающие ИНН, сопоставление категорий расходов. Как только подготовительные данные готовы, начинает работать подсчет всех основных 
финансовых и экономических показателей.

Все готовые данные представлены в виде таблиц ChartJS, также есть возможность сохранить результаты для каждой аптеки и организации в целом в БД

**Main technology stack**: 
back - Java 17, Spring Boot, Spring Data Jpa, Spring MVC, Spring Security, Spring WebFlux, Postgres, Hibernate ORM, Lombok, Junit 5, Maven, Heroku
front - Html, Css, JavaScript, jQuery, Thymeleaf

