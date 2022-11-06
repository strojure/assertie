(ns strojure.assertie.spec-alpha
  "Spec assert macro."
  (:require [clojure.spec.alpha :as s]
            [strojure.assertie.core :as a]
            [strojure.assertie.impl :as impl]))

#?(:clj  (set! *warn-on-reflection* true)
   :cljs (set! *warn-on-infer* true))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defmacro assert-spec
  "Evaluates `(s/assert* spec x)` and throws an exception if assertion failed.
  Returns `true` to be used directly in :pre/:post conditions. Thrown exception
  has original spec's assert exception in a cause."
  ([x spec] `(assert-spec ~x ~spec nil))
  ([x spec message]
   `(impl/assert-impl ~x (do (s/assert* ~spec ~'x) true) '(~'assert-spec ~x ~spec)
                      ~message (impl/ex-data-fn ::assert-spec ~'x))))

(defmethod impl/ex-data-fn ::assert-spec
  [_ value]
  (fn [throwable]
    (when-not (::s/failure (ex-data throwable))
      ((impl/ex-data-fn ::a/assert-pred value) throwable))))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
