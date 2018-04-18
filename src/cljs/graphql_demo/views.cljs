(ns graphql-demo.views
  (:require [reagent.core :as reagent]
            [re-frame.core :refer [dispatch subscribe]]))

(defn query-debugger
  []
  [:div.query-debugger
   [:h4 "GraphQL Query: "]
   [:code @(subscribe [:graphql-query])]])

(defn error-message
  [error]
  [:li
   [:b (:variable-name error)]
   " - "
   (:message error)])

(defn errors
  []
  (if-let [errors @(subscribe [:graphql-errors])]
    [:div.errors
     [:h4 "Errors:"]
     (into [:ul] (map error-message errors))]
    [:div.hidden]))

(defn search-box
  [query]
  (let [value (reagent/atom query)]
    (fn []
      [:div.search-box
       [:input {:type        "text"
                :value       @value
                :on-change   #(reset! value (-> % .-target .-value))
                :on-key-down #(if (= 13 (.-which %))
                                (dispatch [:search @value]))}]
       [:button.button {:on-click #(dispatch [:search @value])}
        "Search"]
       [:div.search-box__instructions
        "Enter a search term or leave blank to retrieve all posts"]])))

(defn search-result
  [result]
  [:li
   [:a
    {:href     ""
     :on-click (fn [evt]
                 (.preventDefault evt)
                 (dispatch [:get-post (:id result)]))}
    (:title result)]])

(defn search-results
  [results]
  [:div
   [:h4 (str "Got " (count results) " results")]
   (into [:ul.search-results]
         (map search-result results))])

(defn add-post
  []
  (fn []
    [:div
     [:h3 "Add Post"]
     [:div.form-row
      [:label "Title:"]
      [:input {:type      "text"
               :value     @(subscribe [:draft-post :title])
               :on-change #(dispatch [:update-draft-post :title
                                      (-> % .-target .-value)])}]]
     [:div.form-row
      [:label "Text:"]
      [:textarea {:value     @(subscribe [:draft-post :text])
                  :on-change #(dispatch [:update-draft-post :text
                                         (-> % .-target .-value)])}]]
     [:div.form-row
      [:label ""]
      [:input.button {:type     "submit"
                      :value    "Create"
                      :on-click #(dispatch [:create-post])}]]]))

(defn main
  []
  (let [results (subscribe [:search-results])]
    (fn []
      [:div
       [search-box @(subscribe [:query])]
       [query-debugger]
       [errors]
       [add-post]
       (if (seq @results)
         [search-results @results])])))