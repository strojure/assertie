(defproject com.github.strojure/assertie "1.1.1-SNAPSHOT"
  :description "Macros for runtime assertion in Clojure(Script)."
  :url "https://github.com/strojure/assertie"
  :license {:name "The MIT License" :url "http://opensource.org/licenses/MIT"}

  :dependencies []

  :profiles {:provided {:dependencies [[org.clojure/clojure "1.11.1"]
                                       [org.clojure/clojurescript "1.11.60"]]}
             :dev,,,,, {:dependencies [;; clojurescript repl deps
                                       [com.google.guava/guava "31.1-jre"]]
                        :source-paths ["doc"]}}

  :deploy-repositories [["clojars" {:url "https://clojars.org/repo" :sign-releases false}]])
