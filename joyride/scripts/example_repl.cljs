(ns example-repl
  (:require [remote-repl :as r]))

(r/repl-setup 6666 7777 "QA" "qauser@10.10.10.10")
