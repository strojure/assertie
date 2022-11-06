(ns strojure.assertie.spec-alpha-test
  (:require [clojure.spec.alpha :as s]
            [clojure.test :as test :refer [deftest]]
            [strojure.assertie.spec-alpha :as a :include-macros true])
  #?(:cljs (:require-macros [strojure.assertie.spec-alpha-test :refer [t]])))

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

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(s/def :test/string string?)

(deftest assert-spec-t
  (test/are [expr result] (= result expr)

    (t (a/assert-spec "1" :test/string "Message"))
    true

    (t (a/assert-spec 1 :test/string "Message"))
    #?(:clj
       [{:ex-message "Message - Assert failed: (assert-spec 1 :test/string)",
         :ex-data {}}
        {:ex-message "Spec assertion failed\n1 - failed: string?\r\n",
         :ex-data #:clojure.spec.alpha{:problems [{:path [], :pred 'clojure.core/string?, :val 1, :via [], :in []}],
                                       :spec :test/string,
                                       :value 1,
                                       :failure :assertion-failed}}]
       :cljs
       [{:ex-message "Message - Assert failed: (assert-spec 1 :test/string)",
         :ex-data #:asserting{:value 1, :type (type 0)}}
        {:ex-message "Spec assertion failed\n1 - failed: string?\n",
         :ex-data nil}])

    #_:clj-kondo/ignore
    (t (a/assert-spec (first 1) :test/string))
    [{:ex-message "Assert failed: (assert-spec (first 1) :test/string)",
      :ex-data {}}
     {:ex-message #?(:clj  "Don't know how to create ISeq from: java.lang.Long"
                     :cljs "1 is not ISeqable"),
      :ex-data nil}]

    ))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
