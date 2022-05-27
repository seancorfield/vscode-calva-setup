(ns javadoc
  (:require ["vscode" :as vscode]
            [clojure.edn :as edn]
            [clojure.string :as str]
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

(defn javadoc-url [code]
  (str
   "(let [c-o-o " code
   " ^Class c (if (instance? Class c-o-o) c-o-o (class c-o-o))] "
   "  (->"
   "   ((requiring-resolve 'clojure.java.javadoc/javadoc-url)"
   "    (.getName c))"
   "   (clojure.string/replace" ; strip inner class
   "    #\"\\$[a-zA-Z0-9_]+\" \"\")"
   "   (clojure.string/replace" ; force https
   "    #\"^http:\" \"https:\")"
   "))"))

(-> (p/let [code (javadoc-url (selected-text))
            resp (evaluate code)
            url  (edn/read-string (.-result resp))]
      (if (str/starts-with? url "https://www.google.com")
        (vscode/commands.executeCommand "vscode.open" url)
        (vscode/commands.executeCommand "simpleBrowser.show" url)))
    (p/catch (fn [e] (println (str "Evaluation error: " e)))))
