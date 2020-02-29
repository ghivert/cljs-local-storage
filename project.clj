(defproject local-storage "0.1.0"
  :description
  "local-storage provides an easy way to use `local-storage` with a lightweight wrapper.
   Everything is focused around simplicity and try to reproduce the options of
   `local-storage` to avoid cognitive overload, but of course by adding this little
   ClojureScript touch and adding some features."
  :url "https://github.com/ghivert/local-storage"
  :license {:name "MIT"
            :url "https://github/ghivert/local-storage/blob/master/LICENSE"}
  :dependencies [[org.clojure/clojurescript "1.10.520" :scope "provided"]
                 [re-frame "0.11.0" :scope "provided"]]
  :source-paths ["src"]
  :deploy-repositories
  [["releases" {:sign-releases false
                :url "https://clojars.org/repo"}]])
