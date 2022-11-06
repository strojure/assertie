(ns strojure.assertie.core
  "Core assert macros."
  (:require [strojure.assertie.impl :as impl]))

#?(:clj  (set! *warn-on-reflection* true)
   :cljs (set! *warn-on-infer* true))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defmacro assert-pred
  "Evaluates `(pred x)` and throws an exception if it does not evaluate to
  logical true. Returns `true` to be used directly in :pre/:post conditions.
  Attaches failed `:asserting/value` and `:asserting/type` in ex-data.

      (assert-pred \"1\" string?)
      ;=> true

      (assert-pred (inc 0) string? \"Expect string\")
      ;Expect string - Assert failed: (assert-pred (inc 0) string?)
      ; #:asserting{:value 1, :type java.lang.Long}
  "
  ([x pred] `(assert-pred ~x ~pred nil))
  ([x pred message]
   `(impl/assert-impl ~x (~pred ~'x) '(~'assert-pred ~x ~pred)
                      ~message (impl/ex-data-fn ::assert-pred ~'x))))

(defmethod impl/ex-data-fn ::assert-pred
  [_ value]
  (fn [_throwable]
    (when (impl/asserting-value? value)
      (cond-> {:asserting/value value}
        (some? value) (assoc :asserting/type (type value))))))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defn not-thrown
  "Returns predicate function which is true if `(f x)` does not throw exception.
  Can be used to assert expressions for errorless flow instead of truthy result.

      (assert-pred [] first)
      ;Assert failed: (assert-pred [] first)

      (assert-pred [] (not-thrown first))
      ;=> true

      (assert-pred 1 (not-thrown first))
      ;Assert failed: (assert-pred 1 (not-thrown first))
      ; #:asserting{:value 1, :type java.lang.Long}
      ;Don't know how to create ISeq from: java.lang.Long
  "
  [f]
  (fn [x] (f x) true))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defmacro assert-expr
  "Evaluates expr and throws an exception if it does not evaluate to logical
  true. Similar to `clojure.core/assert` but with better reporting. Returns
  `true` to be used directly in :pre/:post conditions. Attaches failed
  `:asserting/value` (`false` or `nil`) in ex-data.

      (assert-expr (= 1 (inc 0)))
      ;=> true

      (assert-expr (= 1 2) \"Expect 1=2\")
      ;Expect 1=2 - Assert failed: (assert-expr (= 1 2))
      ; #:asserting{:value false}
  "
  ([expr] `(assert-expr ~expr nil))
  ([expr message]
   `(impl/assert-impl ~expr ~'x '(~'assert-expr ~expr)
                      ~message (impl/ex-data-fn ::assert-expr ~'x))))

(defmethod impl/ex-data-fn ::assert-expr
  [_ value]
  (fn [_throwable]
    (when (impl/asserting-value? value)
      {:asserting/value value})))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
