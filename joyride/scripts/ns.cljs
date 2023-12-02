(ns ns
  (:require ["ext://betterthantomorrow.calva$v1" :as calva]))

(let [[ns-name ns-form] (calva/document.getNamespaceAndNsForm)]
  (-> (calva/repl.evaluateCode "clj" (str "(clojure.core/tap> \"evaluating "
                                          ns-name
                                          "...\")"))
      (.then
       (fn [_]
         (calva/repl.evaluateCode "clj" ns-form)))
      (.then
       (fn [ns-val]
         (calva/repl.evaluateCode "clj" (str "(clojure.core/tap> \""
                                             (.-ns ns-val)
                                             " evaluated\")"))))))
