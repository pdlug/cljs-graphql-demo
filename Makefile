.PHONY: all figwheel clean compile uberjar image

IMAGE_NAME ?= graphql-demo

all: figwheel

figwheel:
	clojure -Afigwheel

clean:
	rm -rf resources/public/js/compiled target

compile:
	clojure build.clj compile

uberjar: clean compile
	clojure -Apack

image: uberjar
	docker build -t "${IMAGE_NAME}:latest" .

image_minimal: uberjar
	docker build -f Dockerfile.minimal -t "${IMAGE_NAME}:latest" .

docker_run:
	docker run -e PORT=8080 -p 8080:8080 "${IMAGE_NAME}:latest"

docker_push:
	docker tag "${IMAGE_NAME}:latest" "${REPO_URL}:latest"
	docker push "${REPO_URL}:latest"
