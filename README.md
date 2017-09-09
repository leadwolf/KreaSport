# Kreasport

Android app for orienteering races with Spring server to host and manage the data. Created as part of an Erasmus project work. Read the [project report](doc/Kreasport%20Report.pdf) to learn more.

## Built With

* [Spring](https://spring.io/) - The web framework used for security, data and authentication.
* [Maven](https://maven.apache.org/) - Dependency Management.
* [Auth0](https://auth0.com/) - Used to authenticate users.
* [MongoDB](https://www.mongodb.com/) - Document based database structure.
* [Heroku](https://www.heroku.com/) - Used to deploy the servers.
* [Realm](https://realm.io/) - Used to store data with the Android app.
* [Osmdroid](https://github.com/osmdroid/osmdroid) - Mapping solution for the Android app.

## Authors

* **Christopher Caroni** - *Idea and project maintainer* - [Github](https://github.com/Christopher-Caroni)

## License

This project is licensed under the GNU General Public License v3.0 - see the [LICENSE](LICENSE) file for details

## Project structure

 - **KreaSport**: This folder contains the Android app
 - **kreasport-backend-server**: contains the server handling request with JWTs for trusted clients such as the Android app. Interacts directly with the database.
 - **kreasport-server**: contains the server handling request authenticated by the user session. Used by the admin to manage the database.

The two servers are because of a Maven conflict with Auth0 dependencies.