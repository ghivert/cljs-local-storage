# CLJS Local Storage

Local Storage is a package aiming to provide an easy and lightweight wrapper around the native `localStorage API`, but with the ClojureScript niceties added.

I’m not sure wether I should upload it to Clojars… For now, if the project is interesting for you, grab it directly, it remains in two files, and let me a sweet comment to indicate that you’re interested in the project. I really don’t see the point to push another project with little or no added value and which only will pollute the global Clojars. This repo is also there to help me learn things in Clojure.

I’ll try to push the package further, so you can probably expect updates in a near future (as the time of writing those lines, the 25th February 2020).

# Getting Started

Local Storage is nice and easy to use. It exposes 5 functions, all of them are unsafe. Be careful when using them.

```clojure
(ns your-cool-project.core
  (:require [local-storage.core :as local-storage]))

(defn do-things []
  (let [item-1       (local-storage/get-item! :my-key)
        items        (local-storage/get-items! [:my-key :my-other-key])
        item-1-again (local-storage/set-item! :my-key item-1)
        items        (local-storage/set-items! {:my-key item :my-other-key item})
        ls-length    (local-storage/length!)
        all-items    (local-storage/get-all!)
        item-1-too   (local-storage/remove-item! :my-key)
        items        (local-storage/remove-items! [:my-key :my-other-key])
        all          (local-storage/clear!)]
  (assert (= item-1 item-1-again item-1-too))
  (assert (= ls-length 0))
  (assert (= all-items {:my-key item-1}))
```

But Local Storage also have a re-frame utility!

```clojure
(ns your-cool-project.events
  (:require [re-frame.core :as rf :refer [reg-event-fx reg-event-db inject-cofx]]
            [local-storage.re-frame]))

;; local-storage/set accepts a map {:keyword item}.
;; local-storage/remove accepts an array of keywords.
;; local-storage/clear clears the localStorage.
(reg-event-fx :event
  (fn [cofx]
    {::local-storage/set    {:my-key item}
     ::local-storage/remove [:my-key]
     ::local-storage/clear}))

;; local-storage/get accepts both a keyword or an array of keyword.
;;   The result value is always a map {:keyword value}.
(reg-event-fx :event
  [(inject-cofx ::local-storage/get [:my-key :my-other-key])]
  (fn [cofx]
    (let [{:keys [my-key my-other-key]} (:local-storage cofx)]
      (println my-key)
      (println my-other-key))))

(reg-event-fx :event
  [(inject-cofx ::local-storage/get :my-key)]
  (fn [cofx]
    (println (:my-key (:local-storage cofx)))))

;; local-storage/all returns a map {:keyword value}.
(reg-event-fx :event
  [(inject-cofx ::local-storage/all)]
  (fn [cofx]
    (println (:local-storage cofx))))
```
