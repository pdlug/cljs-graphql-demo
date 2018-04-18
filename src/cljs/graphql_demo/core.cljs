(ns graphql-demo.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [graphql-demo.events]
            [graphql-demo.subs]
            [graphql-demo.views :as views]
            [graphql-demo.config :as config]))

(defn dev-setup []
  (when config/debug?
    (enable-console-print!)))

(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main]
                  (.getElementById js/document "app")))

(defn ^:export init []
  (re-frame/dispatch-sync [:initialize-db])
  (dev-setup)
  (mount-root))
