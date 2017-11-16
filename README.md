# Consul-WhoAmI

This is a completely useless application to demonstrate the usage of [Consul](https://consul.io) and [Fabio](https://github.com/fabiolb/fabio).

## Image

A prebuilt image of this application is available at [Docker Hub](https://hub.docker.com/r/baez90/consul-whoami/).

## Configuration

There's not much to confgure.
To get the app up and running you have to configure the following environment variables:

* CONSUL_URL
* FABIO_TAGS

You may change the port the application is running on by passing the following environment variable to the container:

* APP_PORT

### CONSUL_URL

The application expects the variable in the format of `<hostname or IP>:<port>`.

### FABIO_TAGS

The application takes the content of this environment variable, splits it at the character `|` and passes them as tags to Consul.

Have a look at the Fabio docs to get an idea which tags are required for you.