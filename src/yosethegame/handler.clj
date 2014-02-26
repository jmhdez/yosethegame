(ns yosethegame.handler
  (:use compojure.core)
  (:use hiccup.core)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] "Hello Yose")
  (GET "/ping" [] "{ \"alive\": true }")
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site app-routes))
