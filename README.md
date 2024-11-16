# Hlæja Device Register

Classes crafted, identities bestowed, Each device recorded, their functions unfold. From sensors to systems, connections take shape, A registry formed, no detail escapes. Signals exchanged, precision is key, Each device declared, its purpose set free. The Device Register, steadfast and true, A harmony of devices, perfectly aligned.

## Properties for deployment

| name                   | required | info                    |
|------------------------|----------|-------------------------|
| spring.profiles.active | *        | Spring Boot environment |
| spring.r2dbc.url       | *        | Postgreas host url      |
| spring.r2dbc.username  | *        | Postgreas username      |
| spring.r2dbc.password  | **       | Postgreas password      |

Required: * can be stored as text, and ** need to be stored as secret.  

## Releasing Service

Run `release.sh` script from `master` branch.

## Development Configuration

### Global gradle properties

To authenticate with Gradle to access repositories that require authentication, you can set your user and token in the `gradle.properties` file.

Here's how you can do it:

1. Open or create the `gradle.properties` file in your Gradle user home directory:
   - On Unix-like systems (Linux, macOS), this directory is typically `~/.gradle/`.
   - On Windows, this directory is typically `C:\Users\<YourUsername>\.gradle\`.
2. Add the following lines to the `gradle.properties` file:
    ```properties
    repository.user=your_user
    repository.token=your_token_value
    ```
   or use environment variables `REPOSITORY_USER` and `REPOSITORY_TOKEN`
