(ns play-rest.routes.api
  (:require [compojure.core :refer :all]
            [play-rest.models.resource :refer :all]
            [clojure.data.json :as json])
  (:import java.util.Map))

(defn read-json-body [body]
  (if (map? body) body (json/read-str ((fnil slurp "{}") body))))

(defn build-resource-location [resource-name resource-data]
  (str "/api/" resource-name "/" (:_id resource-data)))

(defroutes api-routes
  (GET "/api/:res/:id" [res id]
    (let [res (get-resource res id)]
      (if res {:body res } nil)))

  (POST "/api/:res" {body :body headers :headers params :params}
    (let [ data (read-json-body body)
          resource (:res params)
          persisted-data (persist-resource resource data)]
      {:headers
        {"Location" (build-resource-location resource persisted-data) }
       :status 201
       :body (json/write-str persisted-data)}
       )))


