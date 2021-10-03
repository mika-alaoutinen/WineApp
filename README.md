# WineApp - Maistissa

This is the backend API for **WineApp**, implemented with Spring Boot. The frontend application can be
found [here](https://github.com/mika-alaoutinen/WineApp-frontend).

## About WineApp

**WineApp** is a web application for keeping wine tasting notes. It started out as a Word file that grew to an
unmanageable size, thus sparking the creation of this project. WineApp was the first "real" web application I ever
developed, and as such it's been in the works for a long time. I think the application is currently in a usable, if
unpolished state.

## Running the application

The recommended way to run the application is with Docker. To run the application, follow the steps below.

1. Clone project.
2. Move to project folder.
3. Run Docker compose.
4. The application runs on port 8082. See the API documentation for description of available operations.

On command line:

```
git clone git@github.com:mika-alaoutinen/WineApp.git
cd WineApp
docker-compose up
```

## API documentation

An OpenAPI document describing how the application is used can be found in its
own [repository](https://raw.githubusercontent.com/mika-alaoutinen/maistissa-openapi/master/openapi.json).

## Further development ideas

- [ ] Add HATEOAS support.
- [ ] Implement more sophisticated user profile management.