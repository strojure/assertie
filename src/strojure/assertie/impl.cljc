(ns strojure.assertie.impl)

#?(:clj  (set! *warn-on-reflection* true) 
   :cljs (set! *warn-on-infer* true))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defmulti ex-data-fn
  "Returns ex-data function by `id` and asserting value."
  {:arglists '([id, value])}
  (fn [id, _] id))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defn asserting-value?
  "True if x is asserting value."
  [x]
  (not= ::no-value x))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,

(defn exception
  "Returns assertion exception with message:
  `{message} - Assert failed: {form}` and ex-data from `(edf throwable)`.
  Arguments `message`, `edf` and `throwable` can be `nil`."
  [form message edf throwable]
  (-> (ex-info (str (some-> message (str " - "))
                    "Assert failed: " (cond-> form (nil? form) pr-str))
               (or (when edf (edf throwable)) {})
               throwable)
      #?(:clj (#'clojure.core/elide-top-frames "strojure.assertie.impl$exception"))))

(defmacro assert-impl
  "Evaluates `x` and `test`, catches exception and throws an exception if `test`
  does not evaluate to logical true or other exceptions were thrown. Evaluated
  `x` is available in implementing macros as symbol `'x`, which is `::no-value`
  when evaluation failed. Returns `true` when assertion passed."
  [x test form message edf]
  (let [ex-type (if (:ns &env) :default 'Throwable)]
    `(if-let [{e# :throwable, ~'x :x, :or {~'x ::no-value}}
              (try (let [~'x ~x]
                     (try (when-not ~test {:x ~'x})
                          (catch ~ex-type e# {:x ~'x :throwable e#})))
                   (catch ~ex-type e# {:throwable e#}))]
       (throw (exception ~form ~message ~edf e#))
       true)))

;;,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,
