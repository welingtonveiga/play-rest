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

  (testing "GET /api/resource/ID to a existing resource SHOULD return a 200 status code and a single resource data"
    (with-redefs [get-resource (fn [resource, id] {:id "X" :field "value"})]
      (let [response (app (request :get "/api/resource/1"))]
        (is (= (:status response) 200)
        (is (= (:body response) "{\"id\":\"X\",\"field\":\"value\"}"))))))

  (testing "GET /api/resource  SHOULD return a 200 status code and all resources"
    (with-redefs [get-all-resources (fn [resource] '({:f "a"} {:f "b"} {:f "c"}) )]
      (let [response (app (request :get "/api/resource"))]
        (is (= (:status response) 200)
        (is (= (:body response) "[{\"f\":\"a\"},{\"f\":\"b\"},{\"f\":\"c\"}]")))))))

(deftest test-query-resources
  (testing "POST /api/resource?f=a SHOULD return a 200 (OK) and a list with resources where f=a"
    (with-redefs [query-resources (fn [resource, data]'({:_id 1 :f "a"} {:_id 2 :f "a"}))]
      (let [response (app (request :get "/api/resource?f=a"))]
        (is (= (:status response) 200))
        (is (= (:body response) "[{\"_id\":1,\"f\":\"a\"},{\"_id\":2,\"f\":\"a\"}]"))))))

(deftest test-post-resource

  (testing "POST /api/resource SHOULD return a 201 (Created) and a header with link to new ID"
    (let [ id (uuid)]
      (with-redefs [persist-resource (fn [resource, data](assoc data :_id id)) uuid (fn [] id)]
        (let [response (app (request :post "/api/resource" "{\"field\":\"value\"}"))]
          (is (= (:status response) 201))
          (is (= (get (:headers response) "Location") (str "/api/resource/" id))))))))

(deftest test-delete-resource

  (testing "DELETE /api/resource/ID non existent SHOULD return a 404 status code"
      (with-redefs [delete-resource (fn [resource, id] 0)]
        (let [response (app (request :delete "/api/resource/id"))]
          (is (= (:status response) 404)))))

  (testing "DELETE /api/resource/ID exitent SHOULD return a 200 status code"
      (with-redefs [delete-resource (fn [resource, id] 1)]
        (let [response (app (request :delete "/api/resource/id"))]
          (is (= (:status response) 200))))))
