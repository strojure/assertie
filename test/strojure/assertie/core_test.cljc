(ns strojure.assertie.core-test
  (:require [clojure.test :as test :refer [deftest]]
            [strojure.assertie.core :as a :include-macros true])
  #?(:cljs (:require-macros [strojure.assertie.core-test :refer [t]])))

#_(test/run-tests)

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defn- explain
  [e]
  (loop [e e, xs []]
    (if e
      (recur (ex-cause e) (conj xs {:ex-message (ex-message e)
                                    :ex-data (ex-data e)}))
      xs)))

(defmacro ^:private t
  [expr]
  `(try ~expr (catch ~(if (:ns &env) :default 'Exception) e#
                (explain e#))))

(def ^:private Integer* (type 0))
(def ^:private String* (type ""))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(deftest assert-pred-t
  (test/are [expr result] (= result expr)

    (t (a/assert-pred "1" string?))
    true

    (t (a/assert-pred (inc 0) string?))
    [{:ex-message "Assert failed: (assert-pred (inc 0) string?)",
      :ex-data #:asserting{:value 1, :type Integer*}}]

    (t (a/assert-pred nil string?))
    [{:ex-message "Assert failed: (assert-pred nil string?)",
      :ex-data #:asserting{:value nil}}]

    (t (a/assert-pred "" number?))
    [{:ex-message "Assert failed: (assert-pred \"\" number?)",
      :ex-data #:asserting{:value "",
                           :type String*}}]

    (t (a/assert-pred (inc 0) string? "Require string"))
    [{:ex-message "Require string - Assert failed: (assert-pred (inc 0) string?)",
      :ex-data #:asserting{:value 1, :type Integer*}}]

    (t (a/assert-pred (inc 0) string? ["Require string"]))
    [{:ex-message "[\"Require string\"] - Assert failed: (assert-pred (inc 0) string?)",
      :ex-data #:asserting{:value 1, :type Integer*}}]

    (t (a/assert-pred 1 first))
    [{:ex-message "Assert failed: (assert-pred 1 first)",
      :ex-data #:asserting{:value 1, :type Integer*}}
     {:ex-message #?(:clj  "Don't know how to create ISeq from: java.lang.Long"
                     :cljs "1 is not ISeqable"),
      :ex-data nil}]

    #_:clj-kondo/ignore
    (t (a/assert-pred (first 1) int?))
    [{:ex-message "Assert failed: (assert-pred (first 1) int?)",
      :ex-data {}}
     {:ex-message #?(:clj  "Don't know how to create ISeq from: java.lang.Long"
                     :cljs "1 is not ISeqable"),
      :ex-data nil}]

    ))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(deftest not-thrown-t
  (test/are [expr result] (= result expr)

    (t (a/assert-pred [] (a/not-thrown first)))
    true

    (t (a/assert-pred 1 (a/not-thrown first)))
    [{:ex-message "Assert failed: (assert-pred 1 (a/not-thrown first))",
      :ex-data #:asserting{:value 1, :type Integer*}}
     {:ex-message #?(:clj  "Don't know how to create ISeq from: java.lang.Long"
                     :cljs "1 is not ISeqable"),
      :ex-data nil}]

    ))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(deftest assert-expr-t
  (test/are [expr result] (= result expr)

    (t (a/assert-expr (= 1 (inc 0))))
    true

    (t (a/assert-expr (= 1 2)))
    [{:ex-message "Assert failed: (assert-expr (= 1 2))",
      :ex-data #:asserting{:value false}}]

    (t (a/assert-expr (or false nil)))
    [{:ex-message "Assert failed: (assert-expr (or false nil))",
      :ex-data #:asserting{:value nil}}]

    (t (a/assert-expr (or nil false)))
    [{:ex-message "Assert failed: (assert-expr (or nil false))",
      :ex-data #:asserting{:value false}}]

    (t (a/assert-expr (= 1 2) "Require 1=2"))
    [{:ex-message "Require 1=2 - Assert failed: (assert-expr (= 1 2))",
      :ex-data #:asserting{:value false}}]

    #_:clj-kondo/ignore
    (t (a/assert-expr (first 1)))
    [{:ex-message "Assert failed: (assert-expr (first 1))",
      :ex-data {}}
     {:ex-message #?(:clj  "Don't know how to create ISeq from: java.lang.Long"
                     :cljs "1 is not ISeqable"),
      :ex-data nil}]

    ))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
