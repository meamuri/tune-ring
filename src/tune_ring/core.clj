(ns tune-ring.core
  (:require [ring.middleware.cookies :refer [wrap-cookies]]
            [ring.middleware.params :refer [wrap-params]]
            [ring.util.response :as response]
            [ring.adapter.jetty :as jetty]
            [tune-ring.logger :as logger]
            [taoensso.timbre :as timbre :refer [info]]))

(defn foo
  "I don't do a whole lot."
  [x]
  (println x "Hello, World!"))

(defn handler [request]
  (let [seen-path [:seen :value]
        {:keys [cookies]} request
        seen? (get-in cookies seen-path)
        cookies-res (assoc-in {} seen-path true)
        who (get-in request [:params "woosh"])]
    (timbre/info who)
    (timbre/info seen?)
    (timbre/info cookies)
    (timbre/info cookies-res)    
    {:status 200
     :cookies cookies-res
     :body (if seen?
             "Already seen"
             "The first time to see it WOW")}))


(def app 
  (-> handler
      wrap-params
      wrap-cookies))
      ; (wrap-resource "public")
      ; (wrap-content-type)
      ; (wrap-not-modified)))

(def server (atom nil))

(defn- start []
  (logger/init)
  (reset! server (jetty/run-jetty app {:port 3000 :join? false})))

(defn- stop []
  (.stop @server)
  (reset! server nil))

(comment
  (start)
  (stop))
