(ns robust.logging
	(:gen-class))

(import '(java.io File FileInputStream))
(import '(java.util Properties))

(import '(org.apache.commons.logging Log))
(import '(org.apache.commons.logging LogFactory))
(import '(org.apache.log4j PropertyConfigurator))

(def *default-logger* (LogFactory/getLog "robust"))

;; (defn log-info [msg] (do (. *default-logger* info msg) (str msg)))
;; (defn log-error [msg ex] (do (. *default-logger* error msg ex) (str msg)))

(defn log-info [msg] (do (println (str "INFO: " msg)) (str msg)))
(defn log-warn [msg] (do (println (str "WARN: " msg)) (str msg)))
(defn log-error [msg ex] (do (println (str "ERROR: " msg)) (str msg)))
