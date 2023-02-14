(ns javadoc
  (:require ["ext://betterthantomorrow.calva$v0" :as calva]
            ["vscode" :as vscode]
            [clojure.string :as str]
            [promesa.core :as p]))

(p/let [p (-> vscode/window.activeTextEditor
              .-document
              (.positionAt 0))
        ns-form (-> (calva/ranges.currentForm vscode/window.activeTextEditor p)
                    second)]
  (println ns-form)
  (-> (calva/repl.evaluateCode (calva/repl.currentSessionKey) ns-form)
      (.then #(calva/repl.evaluateCode
               "clj"
               (str "(clojure.core/tap> \""
                    (.-ns %)
                    " evaluated\")")))))
