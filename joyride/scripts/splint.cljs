(ns splint
  (:require ["vscode" :as vscode]))

(def ^:private splint-command
  "The command to run Splint. If you have `bb` installed, you can use
   the first definition, otherwise use the second one."
  "bb -Sdeps '{:deps {io.github.noahtheduke/splint {:mvn/version \"1.25.0\"}}}' -m noahtheduke.splint"
  #_
  "clojure -M:splint")

(defn- find-or-create-terminal [name]
  (or (some #(when (= name (.-name %)) %) vscode/window.terminals)
      (vscode/window.createTerminal #js {:name name})))

(let [file vscode/window.activeTextEditor.document.fileName]
  (doto (find-or-create-terminal "Splint")
    (.show)
    (.sendText (str splint-command " -o simple " file)))
  nil)
