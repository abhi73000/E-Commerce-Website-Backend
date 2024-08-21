# E-Commerce Website-Backend

Welcome to the **E-Commerce Website**! This project is a Java-based e-commerce application developed using Spring Boot 3.x.x and managed with Maven. This README provides detailed instructions for setting up, running, and contributing to the project.


## Prerequisites

Ensure you have the following installed:

- [Java JDK 17](
- [Apache Maven]) -- (v3.8.0 or later)
- [IDE] -- Intellij (optional, but recommended for development)

## Installation

1. **Clone the repository:**

    ```bash
    git clone https://github.com/abhi73000/E-Commerce-Website-Backend.git
    cd your-ecommerce-project
    ```

2. **Install dependencies:**

    ```bash
    mvn install
    ```

## Configuration

Before running the application, you need to configure the following:

1. **Database Configuration:**

    Update the `src/main/resources/application.properties` with your database details:

    ```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/yourdatabase
    spring.datasource.username=yourusername
    spring.datasource.password=yourpassword
    spring.jpa.hibernate.ddl-auto=update
    ```

2. **Other Configurations:**

    Adjust any other application-specific settings as needed.

## Running the Application

To start the application locally:

```bash
mvn spring-boot:run
