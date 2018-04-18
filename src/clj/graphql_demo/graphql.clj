(ns graphql-demo.graphql
  (:require [clojure.edn :as edn]
            [clojure.java.io :as io]
            [com.walmartlabs.lacinia.util :as graphql-util]
            [com.walmartlabs.lacinia.schema :as schema])
  (:import (java.util UUID)))

(defn new-post-id
  []
  (str (UUID/randomUUID)))

(def sample-posts
  [{:id    (new-post-id)
    :title "Foo 1"}
   {:id    (new-post-id)
    :title "Foo 2"}
   {:id    (new-post-id)
    :title "Bar 1"}
   {:id    (new-post-id)
    :title "Foo bar baz 1"}])

(defonce posts (atom sample-posts))

(defn get-post
  [context arguments value]
  (let [{:keys [id]} arguments]
    (if-let [post (first (filter #(= (:id %) id) @posts))]
      post)))

(defn search
  [context arguments value]
  (let [{:keys [q]} arguments]
    (filter
      #(re-matches (re-pattern (str "(?i).*" q ".*"))
                   (:title %))
      @posts)))

(defn add-post
  [context arguments value]
  (let [{:keys [title text]} arguments
        new-post {:id    (new-post-id)
                  :title title
                  :text  text}]
    (swap! posts conj new-post)
    new-post))

(defn reset
  [context arguments value]
  (reset! posts sample-posts)
  true)

(def application-schema
  (-> "schema.edn"
      io/resource
      slurp
      edn/read-string
      (graphql-util/attach-resolvers {:get-post get-post
                                      :search   search
                                      :reset    reset
                                      :add-post add-post})
      schema/compile))
