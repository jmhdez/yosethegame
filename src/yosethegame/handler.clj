(ns yosethegame.handler
  (:use compojure.core)
  (:use ring.middleware.json)
  (:use ring.util.response)
  (:use hiccup.page)
  (:use hiccup.element)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

;; Helpers

(defn factorize [number]
  (loop [n number
         f 2
         factors []]
    (cond
     (= number (apply * factors)) (into '() factors)
     (zero? (rem n f)) (recur (/ n f) f (cons f factors))
     :else (recur n (inc f) factors))))

;; Views

(defn home []
  (html5
   [:body
    [:h1 "Hello Yose"]
    [:a#contact-me-link {:href "#"}  "Contact me" ]
    [:a#ping-challenge-link {:href "/ping"} "Ping challenge"]]))

(defn ping []
  (response {:alive true}))

(defn prime-factors [input]
  (if (re-seq #"^\d+$" input)
    (let [n (read-string input)]
      (response {:number n
                 :decomposition (factorize n)}))
    (response {:number input
               :error "not a number"})))

;; Routes

(defroutes app-routes
  (GET "/" [] (home))
  (GET "/ping" [] (ping))
  (GET "/primeFactors" {{n "number"} :query-params} (prime-factors n)))

;; App

(def app
  (handler/site (-> app-routes
                    wrap-json-response
                    wrap-json-body)))

