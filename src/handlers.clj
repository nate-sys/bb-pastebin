(ns handlers
  (:require [db :as db]
            [taoensso.timbre :as timbre]))

(def ^:private internal-error {:status 500 :body "Internal server error (>.<)\n"})
(def ^:private bad-request {:status 400 :body "bad request (-_-)\n"})
(def ^:private not-found {:status 404 :body "not found (T-T)\n"})

(defn- print-info [req params]
  (timbre/info (->> params
                    (map req)
                    (interpose " ")
                    (apply str))))

(defn hello [req]
  (print-info req [:remote-addr :request-method :uri])
  {:status 200
   :body "hello world"})

(defn create-paste [req]
  (print-info req [:remote-addr :request-method :content-length])
  (if-some [body (slurp (:body req))]
    (if-some [id (:last-inserted-id (db/create-paste body))]
      {:status 201
       :body (str "paste id:" id)}
      internal-error)
    bad-request))

(defn get-paste [req]
  (print-info req [:remote-addr :request-method :uri])
  (if-some [id (-> req
                   :params
                   :id
                   (Integer/parseInt))]
    (if-some [content (:content (db/get-paste id))]
      {:status 200
       :body content}
      not-found)
    bad-request))

