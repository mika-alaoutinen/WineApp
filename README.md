# WineApp - Maistissa
This is the backend API for **WineApp**, implemented with Spring Boot. The frontend application can be found [here](https://github.com/mika-alaoutinen/WineApp-frontend).

## About WineApp

**WineApp** is a web application for keeping wine tasting notes. It started out as a Word file that grew to an unmanageable size, thus sparking the creation of this project. WineApp was the first "real" web application I ever developed, and as such it's been in the works for a long time. I think the application is currently in a usable, if
unpolished state.

## Running the application...
The recommended way to run the application is with Docker. To run the application locally without Docker, you will have to install Postgres and create a wine_app database.

You can use the application either by calling the API directly with Postman or by running the frontend application that connects to the API.

## ...Run with Docker
Running the project with Docker is simple:
1. Clone project.
2. Move to project folder.
3. Run Docker compose.
4. The application will run on port 8080. See the API documentation for description of available operations.

On command line:
```
git clone git@github.com:mika-alaoutinen/WineApp.git
cd WineApp
docker-compose up
```

## ...Run without Docker
Just don't do it.

## API documentation
The Swagger/OpenAPI documentation is available at
> localhost:8080/api/swagger

once the application is running. The document lists all available operations supported by the API.

## Further development ideas
- [ ] Add HATEOAS support.
- [ ] Implement more sophisticated user profile management.