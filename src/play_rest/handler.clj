(ns play-rest.handler
  (:require [compojure.core :refer [defroutes routes]]
            [ring.middleware.resource :refer [wrap-resource]]
            [ring.middleware.file-info :refer [wrap-file-info]]
            [hiccup.middleware :refer [wrap-base-url]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [play-rest.routes.home :refer [home-routes]]
            [play-rest.routes.api :refer [api-routes]]
            [ring.middleware.json :as middleware]))

(defn init []
  (println "play-rest is starting"))

(defn destroy []
  (println "play-rest is shutting down"))

(defroutes app-routes
           (route/resources "/")
           (route/not-found "Not Found"))

(def app
  (-> (handler/site (routes api-routes home-routes app-routes))
      middleware/wrap-json-body
      middleware/wrap-json-response))

;; (def app
;;
;;(-> (routes api-routes home-routes app-routes)
;;    (handler/site)
;;    (wrap-base-url)) )
