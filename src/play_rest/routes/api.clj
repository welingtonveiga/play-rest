(ns play-rest.routes.api
  (:require [compojure.core :refer :all]
            [play-rest.models.resource :refer :all]))

(defroutes api-routes
   (GET "/api/:res/:id" [res id]
   (let [res (get-resource res id)]
     (if res
       {:body res }
       nil))))
