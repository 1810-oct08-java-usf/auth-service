# auth-service

The purpose of this service is serve as the primary authentication server for the RPM microservice ecosystem. The authentication scheme being leveraged currently is the OAuth2 Abstract Protocol Flow leveraging refresh tokens. All requests related to authentication are handled by `/auth/**` endpoints. The below diagram details the authentication flow process:

ABSTRACT-PROTOCOL-FLOW-DIAGRAM goes here

Additionally, this service maintains the user pool for registered users of the RPM system. All requests related related to user creation, fetching, and mutation are handled by `/users/**` endpoints. Method-level security is enabled on all of these endpoints to ensure that only authenticated and authorized users have the ability to perform operations on the user pool.
