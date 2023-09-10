(ns server (:require [handlers :as handlers]
                     [clojure.edn :as edn]
                     [ruuter.core :as ruuter]
                     [org.httpkit.server :as hk]
                     [taoensso.timbre :as timbre]))
(def port 
  (->> "config.edn" slurp edn/read-string :port))

(def routes
  [{:path "/"
    :method :get
    :response handlers/hello} 
   {:path "/:id"
    :method :get
    :response handlers/get-paste}
   {:path "/"
    :method :post
    :response handlers/create-paste}])

(defn serve []
  (timbre/info "starting server ...")
  (hk/run-server #(ruuter/route routes %) {:port port})
  (timbre/info "server started at https://localhost:8080")
  @(promise))
