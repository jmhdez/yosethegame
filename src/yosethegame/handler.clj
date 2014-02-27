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
  (let [factors (map (fn [_] 2)
                     (take-while #(not= % n) (iterate #(* 2 %) 1)))]
     (response {:number n
                :decomposition factors})))

(defroutes app-routes
  (GET "/" [] (home))
  (GET "/ping" [] (ping))
  (GET "/primeFactors" {{n "number"} :query-params} (prime-factors (read-string n))))

(def app
  (handler/site (-> app-routes
                    wrap-json-response
                    wrap-json-body)))

