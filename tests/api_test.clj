(ns robust.api.tests.api-test	
	(:use robust.api.robust-api))

(defn rnd [min max]
        "returns random number between min and max"
                (+ min (.nextInt (new java.util.Random) (-  max min))))

(defn collision-avoidance-handler [value]
	(if (= value 1) ;; touch is pushed
			(rb-move-stop-now)))

(defn explorer [speed] 
	"simple exploration algorithm with collision avoidance"
	(do 
		(rb-system-startup)
		(rb-touch-async-handler collision-avoidance-handler)
		(rb-move-forward (rnd 200 400) speed)
		(loop []
			(do				
				(rb-move-forward (rnd 200 400) speed)
				(if (= (. (rb-move-wait-for-new-move) code) 0)
					(do 
						(rb-move-forward 200 -100)			
						(rb-move-inplace-turn (rnd -120 120) speed)))			
				(recur)
			))))			