(ns graphql-demo.css
  (:require [garden.core :as garden]
            [garden.selectors :as s]
            [garden.def :refer [defstyles]]))

(def base-font-size "18px")

(def body
  [:body {:font-family "Helvetica, Arial, sans-serif"
          :font-size   base-font-size
          :margin      "25px 50px"}])

(def h1
  [:h1 {:font-size "3em" :margin "0.5em 0"}])

(def h2
  [:h2 {:font-size "2em" :margin "0.5em 0"}])

(def h3
  [:h3 {:font-size "1.75em" :margin "0.5em 0"}])

(def h4
  [:h4 {:font-size "1.5em" :margin "0.5em 0"}])

(def hidden
  {:display "none"})

(def text-input
  [(s/input (s/attr= :type "text"))
   {:appearance    "none"
    :border        "1px solid #ccc"
    :border-radius "4px"
    :box-shadow    "none"
    :color         "#555"
    :font-size     base-font-size
    :line-height   "1.5"
    :padding       "6px 12px"
    :width         "500px"}
   [:&:focus
    {:outline "none"}]])

(def textarea
  [:textarea
   {:appearance    "none"
    :border        "1px solid #ccc"
    :border-radius "4px"
    :box-shadow    "none"
    :color         "#555"
    :display       "block"
    :font-size     base-font-size
    :line-height   "1.5"
    :padding       "6px 12px"
    :width         "500px"}
   [:&:focus
    {:outline "none"}]])

(def button
  [:.button
   {:color                       "#888"
    :font-size                   "20px"
    :font-family                 "helvetica"
    :text-decoration             "none"
    :border                      "2px solid #888"
    :border-radius               "10px"
    :transition-duration         ".2s"
    :-webkit-transition-duration ".2s"
    :-moz-transition-duration    ".2s"
    :background-color            "white"
    :padding                     "4px 30px"}
   [:&:hover
    {:color                       "white"
     :background-color            "#888"
     :transition-duration         ".2s"
     :-webkit-transition-duration ".11s"
     :-moz-transition-duration    ".2s"}]
   [:&:focus
    {:outline "none"}]])

(def query
  [:.query-debugger
   [:code {:background-color "#eaeaea"
           :display          "block"
           :margin           "0.25em"
           :padding          "1em"}]])

(def search-box
  [:.search-box
   {:margin-bottom "1.5em"}
   [:button {:display     "inline-block"
             :margin-left "15px"}]
   [:.search-box__instructions
    {:font-size "0.8em"}]])

(def search-result-list
  [:ul {:list-style-type "none"
        :margin          0
        :padding         0}
   [:li {:margin "1em 0"}]])

(def form-row
  [:.form-row
   {:margin-bottom "0.5em"}
   [:label {:float       "left"
            :font-size   base-font-size
            :line-height "1.5"
            :padding     "6px 12px"
            :text-align  "right"
            :width       "100px"}]])

(def errors
  [:.errors
   [:ul
    {:background-color "rgba(221, 44, 0, 0.37)"
     :margin           "0.25em"
     :padding          "1em"}]])

(def app
  [body
   h1
   h2
   h3
   h4
   hidden
   text-input
   textarea
   button
   query
   form-row
   errors
   search-box
   search-result-list])