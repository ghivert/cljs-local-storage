(ns local-storage.core-test
  (:require [cljs.test :refer [deftest is async are] :refer-macros [use-fixtures]]
            [local-storage.core :as ls]))

(def raw-test "test")
(def raw-json {:test "test"})

(use-fixtures :each
  {:before (fn []
             (.clear js/localStorage)
             (ls/set-item! :key raw-test)
             (ls/set-item! :json raw-json))})

(deftest set-item-test
  (is (= raw-test (ls/set-item! :key raw-test)))
  (let [data
        (->> (.getItem js/localStorage "key")
             (.parse js/JSON))
        json
        (->> (.getItem js/localStorage "json")
             (.parse js/JSON))
        final-json (js->clj json :keywordize-keys true)]
    (are [stored raw] (= stored raw)
         data "test"
         final-json {:test "test"})))

(deftest get-item-test
  (let [data (ls/get-item! :key)
        json (ls/get-item! :json)]
    (are [stored raw] (= stored raw)
         data raw-test
         json raw-json)
    (is (nil? (ls/get-item! :non-existent)))))

(deftest get-items-test
  (let [{:keys [key json]} (ls/get-items! [:key :json])]
    (are [stored raw] (= stored raw)
         key raw-test
         json raw-json))
  (is (= {:non-existent nil :too nil}) (ls/get-items! [:non-existent :too])))

(deftest set-items-test
  (let [to-store {:other-key raw-test
                  :other-json raw-json}]
    (is (= to-store (ls/set-items! to-store)))
    (let [{:keys [other-key other-json]} (ls/get-items! [:other-key :other-json])]
      (are [stored raw] (= stored raw)
           other-key raw-test
           other-json raw-json))))

(deftest remove-item-test
  (are [stored raw] (= stored raw)
       (ls/remove-item! :key) raw-test
       (ls/remove-item! :json) raw-json)
  (are [stored] (nil? stored)
       (ls/get-item! :key)
       (ls/get-item! :json)))

(deftest remove-items-test
  (is (= {:key raw-test :json raw-json} (ls/remove-items! [:key :json])))
  (is (= {:key nil :json nil}) (ls/get-items! [:key :json])))

(deftest get-all-test
  (is (= {:key raw-test :json raw-json} (ls/get-all!))))

(deftest clear-test
  (is (= {:key raw-test :json raw-json} (ls/clear!)))
  (is (= {} (ls/get-all!))))
