# Hlæja Device Register

Classes crafted, identities bestowed, Each device recorded, their functions unfold. From sensors to systems, connections take shape, A registry formed, no detail escapes. Signals exchanged, precision is key, Each device declared, its purpose set free. The Device Register, steadfast and true, A harmony of devices, perfectly aligned.

## Properties for deployment

| name                   | required | info                    |
|------------------------|----------|-------------------------|
| spring.profiles.active | *        | Spring Boot environment |
| spring.r2dbc.url       | *        | Postgres host url       |
| spring.r2dbc.username  | *        | Postgres username       |
| spring.r2dbc.password  | **       | Postgres password       |
| jwt.private-key        |          | JWT private cert        |

Required: * can be stored as text, and ** need to be stored as secret.  

## Releasing Service

Run `release.sh` script from `master` branch.

## Development Information

### Generate Private and Public RSA Key

OpenSSL Project is dedicated to providing a simple installation of OpenSSL for Microsoft Windows. [Download](https://slproweb.com/products/Win32OpenSSL.html)

Generate an RSA private key, of size 2048, and output it to a file named `private_key.pem` in to `./keys`

```shell
openssl genrsa -out private_key.pem 2048
```

Extract the public key from `private_key.pem` from `./keys`, and output it to a file named `public_key.pem` in to `./keys`

```shell
openssl rsa -in private_key.pem -pubout -out public_key.pem
```

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
