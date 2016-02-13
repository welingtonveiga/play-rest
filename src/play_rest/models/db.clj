(ns play-rest.models.db
  (:require
    [monger.core :as m]
    [monger.collection :as collection]  )
  (:import org.bson.types.ObjectId)
  (:import java.util.UUID))

(def db-conn (atom nil))

(defn connect! []
  (swap! db-conn (fn [c] ( m/connect-via-uri "mongodb://localhost:27017/playrest"))))


(defn uuid []
  (str (UUID/randomUUID)))

(defn db-find[entity id]
  (let [found (collection/find-one-as-map (:db @db-conn) entity { :_id (ObjectId. id)})]
    (println entity)
    (if (some? found) (update found :_id str) nil)))

(defn db-persist[entity data]
  (let [persisted  (collection/insert-and-return (:db @db-conn) entity data)]
   (update persisted :_id str)))