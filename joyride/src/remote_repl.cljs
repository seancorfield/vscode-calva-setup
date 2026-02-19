(ns remote-repl
  (:require ["ext://betterthantomorrow.calva$v1" :as calva]
            ["fs" :as fs]
            ["path" :as path]
            ["vscode" :as vscode]
            [clojure.edn :as edn]
            [clojure.set :as set]
            [promesa.core :as p]))

(defn- start-tunnel [nrepl-port portal-port extension-port label remote-server]
  (let [terminal (vscode/window.createTerminal #js {:isTransient true
                                                    :name label
                                                    :message (str label " Remote REPL...")})]
    (.show terminal)
    (.sendText terminal (str "ssh -C -N"
                             " -L " nrepl-port ":localhost:" nrepl-port
                             " -L " portal-port ":localhost:" portal-port
                             " -R " extension-port ":localhost:" extension-port
                             " " remote-server))))

(defn- connect-repl [nrepl-port]
  (vscode/commands.executeCommand "calva.connect" #js {:port nrepl-port :connectSequence "Generic"}))

(defn- portal-config []
  (->  vscode/workspace.workspaceFolders
       first
       .-uri
       .-fsPath
       (path/join ".portal/vs-code.edn")
       (fs/readFileSync #js {:encoding "utf8"})
       edn/read-string))

(defn- repl-session-key-set []
  (->> (calva/repl.listSessions)
                      (map #(.-replSessionKey %))
                      (set)))

(defn repl-setup [nrepl-port portal-port label remote-server]
  (let [config   (portal-config)
        old-keys (repl-session-key-set)]
    (start-tunnel nrepl-port portal-port (:port config) label remote-server)
    (p/do
      (p/delay 2000)
      (connect-repl nrepl-port)
      (p/delay 1000)
      (let [new-keys (repl-session-key-set)
            new-key  (first (set/difference new-keys old-keys))]
        (calva/repl.evaluateCode new-key (pr-str (list 'spit ".portal/vs-code.edn" config)))))))
