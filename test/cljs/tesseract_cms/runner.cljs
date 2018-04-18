(ns graphql-demo.runner
    (:require [doo.runner :refer-macros [doo-tests]]
              [graphql-demo.core-test]))

(doo-tests 'graphql-demo.core-test)
