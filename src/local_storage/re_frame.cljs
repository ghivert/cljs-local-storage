(ns local-storage.re-frame
  (:require [local-storage.core :as ls]
            [re-frame.core :as re-frame]))

(defn get-item-cofx [coeffects keys]
  (let [values (if (vector? keys)
                 (ls/get-items! keys)
                 {key (ls/get-item! key)})]
    (assoc coeffects :local-storage values)))

(defn set-item-fx [objects]
  (map (fn [[key value]] (ls/set-item! key value)) objects))

(defn remove-item-fx [keys]
  (if (vector? keys)
    (map (fn [key] (ls/remove-item! key)) keys)
    (ls/remove-item! keys)))

(defn all-item-cofx [cofx]
  (assoc cofx :local-storage (ls/get-all!)))

; All coeffects registered.
(re-frame/reg-cofx ::get  get-item-cofx)
(re-frame/reg-cofx ::all  all-item-cofx)

; All effects registered.
(re-frame/reg-fx ::set    set-item-fx)
(re-frame/reg-fx ::remove remove-item-fx)
(re-frame/reg-fx ::clear  ls/clear!)
