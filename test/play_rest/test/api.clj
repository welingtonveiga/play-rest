(ns play-rest.test.api
  (:use clojure.test
        ring.mock.request
        play-rest.handler
        play-rest.models.resource))



(deftest test-get-resource
  (testing "GET /api/resource/ID not created SHOULD return a 404 status code and 'Not Found' body"
    (with-redefs [get-resource (fn [resource, id] nil)]
      (let [response (app (request :get "/api/resource/999"))]
        (is (= (:status response) 404))
        (is (= (:body response) "Not Found")))))

  (testing "GET /api/resource/ID to a existing resource SHOULD return a 200 status code and a single entity data"
    (with-redefs [get-resource (fn [resource, id] {:id "X" :field "value"})]
      (let [response (app (request :get "/api/resource/1"))]
        (println response)
        (is (= (:status response) 200)
            (is (= (:body response) "{\"id\":\"X\",\"field\":\"value\"}")))))))


