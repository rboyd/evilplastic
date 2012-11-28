(ns robust.api.robust-api
	(:gen-class))

(import '(robust.pc RobustFactory))
(import '(robust.pc.api RobustAPISystemAsync RobustAPISystemAsync$BodyLoggerHandler))
(import '(robust.pc.api RobustAPILightAsync RobustAPILightAsync$LightListener))
(import '(robust.pc.api RobustAPIMoveAsync RobustAPIMoveAsync$MoveDoneListener))
(import '(robust.pc.api RobustAPITouchAsync RobustAPITouchAsync$TouchListener))
(import '(robust.pc.api RobustAPISonarAsync RobustAPISonarAsync$ProximityAlarmListner))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; System API

(def *robust-factory* (RobustFactory/getInstance))

(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPISystem) 
	(do
	  (def *robust-system-sync-api* (. *robust-factory* getRobustCommandsSystem)) 
	  (defn rb-system-shutdown [] (. *robust-system-sync-api* shutdown))
    (defn rb-system-remote-shutdown [] (. *robust-system-sync-api* remoteShutdown))
    (defn rb-system-startup [] (. *robust-system-sync-api* startup))
    (defn rb-system-ping [] (. *robust-system-sync-api* ping))
    (defn rb-system-body-name [] (. *robust-system-sync-api* bodyName))
    (defn rb-system-body-version [] (. *robust-system-sync-api* bodyVersion))
	)
	(println "System Sync API is not supported"))

(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPISystemAsync)
		(defn rb-system-async-set-nxt-logger [log-handler]
			"Usage:
			 	(defn logger [msg] \"to do sth with msg\")
			 	(rb-system-async-set-nxt-logger logger)"
			(. (. *robust-factory* getRobustCommandsSystemAsync) registerLogEvent 
				(proxy [RobustAPISystemAsync$BodyLoggerHandler] [] 
					(handleLogEvent [msg] (log-handler msg)))))
	(println "Robust System Async API is not supported"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Light API

(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPILight) 
    (defn rb-get-light-value []
    	"Usage: (rb-light-value)" 
    	(. (. *robust-factory* getRobustCommandsLight) getLightValue))
	(println "Robust Light Sync API is not supported"))

(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPILightAsync)
		(defn rb-light-async-handler [light-handler alarm-value tolerance-range]
			"Usage:
				(defn light-handler [value] (println value))
			 	(rb-light-async-handler light-handler value range)" 
			(. (. *robust-factory* getRobustCommandsLightAsync) registerLightListener 
				(proxy [RobustAPILightAsync$LightListener] [] 
					(handleLightValue [light-value] (light-handler light-value))
					(getAlarmValue [] alarm-value)
					(getTolerance [] tolerance-range))))
	(println "Robust Light Async API is not supported"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Move API

(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPIMove) 
	(do
    (defn rb-move-forward [length speed]
    	"Usage: (rb-move-forward 100 50)"
    	(.  (. *robust-factory* getRobustCommandsMove) moveForward length speed -1))
    (defn rb-move-smooth-turn [degrees radius speed]
    	"Usage: (rb-move-smooth-turn 90 30 50)" 
    	(.  (. *robust-factory* getRobustCommandsMove) moveCircly degrees radius speed))
    (defn rb-move-inplace-turn [degrees speed]
    	"Usage: (rb-move-inplace-turn 90 50)" 
    	(.  (. *robust-factory* getRobustCommandsMove) moveTurnInPlace degrees speed))
    (defn rb-move-stop-now []
    	"Usage: (rb-move-stop-now)" 
    	(.  (. *robust-factory* getRobustCommandsMove) stopNow))
    (defn rb-move-stop-moving []
    	"Usage: (rb-move-stop-moving)" 
    	(.  (. *robust-factory* getRobustCommandsMove) stopMoving))
    (defn rb-move-wait-for-new-move []
    	"Usage: (rb-move-wait-for-new-move)" 
    	(.  (. *robust-factory* getRobustCommandsMove) waitForMoveChange))
    (defn rb-move-info []
    	"Usage: (rb-move-info)" 
    	(.  (. *robust-factory* getRobustCommandsMove) moveStatus))    	
	);; do
	(println "Robust Move Sync API is not supported"))
	
(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPIMoveAsync)
		(defn rb-move-async-handler [move-done-handler]
			"Usage:
				(defn move-done-handler [value] (println value))
			 	(rb-move-async-handler move-done-handler)" 
			(. (. *robust-factory* getRobustCommandsMoveAsync) registerMoveDoneListener 
				(proxy [RobustAPIMoveAsync$MoveDoneListener] [] 
					(handleMoveDoneEvent [move-no] (move-done-handler move-no)))))
	(println "Robust Move Async API is not supported"))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Touch API

(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPITouch)
	(defn rb-get-touch-state [] 
		(aget (. (. *robust-factory* getRobustCommandsTouch) getTouchSenseState) 0))
		(println "Robust Touch Sync API is not supported"))
		
(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPITouchAsync)
	(defn rb-touch-async-handler [touch-handler] 
			"Usage:
				(defn rb-touch-handler [value] (println value))
			 	(rb-touch-async-handler rb-touch-handler)"
			(. (. *robust-factory* getRobustCommandsTouchAsync) registerTouchListener 
				(proxy [RobustAPITouchAsync$TouchListener] [] 
					(handleTouchEvent [touch-state] (touch-handler (aget touch-state 0))))))
	(println "Robust Touch Async API is not supported"))
	
;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Sonar API
	
(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPISonar)
	(do 
	(defn rb-get-sonar-distance [] 
		(. (. *robust-factory* getRobustCommandsSonar) getDistance))
	(defn rb-get-sonar-target [] 
		(. (. *robust-factory* getRobustCommandsSonar) getSonarTarget))
	(defn rb-set-sonar-target [target] 
		(. (. *robust-factory* getRobustCommandsSonar) setSonarTarget target)))
		(println "Robust Sonar Sync API is not supported"))
			 	
(if (. *robust-factory* isAPISupported robust.pc.api.RobustAPISonarAsync) 
	(defn rb-sonar-async-handler [sonar-handler, alarm-distance, mode] 
			"Usage:
				(defn rb-sonar-handler [distance] (println distance))
			 	(rb-sonar-async-handler rb-sonar-handler 200 0)"
			(. (. *robust-factory* getRobustCommandsSonarAsync) registerProximityAlarmListener 
				(proxy [RobustAPISonarAsync$ProximityAlarmListner] [] 
					(handleProximityAlarm [distance] (sonar-handler distance))
					(getAlarmDistance [] alarm-distance)
					(getMode [] mode))))
	(println "Robust Sonar Async API is not supported"))
