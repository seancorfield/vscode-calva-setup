(ns clojuredocs
  (:require ["vscode" :as vscode]
            [clojure.edn :as edn]
            [promesa.core :as p]))

(def ^:private calva (vscode/extensions.getExtension "betterthantomorrow.calva"))

(def ^:private calvaApi (-> calva
                            .-exports
                            .-v0
                            (js->clj :keywordize-keys true)))

(defn- evaluate [code] ((:evaluateCode calvaApi) "clj" code))

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
            resp (evaluate code)
            url  (edn/read-string (.-result resp))]
      (vscode/commands.executeCommand "simpleBrowser.show" url))
    (p/catch (fn [e] (println (str "Evaluation error: " e)))))
