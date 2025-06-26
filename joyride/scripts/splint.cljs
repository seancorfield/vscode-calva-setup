(ns splint
  (:require ["vscode" :as vscode]))

(defn- find-or-create-terminal [name]
  (or (some #(when (= name (.-name %)) %) vscode/window.terminals)
      (vscode/window.createTerminal #js {:name name})))

(let [file vscode/window.activeTextEditor.document.fileName]
  (doto (find-or-create-terminal "Splint")
    (.show)
    (.sendText (str "clojure -M:splint -o simple " file))))
