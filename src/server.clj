(ns server (:require [clojure.string :as s]
                     [honeysql.core :as sql]
                     [org.httpkit.server :as hk]
                     [honeysql.helpers :as h]
                     [pod.babashka.go-sqlite3 :as sqlite]
                     [taoensso.timbre :as timbre]))

(def db-file "./bb-pastebin.db")

(defn init-db []
  (sqlite/execute!
   db-file
   ["CREATE TABLE IF NOT EXISTS PASTES (id INTEGER PRIMARY KEY, content TEXT)"]))

(defn create-paste [content]
  {:status 201
   :body (->> (-> (h/insert-into :pastes)
                  (h/values [{:content content}])
                  (sql/format))
              (sqlite/execute! db-file)
              :last-inserted-id
              (str "paste id: "))})

(defn get-paste [uri]
  {:status 200
   :body (->> (sql/format {:select [:content] :from [:pastes] :where [:= :id uri]})
              (sqlite/query db-file)
              first
              :content
              str)})

(defn app [req]
  (timbre/info (:remote-addr req)
               (get (:headers req) "user-agent")
               (:request-method req)
               (:uri req))
  (case (:request-method req)
    :get (get-paste (-> req :uri (s/replace #"/" "") Integer/parseInt))
    :post (create-paste (-> req :body slurp))))

(defn serve []
  (println "starting server ...")
  (hk/run-server app {:port 8080})
  (println "server started at https://localhost:8080")
  @(promise))
