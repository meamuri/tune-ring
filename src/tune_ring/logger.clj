(ns tune-ring.logger
  (:require [taoensso.timbre :as timbre]
            [cheshire.core :as json]))

(defn- json-output [{:keys [level msg_ instant]}]
  (let [msg (read-string (force msg_))]
    (json/generate-string {:timestamp instant
                           :level level
                           :msg msg})))

(defn init []
  (timbre/merge-config! {:output-fn json-output}))
