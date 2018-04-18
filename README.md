# Clojure(script) GraphQL Demo

A small application to demonstration usage of GraphQL in a full stack 
clojure + clojurescript application.

* [re-frame](https://github.com/Day8/re-frame) 
* [lacinia](https://github.com/walmartlabs/lacinia) - server-side GraphQL
* [venia](https://github.com/Vincit/venia) - client-side GraphQL

## Development Mode

### Compile css:

Compile css file once.

```
lein garden once
```

Automatically recompile css file on change.

```
lein garden auto
```

### Run application:

```
lein clean
lein figwheel dev
```

Open [http://localhost:8080](http://localhost:8080) and play around with the
application.

## Production Build

### Standalone jar

```
lein clean
lein uberjar
```

Web server runs on port 8080 by default, configurable via the `PORT` environment
variable.

### Docker

Various scripts in `bin` are provided to build and run docker images of the
application. These utilize the environment variables below, recommend using a
`.envrc` with [direnv](https://direnv.net/) to configure them

#### Environment Variables

* `REPO_URL` - repository URL to push to (Docker Hub, AWS Elastic Container
 Registry, Azure Container Registry, etc.)
* `IMAGE_NAME` - name of the image (ex: "graphql-demo")

#### Scripts

* `./bin/build` - builds the docker image and tags it "latest"
* `./bin/run` - run the docker image (PORT set to 8080)
* `./bin/push` - push the image to the repository URL
