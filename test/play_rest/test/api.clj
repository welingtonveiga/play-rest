(ns play-rest.test.api
  (:use clojure.test
        ring.mock.request
        play-rest.handler
        play-rest.models.db
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
        (is (= (:status response) 200)
            (is (= (:body response) "{\"id\":\"X\",\"field\":\"value\"}")))))))

(deftest test-post-resource
  (testing "POST /api/resource SHOULD return a 201 (Created) and a header with link to new ID"
    (let [ id (uuid)]
      (with-redefs [persist-resource (fn [resource, data](assoc data :_id id)) uuid (fn [] id)]
        (let [response (app (request :post "/api/resource" "{\"field\":\"value\"}"))]
          (is (= (:status response) 201))
          (is (= (get (:headers response) "Location") (str "/api/resource/" id))))))))
