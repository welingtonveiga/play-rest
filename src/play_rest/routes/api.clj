(ns play-rest.routes.api
  (:require [compojure.core :refer :all]
            [play-rest.models.resource :refer :all]
            [clojure.data.json :as json]))

(defn read-json-body [body]
  (if (map? body) body (json/read-str ((fnil slurp "{}") body))))

(defn build-resource-location [resource-name resource-data]
  (str "/api/" resource-name "/" (:_id resource-data)))

(defroutes api-routes

  (GET "/api/:res/:id" [res id]
    (let [res (get-resource res id)]
      (if res {:body res } nil)))

  (GET "/api/:res" {params :params}
    (let [res (:res params)
          query (dissoc params :res)
          result (if (empty? query) (get-all-resources res) (query-resources res query))]
      (if res {:body result } nil)))

  (POST "/api/:res" {body :body params :params}
    (let [ data (read-json-body body)
          resource (:res params)
          persisted-data (persist-resource resource data)]
      {:headers
        {"Location" (build-resource-location resource persisted-data) }
       :status 201
       :body (json/write-str persisted-data)}))

  (DELETE "/api/:res/:id" [res id]
    (let [result (delete-resource res id) ]
     (if (zero? result) nil {:body "" :status 200}))))


