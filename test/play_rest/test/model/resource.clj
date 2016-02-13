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
