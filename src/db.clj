(ns db
  (:require
   [honeysql.core :as sql]
   [honeysql.helpers :as h]
   [pod.babashka.go-sqlite3 :as sqlite]))

(def ^:private db-file "./bb-pastebin.db")

(defn init-db []
  (sqlite/execute!
   db-file
   ["CREATE TABLE IF NOT EXISTS PASTES (id INTEGER PRIMARY KEY, content TEXT)"]))

(defn create-paste [content]
  (sqlite/execute!
   db-file
   (-> (h/insert-into :pastes)
       (h/values [{:content content}])
       #_{:clj-kondo/ignore [:unresolved-var]}
       (sql/format))))

(defn get-paste [id]
  (->> {:select [:content]
        :from   [:pastes]
        :where  [:= :id id]}
       #_{:clj-kondo/ignore [:unresolved-var]}
       (sql/format)
       (sqlite/query db-file)
       first))

