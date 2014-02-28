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

(defn reject-invalid-number [next-fn input]
  (if (re-seq #"^\d+$" input)
    (next-fn (read-string input))
    (response {:number input :error "not a number"})))

(defn reject-big-number [next-fn number]
  (if (<= number 1e6)
    (next-fn number)
    (response {:number number :error "too big number (>1e6)"})))

;; Views

(defn home []
  (html5
   [:body
    [:h1 "Hello Yose"]
    [:a#contact-me-link {:href "#"}  "Contact me" ]
    [:a#ping-challenge-link {:href "/ping"} "Ping challenge"]]))

(defn ping []
  (response {:alive true}))

(defn prime-factors [n]
  (response {:number n :decomposition (factorize n)}))

;; Routes

(defroutes app-routes
  (GET "/" [] (home))
  (GET "/ping" [] (ping))
  (GET "/primeFactors" {{input "number"} :query-params}
       (reject-invalid-number (partial reject-big-number prime-factors) input)))

;; App

(def app
  (handler/site (-> app-routes
                    wrap-json-response
                    wrap-json-body)))

