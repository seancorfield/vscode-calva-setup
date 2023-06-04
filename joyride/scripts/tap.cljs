(ns tap
  (:require ["ext://betterthantomorrow.calva$v0" :as calva]
            ["vscode" :as vscode]
            [promesa.core :as p]))

(p/let [[_ f] (calva/ranges.currentFunction)
        p     (-> vscode/window.activeTextEditor
                  .-selection
                  .-active)
        [r s] (if (= "->" f)
                [(vscode/Range. p p) nil]
                (calva/ranges.currentForm))]
  (calva/editor.replace vscode/window.activeTextEditor r
                        (str "(doto " s (when s " ") "tap>)")))
