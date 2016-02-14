(ns play-rest.models.db
  (:require
    [monger.core :as m]
    [monger.collection :as collection])
  (:import java.util.UUID))

(def db-conn (atom nil))

(defn connect! []
  (swap! db-conn (fn [] ( m/connect-via-uri "mongodb://localhost:27017/playrest"))))


(defn uuid []
  (str (UUID/randomUUID)))

(defn db-search[entity criteria]
  (let [found (collection/find-maps(:db @db-conn) entity criteria)]
    (if (some? found) (map #(update % :_id str) found) nil)))

(defn db-list[entity]
  (let [found (collection/find-maps(:db @db-conn) entity)]
    (if (some? found) (map #(update % :_id str) found) nil)))

(defn db-find[entity id]
  (let [found (collection/find-one-as-map (:db @db-conn) entity { :_id (ObjectId. id)})]
    (if (some? found) (update found :_id str) nil)))

(defn db-persist[entity data]
  (let [persisted  (collection/insert-and-return (:db @db-conn) entity data)]
   (update persisted :_id str)))

(defn db-remove[entity id]
  (.getN (collection/remove-by-id (:db @db-conn) entity (ObjectId. id))))