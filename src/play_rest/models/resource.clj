(ns play-rest.models.resource
  (:use play-rest.models.db))


(defn get-resource[resource id]
  {:pre [(some? id) (some? resource)]}
  (db-find resource id))