# Clojure(script) GraphQL Demo

A small application to demonstration usage of GraphQL in a full stack 
clojure + clojurescript application. Also demonstrates using plain clojure CLI
tools with `deps.edn` along with a `Makefile` rather than a heavier tool like
[Leiningen](https://leiningen.org/) or [boot](http://boot-clj.com/).

* [re-frame](https://github.com/Day8/re-frame)
* [lacinia](https://github.com/walmartlabs/lacinia) - server-side GraphQL
* [venia](https://github.com/Vincit/venia) - client-side GraphQL


## Environment Variables

Configuration is done via environment variables. I recommend using a
`.envrc` with [direnv](https://direnv.net/) to configure them as needed. For
basic development the defaults will be fine and no configuration is needed.

* `PORT` - port to run the server on (default: 8080)
* `REPO_URL` - repository URL to push to (Docker Hub, AWS Elastic Container
 Registry, Azure Container Registry, etc.)
* `IMAGE_NAME` - name of the image (ex: "graphql-demo")

## Development

### Run application:

The default `make` task is `figwheel` which will start the backend server in
development mode with Figwheel alongside it for frontend development.

```
$ make
```

Open [http://localhost:8080](http://localhost:8080) and play around with the
application.

## Production / Deployment

### Standalone jar

```bash
$ make uberjar
```

### Docker

Two different `Dockerfile`s are provided. The default `Dockerfile` uses the
`openjdk:8-jre-alpine` base image which is fairly lightweight and well tested.
The `Dockerfile.minimal` variant is a multi-stage docker build that uses `jlink`
on alpine linux and OpenJDK 11 EA to create a minimal JRE that only uses the
essential modules that the app uses (java.base, java.instrument,
java.management) as identified by `jdeps`. This results in a smaller image
(117MB vs. 73MB in my tests). See [Multi-Stage Docker build with jlink](https://mjg123.github.io/2018/05/26/Multi-Stage-Docker-Build-with-jlink.html)
for more details.

The images can be built with either of these two make tasks:

Default (OpenJDK 8 JRE Alpine):

```bash
$ make image
```

Minimal (custom built OpenJDK 11 EA via `jlink`):

```bash
$ make image_minimal
```

To run the image just execute (assuming your `IMAGE_NAME` is set to "graphql-demo"):

```bash
$ docker run graphql-demo:latest
```

Some additional `make` tasks are provided for common docker related tasks:

Start a container from the image (running on port 8080):

```bash
$ make docker_run
```

Tag and push the image to the repository specified by `REPO_URL`:

```bash
$ make docker_push
```
