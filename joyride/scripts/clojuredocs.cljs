(ns clojuredocs
  (:require ["ext://betterthantomorrow.calva$v1.repl" :refer [evaluateCode]]
            ["ext://betterthantomorrow.calva$v1.document" :refer [getNamespace]]
            ["vscode" :as vscode]
            [clojure.edn :as edn]
            [promesa.core :as p]))

(defn- selected-text []
  (vscode/commands.executeCommand "calva.selectCurrentForm")
  (let [editor ^js vscode/window.activeTextEditor
        selection (.-selection editor)]
    (.getText (.-document editor) selection)))

(defn clojuredocs-url [code]
  (str
   " (str \"https://clojuredocs.org/\""
   " (-> (str (symbol #'" code "))"
   ;; clean up ? ! &
   "  (clojure.string/replace \"?\" \"%3f\")"
   "  (clojure.string/replace \"!\" \"%21\")"
   "  (clojure.string/replace \"&\" \"%26\")"
   "))"))

(-> (p/let [code (clojuredocs-url (selected-text))
            resp (evaluateCode "clj" code (getNamespace))
            url  (edn/read-string (.-result resp))]
      (vscode/commands.executeCommand "simpleBrowser.show" url))
    (p/catch (fn [e] (println (str "Evaluation error: " e)))))
