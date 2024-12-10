# Hlæja Device Register

Classes crafted, identities bestowed, Each device recorded, their functions unfold. From sensors to systems, connections take shape, A registry formed, no detail escapes. Signals exchanged, precision is key, Each device declared, its purpose set free. The Device Register, steadfast and true, A harmony of devices, perfectly aligned.

## Properties for deployment

| name                   | required | info                    |
|------------------------|:--------:|-------------------------|
| spring.profiles.active | &check;  | Spring Boot environment |
| spring.r2dbc.url       | &check;  | Postgres host url       |
| spring.r2dbc.username  | &check;  | Postgres username       |
| spring.r2dbc.password  | &cross;  | Postgres password       |
| jwt.private-key        | &check;  | JWT private cert        |

*Required: &check; can be stored as text, and &cross; need to be stored as secret.*

## Releasing Service

Run `release.sh` script from `master` branch.

## Development Information

### Private RSA Key

This service uses RAS keys to create identities for devices. The private key is used here to generate identities, while the public key is used by **[Hlæja Device API](https://github.com/swordsteel/hlaeja-device-api)** to identify a device and accept data.

*For instructions on how to set these up, please refer to our [generate RSA key](https://github.com/swordsteel/hlaeja-development/blob/master/doc/rsa_key.md) documentation.*

### Global Setting

The following global settings are used in Hlaeja Device Registry. You can configure these settings using either Gradle properties or alternatively environment variables. 

*For instructions on how to set these up, please refer to our [set global settings](https://github.com/swordsteel/hlaeja-development/blob/master/doc/global_settings.md) documentation.*

#### Gradle Properties

```properties
repository.user=your_user
repository.token=your_token_value
```

#### Environment Variables

```properties
REPOSITORY_USER=your_user
REPOSITORY_TOKEN=your_token_value
```
