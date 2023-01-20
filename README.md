# Dance events (backend)
Backend for android app (dance events) written on Java 16 with Spring Framework, using MySQL database and Docker for running application.

### How to build application for docker container
    - run command in console -> ./start.sh (build/rebuild and start container)
    - run command in console -> ./stop.sh (stor container)

### Application.yml profiles
    - test (local test)
    - docker (packege prject to docker image)

### Help command for console docker MySQL:
    - mysql -uroot -p <database_name> (enter into database)