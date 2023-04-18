(ns remote-repl
  (:require ["ext://betterthantomorrow.calva$v0" :as calva]
            ["fs" :as fs]
            ["path" :as path]
            ["vscode" :as vscode]
            [clojure.edn :as edn]
            [promesa.core :as p]))

(defn- start-tunnel [nrepl-port portal-port extension-port label remote-server]
  (let [terminal (vscode/window.createTerminal #js {:isTransient true
                                                    :name label
                                                    :message (str label " Remote REPL...")})]
    (.show terminal)
    (.sendText terminal (str "ssh -N"
                             " -L " nrepl-port ":localhost:" nrepl-port
                             " -L " portal-port ":localhost:" portal-port
                             " -R " extension-port ":localhost:" extension-port
                             " " remote-server))))

(defn- connect-repl [nrepl-port]
  (vscode/commands.executeCommand "calva.disconnect")
  (vscode/commands.executeCommand "calva.connect" #js {:port nrepl-port :connectSequence "Generic"}))

(defn- portal-config []
  (->  vscode/workspace.workspaceFolders
       first
       .-uri
       .-fsPath
       (path/join ".portal/vs-code.edn")
       (fs/readFileSync #js {:encoding "utf8"})
       edn/read-string))

(defn repl-setup [nrepl-port portal-port label remote-server]
  (let [config (portal-config)]
    (start-tunnel nrepl-port portal-port (:port config) label remote-server)
    (p/do
      (p/delay 2000)
      (connect-repl nrepl-port)
      (p/delay 1000)
      (calva/repl.evaluateCode "clj" (pr-str (list 'spit ".portal/vs-code.edn" config))))))
