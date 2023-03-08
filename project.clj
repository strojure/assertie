(defproject com.github.strojure/assertie "1.0.2-17-SNAPSHOT"
  :description "Macros for runtime assertion in Clojure(Script)."
  :url "https://github.com/strojure/assertie"
  :license {:name "The Unlicense" :url "https://unlicense.org"}

  :dependencies []

  :profiles {:provided {:dependencies [[org.clojure/clojure "1.11.1"]
                                       [org.clojure/clojurescript "1.11.60"]]}
             :dev,,,,, {:dependencies [;; clojurescript tests
                                       [com.google.guava/guava "31.1-jre"]
                                       [olical/cljs-test-runner "3.8.0"]]
                        :source-paths ["doc"]}}

  :aliases {"cljs-test" ["run" "-m" "cljs-test-runner.main"]}

  :clean-targets ["target" "cljs-test-runner-out"]

  :deploy-repositories [["clojars" {:url "https://clojars.org/repo" :sign-releases false}]])
