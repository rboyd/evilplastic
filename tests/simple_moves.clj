(ns robust.api.tests.simple-moves
	(:use nlists.utils)
	(:use robust.api.robust-api))

(defn console-break []
        (do
                (println "-- press enter --")
          (.readLine (new BufferedReader *in*))
          nil
))

(rb-system-startup)

(rb-move-forward 200 50)
(rb-move-smooth-turn 90 30 50)
(rb-move-forward 200 50)
(rb-move-smooth-turn 90 30 50)
(rb-move-forward 200 50)
(rb-move-smooth-turn 90 30 50)
(rb-move-forward 200 50)
(rb-move-smooth-turn 90 30 50)

(console-break)

(rb-system-shutdown)