(ns graphql-demo.service
  (:require
    [clojure.java.io :as io]
    [io.pedestal.http :as http]
    [io.pedestal.http.route :as route]
    [io.pedestal.http.body-params :as body-params]
    [io.pedestal.http.route.definition :refer [defroutes]]
    [io.pedestal.http.route.definition.table :as table]
    [io.pedestal.http.secure-headers :as sec-headers]
    [ring.util.response :as ring-resp :refer [content-type response]]
    [hiccup.page :as page]
    [com.walmartlabs.lacinia.pedestal :as lacinia]
    [graphql-demo.graphql :as graphql]
    [cheshire.core :as json]))

(defn head
  [title]
  [:head
   [:meta {:charset "utf-8"}]
   [:meta {:name    "viewport"
           :content "width=device-width, initial-scale=1"}]
   [:meta {:http-equiv "x-ua-compatible"
           :content    "ie=edge"}]
   [:link {:rel  "stylesheet"
           :href "/css/app.css"}]
   [:title title]])

(defn home-page
  []
  (page/html5
    {:lang "en"}
    (head "Clojure(script) GraphQL Demo")
    [:body
     [:div
      [:h1 "Clojure(script) GraphQL Demo"]
      [:div:#app]]
     [:script {:src "/js/compiled/app.js"}]
     [:script "graphql_demo.core.init();"]]))

(defn main
  [_]
  (content-type (response (home-page)) "text/html"))

(defn reset
  [_]
  (graphql/reset)
  (content-type
    (response (json/generate-string {:done true}))
    "application/json"))

(def common-interceptors
  [(body-params/body-params) http/html-body])

(defroutes base-routes
           [[["/"
              ^:interceptors common-interceptors {:get main}]
             ["/reset" {:post reset}]]])

(def routes
  (concat
    base-routes
    (table/table-routes
      (lacinia/graphql-routes graphql/application-schema {}))))

(def csp-header
  (sec-headers/content-security-policy-header
    {:default-src "'self'"
     :connect-src "'self' ws://localhost:3449/"
     :style-src   "'self' 'unsafe-inline'"
     :object-src  "'none'"
     :script-src  "'self' 'unsafe-inline' 'unsafe-eval' https: http:"}))

(def port
  (if-let [p (System/getenv "PORT")]
    (Integer/parseInt p)
    8080))

(def service
  {:env                     :prod
   ::http/routes            routes
   ::http/router            :linear-search
   ::http/resource-path     "/public"
   ::http/type              :jetty
   ::http/port              port
   ::http/container-options {:h2c? true
                             :h2?  false
                             :ssl? false}
   ::http/secure-headers    {:content-security-policy-settings csp-header}})
