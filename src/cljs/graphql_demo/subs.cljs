(ns graphql-demo.subs
  (:require-macros [reagent.ratom :refer [reaction]])
  (:require [re-frame.core :refer [reg-sub]]))

(reg-sub
  :query
  (fn [db]
    (:query db)))

(reg-sub
  :graphql-query
  (fn [db]
    (:graphql-query db)))

(reg-sub
  :graphql-errors
  (fn [db]
    (:graphql-errors db)))

(reg-sub
  :search-results
  (fn [db]
    (:results db)))

(reg-sub
  :draft-post
  (fn [db [_ field]]
    (get-in db [:draft-post field])))