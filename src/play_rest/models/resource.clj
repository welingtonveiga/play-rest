(ns play-rest.models.resource
  (:use play-rest.models.db))

(defn get-all-resources [resource]
  {:pre [(some? resource)]}
  (db-list resource))

(defn query-resources [resource criteria]
  {:pre [(some? resource) (some? criteria)]}
  (db-search resource criteria))

(defn get-resource[resource id]
  {:pre [(some? id) (some? resource)]}
  (db-find resource id))

(defn persist-resource[resource data]
  {:pre [(some? resource) (some? data)]}
  (db-persist resource data))

(defn delete-resource[resource id]
  {:pre [(some? resource) (some? id)]}
  (db-remove resource id))
