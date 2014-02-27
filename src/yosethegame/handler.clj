(ns yosethegame.handler
  (:use compojure.core)
  (:use ring.middleware.json)
  (:use ring.util.response)
  (:use hiccup.page)
  (:use hiccup.element)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defn home []
  (html5
   [:body
    [:h1 "Hello Yose"]
    [:a#contact-me-link {:href "#"}  "Contact me" ]
    [:a#ping-challenge-link {:href "/ping"} "Ping challenge"]]))

(defn ping []
  (response {:alive true}))

(defn prime-factors [n]
  (let [power-of-two (.indexOf (iterate #(* 2 %) 1) n)]
     (response {:number n
                :decomposition (repeat power-of-two 2)})))

(defn ensure-number [input fn]
  (if (re-seq #"\d" input)
    (fn (read-string input))
    (response {:number input
               :error "not a number"})))

(defroutes app-routes
  (GET "/" [] (home))
  (GET "/ping" [] (ping))
  (GET "/primeFactors" {{n "number"} :query-params} (ensure-number n  prime-factors)))

(def app
  (handler/site (-> app-routes
                    wrap-json-response
                    wrap-json-body)))

