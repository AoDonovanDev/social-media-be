# Social Media Backend
This project is a social media application built using Java, Spring Boot, and Maven. It includes various functionalities such as user authentication, user profile management, and post reactions. Here is a brief overview of the main components:  

## Controller Layer:  
- **AuthController**: Manages user authentication, including login and registration.

- **UserController**: Handles user-related operations such as fetching user profiles, updating user information, and managing followers.

- **PostController**: Manages operations related to posts, such as creating, updating, and deleting posts.
- **ReactionController**: Handles operations related to post reactions, such as creating, updating, and deleting reactions.
- **CommentController**: Manages operations related to comments, such as creating, updating, and deleting comments.
- **FriendController**: Handles operations related to friend management, such as sending friend requests, accepting requests, and removing friends.
- **S3Controller**: Manages operations related to AWS S3, such as uploading and deleting files.

## Service Layer:  
- **AuthService**: Contains the business logic for user authentication.
- **UserService**: Manages user data and operations like updating profile information and handling follow/unfollow actions.
- **PostService**: Manages operations related to posts.
- **ReactionService**: Handles operations related to post reactions, such as creating, updating, and deleting reactions.
- **CommentService**: Manages operations related to comments.
- **FriendService**: Handles operations related to friend management.
- **S3Service**: Manages operations related to AWS S3.

## Utility Layer:
- **Utility**: Provides helper methods, such as retrieving the logged-in user and mapping entities to response DTOs.

## Repository Layer:  
- **UserRepo**: Interface for database operations related to users.
- **PostRepo**: Interface for database operations related to posts.
- **ReactionRepo**: Interface for database operations related to reactions.
- **CommentRepo**: Interface for database operations related to comments.
- **FriendRepo**: Interface for database operations related to friends.

## DTOs (Data Transfer Objects):
- Various DTOs like UserResponse, UserProfileResponse, PostResponse, etc., are used to transfer data between the client and server.

## Exception Handling:  
- Custom exception handling is implemented in controllers to manage specific exceptions like UsernameNotFoundException. 

## CI/CD Pipeline:
This Jenkins pipeline automates the process of building, testing, and deploying a Java Spring Boot application. Here is a step-by-step explanation of each stage:

### Pipeline Overview
- **Agent**: Specifies that the pipeline can run on any available Jenkins agent.
- **Environment**: Defines environment variables, including AWS credentials, Docker credentials, and database connection details.
- **Tools**: Specifies the Maven version to use.
### Stages
#### Checkout Code:  
- Checks out the source code from the Git repository.
#### Build jar file:  
- Uses Maven to build the project and create a JAR file, skipping tests.
#### Build and push docker image:  
- Builds a Docker image for the application.
- Tags the Docker image.
- Logs into Docker Hub using the provided credentials.
- Pushes the Docker image to Docker Hub.
#### Remote into docker runner ec2, pull and run image:
- Connects to a remote EC2 instance using SSH.
- Stops and removes any running container named be.
- Pulls the latest Docker image from Docker Hub.
- Runs a new container with the pulled image, setting environment variables for AWS and database credentials.

### To run the Jenkinsfile, you need to set the following environment variables in your Jenkins environment:
| Environment Variable         | Description                                                      |
|------------------------------|------------------------------------------------------------------|
| AWS_DEFAULT_REGION           | The AWS region where your resources are located (e.g., us-east-2)|
| AWS_ACCOUNT_ID               | Your AWS account ID                                              |
| IMAGE_TAG                    | The tag for your Docker image (e.g., new-sm-img-22time)          |
| DOCKER_CREDENTIALS           | Jenkins credentials ID for Docker Hub credentials                |
| SPRING_DATASOURCE_URL        | Jenkins credentials ID for the Spring datasource URL             |
| SPRING_DATASOURCE_USERNAME   | Jenkins credentials ID for the Spring datasource username        |
| SPRING_DATASOURCE_PASSWORD   | Jenkins credentials ID for the Spring datasource password        |
| AWS_ACCESS_KEY_ID            | Jenkins credentials ID for the AWS access key ID                 |
| AWS_SECRET_ACCESS_KEY        | Jenkins credentials ID for the AWS secret access key             |
| backend-ec2-ssh-key          | Jenkins credentials ID for the SSH key to access the EC2 instance|
**Make sure these credentials are configured in Jenkins under "Manage Jenkins" -> "Manage Credentials".**

## Running Locally:
To run the application locally, you need to have Java, Maven, and MySQL installed on your machine. You also need to set up an AWS S3 bucket and configure the env.properties file with your database and AWS credentials. Here are the steps to run the application locally:
1. Clone the repository using the following command:
```bash
git clone https://github.com/bitsurevature/social-media-be.git 
```
2. Navigate to the project directory and setup env.properties file:
```bash
cd social-media-be
```
3. Build the project using Maven:
```bash
mvn clean install
```
4. Run the application:
```bash
java -jar target/social-media-be-0.0.1-SNAPSHOT.jar
```
5. The application will start on port 8080. You can access the Swagger documentation at http://localhost:8080/swagger-ui.html.
6. You can test the API endpoints using Postman or any other API testing tool.
7. To run the frontend application, follow the instructions in the frontend repository.
 
#### The project structure follows a typical Spring Boot application pattern, ensuring a clear separation of concerns and maintainability.

## Tools and Technologies:
| Tool/Technology    | Description                                      |
|--------------------|--------------------------------------------------|
| Java               | Programming language                             |
| Spring Boot        | Framework for building Java applications         |
| Spring Security    | Security framework for Spring applications       |
| Maven              | Build automation tool                            |
| MySQL              | Relational database management system            |
| AWS S3             | Cloud storage service                            |
| AWS EC2            | Cloud computing service                          |
| Postman / Insomnia | API testing tools                                |
| IntelliJ IDEA      | Integrated development environment (IDE)         |
| Git                | Version control system                           |
| GitHub             | Platform for version control and collaboration   |
| JUnit              | Testing framework for Java                       |
| Mockito            | Mocking framework for unit tests                 |
| Docker             | Containerization platform                        |
| Docker Hub         | Cloud-based repository for Docker images         |
| Swagger            | API documentation tool                           |
| Jenkins            | Continuous integration and delivery tool         |