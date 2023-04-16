(ns remote-repl
  (:require ["vscode" :as vscode]
            [promesa.core :as p]))

(defn- start-tunnel [nrepl-port portal-port label remote-server]
  (let [terminal (vscode/window.createTerminal #js {:isTransient true
                                                    :name label
                                                    :message (str label " Remote REPL...")})]
    (.show terminal)
    (.sendText terminal (str "ssh -N"
                             " -L " nrepl-port ":localhost:" nrepl-port
                             " -L " portal-port ":localhost:" portal-port
                             " " remote-server))))

(defn- start-browser [portal-port]
  (vscode/commands.executeCommand "simpleBrowser.show" (str "http://localhost:" portal-port))
  (p/do
    (p/delay 2000)
    (vscode/commands.executeCommand "workbench.action.moveEditorToRightGroup")
    (p/delay 1000)
    (vscode/commands.executeCommand "workbench.action.focusFirstEditorGroup")))

(defn- connect-repl []
  (vscode/commands.executeCommand "calva.disconnect")
  (vscode/commands.executeCommand "calva.connect" #js {:disableAutoSelect true}))

(defn repl-setup [nrepl-port portal-port label remote-server]
  (start-tunnel nrepl-port portal-port label remote-server)
  (p/do
    (p/delay 2000)
    (start-browser portal-port)
    (p/delay 1000)
    (connect-repl)))
