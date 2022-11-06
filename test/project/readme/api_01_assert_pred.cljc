(ns project.readme.api-01-assert-pred
  (:require [strojure.assertie.core :as a :include-macros true]))

(a/assert-pred "1" string?)
;=> true

(a/assert-pred (inc 0) string?)
;Assert failed: (assert-pred (inc 0) string?)
; #:asserting{:value 1, :type java.lang.Long}

(a/assert-pred (inc 0) string? "Expect string")
;Expect string - Assert failed: (assert-pred (inc 0) string?)
; #:asserting{:value 1, :type java.lang.Long}

(a/assert-pred [] (a/not-thrown first))
;=> true

(a/assert-pred 1 (a/not-thrown first))
;Assert failed: (assert-pred 1 (a/not-thrown first))
; #:asserting{:value 1, :type java.lang.Long}
;Don't know how to create ISeq from: java.lang.Long
