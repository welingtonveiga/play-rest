(ns play-rest.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [ring.middleware.json :as middleware]
            [ring.middleware.params :as wrap-params]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [play-rest.routes.home :refer [home-routes]]
            [play-rest.routes.api :refer [api-routes]]
            [play-rest.models.db :refer [connect!]]))

(defn init []
  (connect!)
  (println "play-rest started"))

(defn destroy []
  (println "play-rest is shutting down"))

(defroutes app-routes
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
  (-> (handler/site (routes api-routes home-routes app-routes))
      ;; wrap-params
      middleware/wrap-json-body
      middleware/wrap-json-response))

