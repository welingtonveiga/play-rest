(ns play-rest.routes.home
  (:require [compojure.core :refer :all]
            [play-rest.views.layout :as layout]))

(defn home []
  (layout/common [:h1 "Hello World!"]))

(defroutes home-routes
  (GET "/" [] (home)))
