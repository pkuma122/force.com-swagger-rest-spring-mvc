# Exposing Force.com RESTful Services using Swagger

## What is Swagger?
Swagger is a specification and complete framework implementation for describing, producing, consuming, and visualizing RESTful web services.

## Describing your REST Endpoints
Describing your end point providing contextual metadata on what the purpose is of the service end point. How providing an interface to enter request parameters or provide the structure of the accepted JSON. After the service end point has been invoked output will be in the specified format.This provides a easier way to develop, test and document your service end points.

## Developing and Consuming your REST Endpoints
Developing of services is easy through the use of Swagger specific annotations you can create your service end point documentation in minutes. Consuming of services through the visual interface takes the time to run command line, provides output in an easy to read format, provide error codes and possible descriptions on what the may mean.

## Visualizing your REST Endpoints
The visualizing of service end point provides an great overview of all the service end points, all the services requests per end point and also metadata about service each end points.

## Running the application locally

Setup OAuth Remote Access in Salesforce.com

    1. Go to Salesforce.com's Setup page
    2. Go to Develop -> Remote Access
    3. Add a new Remote Access config with a URL of: `http://localhost:8080/_auth`

Add environment variables for authenticating to Salesforce.com (replace the values with the ones from the Remote Access definition on Salesforce.com):

- On Linux/Mac:

        $ export OAUTH_CLIENT_KEY=3MVM3_GuVCQ3gmEE5al72RmBfiAWhBX5O2wYc9zTZ8ytj1E3NF7grV_G99OxTyEcY71Tc46TOvzK_rzoyYYPk
        $ export OAUTH_CLIENT_SECRET=1319558946720906100

- On Windows:

        $ set OAUTH_CLIENT_KEY=3MVM3_GuVCQ3gmEE5al72RmBfiAWhBX5O2wYc9zTZ8ytj1E3NF7grV_G99OxTyEcY71Tc46TOvzK_rzoyYYPk
        $ set OAUTH_CLIENT_SECRET=1319558946720906100

Build with:

    $ mvn clean install

Then run it with:

    $ java -jar target/dependency/webapp-runner.jar target/*.war


## Running on Heroku

Clone this project locally:

    $ git clone git://github.com/thysmichels/force.com-swagger-rest-spring-mvc-heroku.git

Create a new app on Heroku (make sure you have the [Heroku Toolbelt](http://toolbelt.heroku.com) installed):

    $ heroku login
    $ heroku create -s cedar

Setup OAuth Remote Access in Salesforce.com

    1. Go to Salesforce.com's Setup page
    2. Go to Develop -> Remote Access
    3. Add a new Remote Access config with a URL of: `https://your-app-1234.herokuapp.com/_auth`

Add config params for authenticating to Salesforce.com (replace the values with the ones from the Remote Access definition on Salesforce.com):

    $ heroku config:add OAUTH_CLIENT_KEY=3MVM3_GuVCQ3gmEE5al72RmBfiAWhBX5O2wYc9zTZ8ytj1E3NF7grV_G99OxTyEcY71Tc46TOvzK_rzoyYYPk OAUTH_CLIENT_SECRET=1319558946720906100

Upload the app to Heroku:

    $ git push heroku master

Open the app in your browser:

    $ heroku open

