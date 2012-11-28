(ns evilplastic.core
 (:import [lejos.pc.comm NXTConnector NXTCommFactory NXTInfo NXTCommException]
          [java.io DataInputStream DataOutputStream]))

(defn connect []
 (def conn (NXTConnector.))
 (.connectTo conn "usb://")

 (def in-dat (DataInputStream. (.getInputStream conn)))
 (def out-dat (DataOutputStream. (.getOutputStream conn))))

(defn rot-motor [motor angle]
 (.writeChar out-dat (int motor))
 (.flush out-dat)
 (.writeInt out-dat angle)
 (.flush out-dat))

(defn sig-quit []
  (.writeChar out-dat (int \F))
  (.flush out-dat)
  (.writeInt out-dat 0)
  (.flush out-dat)
  (.close conn))

(defn grab []
  (rot-motor \C -43)
  (Thread/sleep 500))

(defn pull []
  (rot-motor \C -28)
  (Thread/sleep 1000))

(defn push []
  (rot-motor \C 28)
  (Thread/sleep 500))

(defn release []
  (rot-motor \C 43)
  (Thread/sleep 500))

(defn flip []
  (grab)
  (pull)
  (push)
  (release))

(defn turn
  ([] (rot-motor \A 90))
  ([angle] (rot-motor \A angle)))

(defn d []
 (grab) (turn 90) (release))

(defn d' []
 (grab) (turn -90) (release))

(defn u []
 (flip) (flip) (grab) (turn 90) (release) (flip) (flip))

(defn u' []
 (flip) (flip) (grab) (turn -90) (release) (flip) (flip))

(defn f []
  (turn -90) (flip) (grab) (turn 90) (release) (flip) (turn -90) (flip) (flip))

(defn f' []
  (turn -90) (flip) (grab) (turn -90) (release) (flip) (turn -90) (flip) (flip))

(defn b []
  (turn 90) (flip) (grab) (turn 90) (release) (flip) (turn 90) (flip) (flip))

(defn b' []
  (turn 90) (flip) (grab) (turn -90) (release) (flip) (turn 90) (flip) (flip))

(defn r []
  (turn -180) (flip) (grab) (turn 90) (release) (turn 180) (flip))

(defn r' []
  (turn -180) (flip) (grab) (turn -90) (release) (turn 180) (flip))

(defn l []
  (flip) (grab) (turn 90) (release) (turn 180) (flip) (turn 180))

(defn l' []
  (flip) (grab) (turn -90) (release) (turn 90) (turn 90) (flip) (turn 90) (turn 90))

(def six-spot [:u :d' :r :l' :f :b' :u :d'])

(defn do-six-spot []
 (let [fs (map #(resolve (symbol (name %))) six-spot)]
  (doseq [f fs] (f))))


; (f) (f) (b) (b) (r) (r) (l) (l) (u) (u) (d) (d)

; (d') (d') (u') (u') (l') (l') (r') (r') (b') (b') (f') (f')

