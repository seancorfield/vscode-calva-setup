(ns ns
  (:require ["ext://betterthantomorrow.calva$v0" :as calva]
            ["vscode" :as vscode]
            [promesa.core :as p]))

(p/let [p (-> vscode/window.activeTextEditor
              .-document
              (.positionAt 0))
        ns-form (-> (calva/ranges.currentForm vscode/window.activeTextEditor p)
                    second)]
  (-> (calva/repl.evaluateCode (calva/repl.currentSessionKey)
                               "(clojure.core/tap> \"evaluating ns...\")")
      (.then (fn [_]
               (calva/repl.evaluateCode (calva/repl.currentSessionKey) ns-form)))
      (.then (fn [ns-val]
               (calva/repl.evaluateCode
                (calva/repl.currentSessionKey)
                (str "(clojure.core/tap> \""
                     (.-ns ns-val)
                     " evaluated\")"))))))
