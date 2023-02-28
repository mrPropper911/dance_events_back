# Dance Events 
## Spring boot application written for Android app
This application is written using software architecture REST API and has the following advantages: 
scalability, flexibility and independence.

### Technological stack
    - Java 16
    - Spring Boot as a skeleton framework
    - Spring Data
    - Spring Security + JWT
    - MySQL database as a database
    - Flyway database migration tool
    - phpMyAdmin administration tool for MySQL
    - Junit5 + AssertJ for testing
    - Docker

### Application.yml profiles
    To change the application execution profile
    - test (for local development)
    - docker (for docker development/prodaction)

### How to build application for Docker container
     1 To set your own variables, you need to create a file ".env" in the root of the 
    project (docker-compose.yml).
        This file must contain the following variables:
            DB_USERNAME=<ANY_NAME>
            DB_PASSWORD=<ANY_PASSWORD>
            DB_ROOT_PASSWORD=<ANY_ROOT_PASSWORD>
    2 run docker 
    3 Run console command in the root of the project: 
       --> ./start.sh (build/rebuild and start container)
       --> ./stop.sh (stor container)

### Help command for console in docker image MySQL:
    - mysql -uroot -p <database_name> (enter into database)


### License
    
    This project is Apache License 2.0 - see the [LICENSE](LICENSE) file for details