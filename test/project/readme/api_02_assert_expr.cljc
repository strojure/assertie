(ns project.readme.api-02-assert-expr
  (:require [strojure.assertie.core :as a :include-macros true]))

(a/assert-expr (= 1 (inc 0)))
;=> true

(a/assert-expr (= 1 2))
;Assert failed: (assert-expr (= 1 2))
; #:asserting{:value false}

(a/assert-expr (= 1 2) "Expect 1=2")
;Expect 1=2 - Assert failed: (assert-expr (= 1 2))
; #:asserting{:value false}
