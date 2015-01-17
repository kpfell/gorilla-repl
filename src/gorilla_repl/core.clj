;;;; This file is part of gorilla-repl. Copyright (C) 2014-, Jony Hudson.
;;;;
;;;; gorilla-repl is licenced to you under the MIT licence. See the file LICENCE.txt for full details.

(ns gorilla-repl.core
  (:require [org.httpkit.server :as server]
            [gorilla-repl.renderer :as renderer] ;; this is needed to bring the render implementations into scope
            [gorilla-repl.version :as version]
            [gorilla-repl.api :as api]
            [gorilla-repl.nrepl-servers :as nrepl]
            [clojure.set :as set]
            [clojure.java.io :as io])
  (:gen-class))



(defn run-gorilla-server
  [conf]
  ;; get configuration information from parameters
  (let [version (or (:version conf) "develop")
        webapp-requested-port (or (:port conf) 0)
        ip (or (:ip conf) "127.0.0.1")
        nrepl-requested-port (or (:nrepl-port conf) 0)  ;; auto-select port if none requested
        project (or (:project conf) "no project")
        keymap (or (:keymap (:gorilla-options conf)) {})
;;        _ (swap! excludes (fn [x] (set/union x (:load-scan-exclude (:gorilla-options conf)))))
        ]
    ;; app startup
    (println "Gorilla-REPL:" version)
    ;; build config information for client
;;    (set-config :project project)
;;    (set-config :keymap keymap)
    ;; check for updates
    (version/check-for-update version)  ;; runs asynchronously
    ;; first startup nREPL
    (nrepl/start-and-connect nrepl-requested-port)
    ;; and then the webserver
    (let [s (server/run-server #'api/app-routes {:port webapp-requested-port :join? false :ip ip})
          webapp-port (:local-port (meta s))]
      (spit (doto (io/file ".gorilla-port") .deleteOnExit) webapp-port)
      (println (str "Running at http://" ip ":" webapp-port "/worksheet.html ."))
      (println "Ctrl+C to exit."))))

(defn -main
  [& args]
  (run-gorilla-server {:port 8990}))
