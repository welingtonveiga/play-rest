(ns play-rest.test.handler
  (:use clojure.test
        ring.mock.request
        play-rest.handler
        play-rest.models.resource))

(deftest test-app
  (testing "main route"
    (let [response (app (request :get "/"))]
      (is (= (:status response) 200))
      (is (.contains (:body response) "Hello World"))))

  (testing "not-found route"
    (let [response (app (request :get "/invalid"))]
      (is (= (:status response) 404)))))

