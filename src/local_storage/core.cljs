(ns local-storage.core)

(defn get-item!
  "Get item from localStorage and returns it. Accepts keywords."
  [key]
  (let [value
        (->> (name key)
             (.getItem js/localStorage)
             (.parse js/JSON))]
    (js->clj value :keywordize-keys true)))

(defn- assoc-items! [acc key]
  (assoc acc key (get-item! key)))

(defn get-items!
  "Get items from localStorage and returns it. Accepts a vector of keywords."
  [keys]
  (reduce assoc-items! {} keys))

(defn set-item!
  "Set item in localStorage and returns the value provided in argument.
   Accepts keywords and EDN."
  [key value]
  (->> (clj->js value)
       (.stringify js/JSON)
       (.setItem js/localStorage (name key)))
  value)

(defn set-items!
  "Set items in localStorage and returns the value provided in argument.
   Accepts a map of keywords and EDN."
  [objects]
  (map (fn [[key value]] (set-item! key value)))
  objects)

(defn remove-item!
  "Remove item from localStorage and returns the value which was stored inside.
   Accepts keywords."
  [key]
  (let [value (get-item! key)]
    (->> (name key)
         (.removeItem js/localStorage))
    value))

(defn remove-items!
  "Remove items from localStorage and returns the values which was stored inside.
   Accepts a vector of keywords."
  [keys]
  (map (fn [key] (remove-item! key))))

(defn length!
  "Returns the length of localStorage."
  []
  (.-length js/localStorage))

(defn get-all!
  "Returns all the value stored in localStorage associated with their keys."
  ([n objects]
   (if (< n 0)
     objects
     (let [key (keyword (.key js/localStorage n))
           value (get-item! key)]
       (get-all! (- n 1) (assoc objects key value)))))
  ([] (let [total (- (length!) 1)]
        (get-all! total {}))))

(defn clear!
  "Clear all localStorage and returns what’s contained inside."
  []
  (let [all (get-all!)]
    (.clear js/localStorage)
    all))
