(ns graphql-demo.events
  (:require [re-frame.core :refer [reg-event-db reg-event-fx]]
            [day8.re-frame.http-fx]
            [ajax.core :as ajax]
            [venia.core :as v]
            [graphql-demo.db :as db]))

(reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))

(reg-event-db
  :update-draft-post
  (fn update-draft-post [db [_ field value]]
    (assoc-in db [:draft-post field] value)))

(reg-event-db
  :post-created
  (fn post-created [db [_ result]]
    (assoc db :draft-post {})))

(reg-event-db
  :good-http-result
  (fn good-http-result [db [_ db-key result-key result]]
    (assoc db db-key (get-in result [:data result-key]))))

(reg-event-db
  :bad-http-result
  (fn good-http-result [db [_ result]]
    (if-let [errors (get-in result [:response :errors])]
      (assoc db :graphql-errors errors)
      db)))

(defn graphql-request
  [& {:keys [db query variables on-success on-failure]}]
  {:db (-> db
           (assoc :graphql-query query)
           (assoc :graphql-errors nil))
   :http-xhrio
       {:method          :post
        :timeout         8000
        :uri             "/graphql"
        :format          (ajax/json-request-format)
        :response-format (ajax/json-response-format {:keywords? true})
        :params          {:query     query
                          :variables variables}
        :on-success      on-success
        :on-failure      (or on-failure [:bad-http-result])}})

(reg-event-fx
  :search
  (fn search [{:keys [db]} [_ q]]
    (let [gq (v/graphql-query {:venia/queries [[:search {:q q}
                                                [:id :title]]]})]
      (graphql-request
        :db (assoc db :query q)
        :query gq
        :on-success [:good-http-result :results :search]))))

(reg-event-fx
  :get-post
  (fn get-post [{:keys [db]} [_ id]]
    (let [gq (v/graphql-query {:venia/queries [[:post {:id id}
                                                [:id :title :text]]]})]
      (graphql-request
        :db db
        :query gq
        :on-success [:good-http-result :post :post]))))

(reg-event-fx
  :create-post
  (fn create-post [{:keys [db]} _]
    (let [gq (v/graphql-query
               {:venia/operation {:operation/type :mutation
                                  :operation/name "CreatePost"}
                :venia/variables [{:variable/name "title"
                                   :variable/type :String!}
                                  {:variable/name "text"
                                   :variable/type :String}]
                :venia/queries   [[:addPost {:title :$title
                                             :text  :$text}
                                   [:id :title :text]]]})]
      (graphql-request
        :db db
        :query gq
        :variables (select-keys (:draft-post db) [:title :text])
        :on-success [:post-created]))))
