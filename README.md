
# README #

This README documents the steps needed to run or set up the application.

### What is this repository for? ###

This application is creating an multithreaded HTTP server in Java. 
The server can also be configured to run inside a docker container.

### Features
 - This server supports the following HTTP methods:   GET  & HEAD
 - Persistent connections through keep-alive. As per HTTP/1.1, this server supports **keep-alive** with a server specified timeout and the number of request.
   By default, the keep-alive feature is enabled and the details about the serving socket can be seen in the logs.
   In order to disable the keep-alive, pass the Connection Header with the value "close". See more information, see [here](https://developer.mozilla.org/en-US/docs/Web/HTTP/Headers/Connection). 
 - Multi-threading while request handling.
 - Customizable directory file browsing 

### How do I get set up? ###
Below are instructions on how to run the server via cmd or IDEs, how to configure it and so on.

## Setting up this repository
To configure this project in your local machine, please follow the following steps:

 1. Clone the project to your local machine.
 2. Download & configure Project Lombok into your IDE. See [Using Lombok in IDEs section](https://projectlombok.org/setup/overview).
 3. The first time import to your preferred IDE might require a maven install of the dependencies via command line, such as: `mvn clean install` .
 4. Run the server from the IDE using the instructions in the section above.

## How to start the server from command line?
To start the server from the main page with the default settings, run in cmd the following: 

    java -jar target/HTTPServer-1.0-SNAPSHOT.jar
which will load the files from the default directory *./pages/pages_1*.

This will start the server using the default setting: the port 8080 and a document root directory.

However, to specify your own directory with your own files, please use the command below:
   

     java -jar target/HTTPServer-1.0-SNAPSHOT.jar -i <input_path_to_your_directory>

Please substitute *<input_path_to_your_directory>* with your desired directory path.

When the server runs, you can browse the specified default directory by visiting http://localhost:8080/ with a GET and/or HEAD method/verb either using a browser or Postman.

## How to start the server from IDE (Eclipse, IntelliJ, etc)
Navigate to class: `com.adobe.project.application.HttpServerLauncher`
and run the application with *Run As: Java Application*.
This will run the server using the default file directory. If another directory needs to be loaded, then in the *Run Configurations...* option set up the Program arguments as below:

    -i=<input_path_to_your_directory>

## Dependencies
	* Java 8
	* Maven 3
	* Lombok Project 1.18.12
	
## Deployment instructions with Docker

Requirements:
1. Docker installed in the machine. You can download the docker from the [homepage](https://www.docker.com/products/docker-desktop).
2. [Optional] Make sure that the docker configuration supports port forwarding.

Steps:
1. Make sure that you have the jar in folder **target** with the name **HTTPServer-1.0-SNAPSHOT.jar**.
1. Make sure that you pages folder as mentioned in this repository. 
1. Go to the root folder of the project (folder where the Dockerfile is accessible), and run
    ```shell script
    docker build -t adobe/httpserver .
    ```
1. The above command will build a docker image for you.
1. In order to run this docker image, use the commmand:
    ```shell script
    docker run --name adobe_server -p 8080:8080 -t adobe/httpserver
    ```
   The above command starts the docker with the default directory, in order to run the server with a different directory use:
   ```shell script
    docker run --name adobe_server -p 8080:8080 -t adobe/httpserver -i ./pages/pages_2
   ```
   We don't check if the directory provided by the user exists or not.
1. The above command runs the docker with the container name *adobe_server* and exposes the port 8080 from the docker container to the host machine. Currently, the server port is not configurable, but you can use different port on the host machine using the following:
    ```shell script
     docker run --name adobe_server -p <your_host_port>:8080 -t adobe/httpserver
    ```
   Through-out the set-up we are using 8080 as the host port. Make sure that the port 8080 is currently unoccupied in the host machine. 
1. The above command will start the server and you can see the logs in the command line.
1. Go to your browser or your postman app and make a request HEAD/GET at http://localhost:8080. You should be welcomed to the homepage.
1. You can also make a curl call through command line with the headers:
   ```shell script
    curl -X GET http://localhost:8080/ -H 'cache-control: no-cache' -H 'connection: Keep-Alive'
    ```
1. You should see the response.
1. [Optional] You can go inside the container using the following command:
    ```shell script
    docker exec -it adobe_server ash
    ```
1. Clean-up your docker:
    1. Stop the running server using:
        ```shell script
        docker stop adobe_server
        ```
    1. Once you are done playing with the server, you can clean-up your running docker container by:
        ```shell script
        docker rm adobe_server
        ```
    1. You can clean up your image which was created by using:
        ```shell script
        docker image ls
        ```
        pick up the image id of the image which has the repository name as adobe/httpserver. Delete that image using:
        ```shell script
        docker image rm <image_id>
        ```


### Who do I talk to? ###

* Aman Sinha, e-mail to: [amansinh@gmail.com](mailto:amansinh@gmail.com).


### References
* [University lecture: Design and Implementation of an HTTP Server](https://users.cs.jmu.edu/bernstdh/web/common/lectures/slides_http-server-example_java.php)
* [Developer Mozilla: HTTP](https://developer.mozilla.org/en-US/docs/Web/HTTP)
* [Create a simple HTTP Web Server in Java](https://www.youtube.com/watch?v=LJjIaCKuzoc&feature=emb_logo&ab_channel=SylvainSaurel)