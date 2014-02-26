(ns yosethegame.handler
  (:use compojure.core)
  (:use hiccup.core)
  (:use ring.middleware.json)
  (:use ring.util.response)
  (:require [compojure.handler :as handler]
            [compojure.route :as route]))

(defroutes app-routes
  (GET "/" [] "Hello Yose")
  (GET "/ping" [] (response {:alive true}))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (handler/site (-> app-routes
                    wrap-json-response
                    wrap-json-body)))
