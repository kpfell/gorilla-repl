(ns gorilla-repl.api
  (:require [ring.middleware.json :as json]
            [ring.middleware.params :as params]
            [ring.middleware.keyword-params :as keyword-params]
            [ring.util.response :as res]
            [compojure.route :as route]
            [compojure.core :refer :all]
            [compliment.core :as complete]))


;; a wrapper for JSON API calls
(defn wrap-api-handler
  [handler]
  (-> handler
      (keyword-params/wrap-keyword-params)
      (params/wrap-params)
      (json/wrap-json-response)))


;; API endpoint for getting completions
(defn completions
  [req]
  (when-let [stub (:stub (:params req))]
    (when-let [ns (:ns (:params req))]
      (res/response {:completions (complete/completions stub (symbol ns))}))))


;; the combined routes - we serve up everything in the "public" directory of resources under "/".
;; The REPL traffic is handled in the websocket-transport ns.
(defroutes app-routes
           ;(GET "/load" [] (wrap-api-handler load-worksheet))
           ;(POST "/save" [] (wrap-api-handler save))
           ;(GET "/completions" [] (wrap-api-handler completions))
           ;(GET "/gorilla-files" [] (wrap-api-handler gorilla-files))
           ;(GET "/config" [] (wrap-api-handler config))
           ;(GET "/repl" [] ws-relay/ring-handler)
           (route/resources "/")
           (route/files "/project-files" [:root "."]))
