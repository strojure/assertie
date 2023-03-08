# assertie

Macros for runtime assertion in Clojure(Script).

[![Clojars Project](https://img.shields.io/clojars/v/com.github.strojure/assertie.svg)](https://clojars.org/com.github.strojure/assertie)

[![cljdoc badge](https://cljdoc.org/badge/com.github.strojure/assertie)](https://cljdoc.org/d/com.github.strojure/assertie)
[![tests](https://github.com/strojure/assertie/actions/workflows/tests.yml/badge.svg)](https://github.com/strojure/assertie/actions/workflows/tests.yml)

## Motivation

Runtime asserts with better reporting about failed asserting value.

## Features
- Ex-data with info about failed asserting value.
- Predicate based assertions.
- Return `true` instead of `nil` to be used directly in :pre/:post conditions.
- Argument order is designed to be used with `doto` macro.
- `Exception`-inherited exceptions instead of `Throwable`-inherited ones.

## API

### assert-pred

Main macro to assert value/expression by predicate function.

```clojure
(ns readme.api-01-assert-pred
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
```

### assert-expr

Secondary macro similar to `clojure.core/assert`.

```clojure
(ns readme.api-02-assert-expr
  (:require [strojure.assertie.core :as a :include-macros true]))

(a/assert-expr (= 1 (inc 0)))
;=> true

(a/assert-expr (= 1 2))
;Assert failed: (assert-expr (= 1 2))
; #:asserting{:value false}

(a/assert-expr (= 1 2) "Expect 1=2")
;Expect 1=2 - Assert failed: (assert-expr (= 1 2))
; #:asserting{:value false}
```

### assert-spec

Custom macro to assert against spec. This macro is an example of implementation
of new macros on top of `assertie`.

```clojure
(ns readme.api-03-assert-spec
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
```