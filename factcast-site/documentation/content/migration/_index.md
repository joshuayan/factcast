+++
draft = false
title = "Migration Guide"
description = ""

creatordisplayname = "Uwe Schaefer"
creatoremail = "uwe@codesmell.de"

[menu.main]
parent = "migration"
identifier = "migration"
weight = 1000
+++

## Upgrading to 0.2.0 (quite a lot)

#### basic-auth setup has changed

If you used a 'factcast-security.json' before, you will be please to learn that factcast was extended to support role/namespace based autorisation. Also the filename changed to 'factcast-access.json'.

see [basicauth usage](/setup/examples/grpc-config-basicauth)

#### basic-auth setup is enforced

By default, when executing without security enabled, you need to supply a property
'factcast.security.enabled=false' via commandline or propertyfile to get away with just a warning. If you don't, factcast will exit with errorcode 1.

#### fetching facts by ID has been removed

Even though it is a breaking change, this feature was removed from the API. First of all it interfered with transformation (so it would have needed to be extended), second it added unnecessary complexity to the subscription handling for no good reason.
The idea of that feature was to enable "local" caching in a HTTP Scenario, that we currently do not support any longer.
Also, the feature has never proven to deliver in terms of local caching, as nobody ever used it (or at least did not provide any feedback).

This also means, that subscribe for Ids is gone.

If you have severe problems with that change, please provide feedback, so that we can learn about the usecase and maybe provide an alternative.

## Upgrading to 0.1.0

#### unique_identifier

If you used the uniqe_identifier feature before, it was removed. It was only a rouge hack that was used to coordinate two instance in case of publishing. By now, coordination can be done via optimistic locking, so that the need for *unique_identifier* is no longer there.

#### optimistic locking

There is a [section on optimitic locking](/usage/java/optimistic_locking/) as a new api feature.

#### Postgres module uuid-ossp 

The Postgres module *uuid-ossp* is necessary for the new optimistic locking api feature. In order to install this
extension, the user performing the Liquibase operations requires Postgres superuser permissions. 

#### GRPC Protocol Version

The GRPC Protocol Version shifted from 1.0.0 to 1.1.0. That means, in order to talk to a factcast server with version 0.1.0, you can use and client from 0.0.30 on, but in order to use a 0.1.0 client, you'd need to talk to a factcast server with at least the same protocol version than your client.
So the idea is: first update your servers, then update the clients. 

#### GRPC Adresses, Hosts, Ports

We updated to yidongnan/grpc-spring-boot-starter. In order to direct your client to a particular target address of a Factcast server, you might have specified:

```
grpc.client.factstore.port=9443
grpc.client.factstore.host=localhost
```

this was replaced by

```
grpc.client.factstore.address=static://localhost:9443
```

or

```
grpc.client.factstore.address=dns:///some.host:9443
```

see https://github.com/yidongnan/grpc-spring-boot-starter for details


## Upgrading to 0.0.30

#### Spring Boot 2

If you use Spring boot, please note, that all projects now depend on Spring Boot 2 artifacts. 
Support for Sring Boot 1.x was removed. 

#### Plaintext vs TLS

There was a dependency upgrade of [grpc-spring-boot-starter](https://github.com/yidongnan/grpc-spring-boot-starter) in order to support TLS. Note that the default client configuration is now switched to TLS. That means, if you want to continue communicating in an unencrypted fashion, you need to set an application property of **'grpc.client.factstore.negotiation-type=PLAINTEXT'**. 

#### Testcontainers / Building and Testing

In order to run integration tests, that need a Postgres to run, FactCast now uses [Testcontainers](https://www.testcontainers.org/usage/database_containers.html) in order to download and run an ephemeral Postgres.
For this to work, the machine that runs test must have docker installed and the current user needs to be able to run and stop docker containers.

You can still override this behavior by supplying an Environment-Variable **'pg_url'** to use a particular postgres instead. This might be important for build agents that themselves run within docker and do not provide Docker-in-Docker. 


## Upgrading to 0.0.14

* Incompatible change in GRPC API

The GRPC API has changed to enable non-breaking changes later. (Version endpoint added)
The result is, that you have to use > 0.0.14 on Client and Server consistently.

## Noteworthy 0.0.12

* Note that the jersey impl of the REST interface has its own <a href="https://github.com/Mercateo/factcast-rest-jersey">place on github now.</a> and got new coordinates: **org.factcast:factcast-server-rest-jersey:0.0.12.** If you use the REST Server, you'll need to change your dependencies accordingly

* There is a BOM within factcast at org.factcast:factcast-bom:0.0.12 you can use to conveniently pin versions - remember that factcast-server-rest-jersey might not be available for every milestone and is not part of the BOM








