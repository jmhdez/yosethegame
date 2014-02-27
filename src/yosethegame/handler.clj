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
    [:a#contact-me-link {:href "#"}  "Contact me" ]]))

(defroutes app-routes
  (GET "/" [] (home))
  (GET "/ping" [] (response {:alive true})))

(def app
  (handler/site (-> app-routes
                    wrap-json-response
                    wrap-json-body)))
