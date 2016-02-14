(ns play-rest.test.model.resource
  (:use clojure.test
        ring.mock.request
        play-rest.models.db
        play-rest.models.resource))

(deftest test-get-resource

  (testing "id SHOULD not be null"
    (is (thrown? AssertionError (get-resource "resource" nil))))

  (testing "resource SHOULD not be null"
    (is (thrown? AssertionError (get-resource nil 1))))

  (testing "get inexistent resource SHOULD return 'nil'"
    (with-redefs [db-find (fn [entity id] (if (and (= entity "resource") (= id 999)) nil :failure))]
      (let [result (get-resource "resource" 999)]
        (is (nil? result)))))

  (testing "get existent resource SHOULD return it"
    (with-redefs [db-find (fn [entity id] (if (and (= entity "resource") (= id 999))  {:id "ID" :field "value"}:failure))]
      (let [result (get-resource "resource" 999)]
        (is (= result {:id "ID" :field "value"}))))))

(deftest test-get-all-resources

  (testing "resource SHOULD not be null"
    (is (thrown? AssertionError (get-all-resources nil))))

  (testing "persist resource SHOULD return it with generated _id"
    (with-redefs [db-list (fn [resource] '({:f "a"} {:f "b"} {:f "c"}))]
      (let [result (get-all-resources "resource")]
        (is (= result '({:f "a"} {:f "b"} {:f "c"})))))))

(deftest test-query-resources

  (testing "resource SHOULD not be null"
    (is (thrown? AssertionError (query-resources nil {}))))

  (testing "criteria SHOULD not be null"
    (is (thrown? AssertionError (query-resources "resource" nil))))

  (testing "persist resource SHOULD return it with generated _id"
  (with-redefs [db-search (fn [resource criteria] '({:f "a"} {:f "b"}))]
    (let [result (query-resources "resource" {:c1 "val"})]
      (is (= result '({:f "a"} {:f "b"})))))))

(deftest test-persist-resource

  (testing "data SHOULD not be null"
    (is (thrown? AssertionError (persist-resource "resource" nil))))

  (testing "resource SHOULD not be null"
    (is (thrown? AssertionError (persist-resource nil {:field "value"}))))

  (testing "persist resource SHOULD return it with generated _id"
    (with-redefs [db-persist (fn [resource, data](assoc data :_id "1"))]
      (let [result (persist-resource "resource" {:field "value"})]
        (is (= result {:_id "1" :field "value"}))))))

(deftest test-delete-resource

  (testing "id SHOULD not be null"
    (is (thrown? AssertionError (delete-resource "resource" nil))))

  (testing "resource SHOULD not be null"
    (is (thrown? AssertionError (delete-resource  nil "1"))))

  (testing "delete resource SHOULD be called"
    (with-redefs [db-remove (fn [resource, id] 1)]
       (let [result (delete-resource "resource" "56bfb897aefa911c66310c02")]
         (is (= result 1))))))
