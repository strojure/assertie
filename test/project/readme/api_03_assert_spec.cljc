(ns project.readme.api-03-assert-spec
  (:require [clojure.spec.alpha :as s]
            [strojure.assertie.spec-alpha :as a :include-macros true]))

(s/def ::string string?)

(a/assert-spec "1" ::string "Message")
;=> true

(a/assert-spec 1 ::string "Message")
;Message - Assert failed: (assert-spec 1 :project.readme.example.spec-01-assert-spec/string)
; {}
;Spec assertion failed
;1 - failed: string?
; #:clojure.spec.alpha{:problems [{:path [], :pred clojure.core/string?, :val 1, :via [], :in []}], :spec :project.readme.example.spec-01-assert-spec/string, :value 1, :failure :assertion-failed}
