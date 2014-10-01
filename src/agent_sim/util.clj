(ns agent-sim.util
  (:use [agent-sim.globals]))

(defn get-new-id
 []
 (let [id @next-available-id]
   (do
     (swap! next-available-id inc)
     id)))

(defn sim-random
  ([max]
    (rand max))
  ([min max]
    (+ (rand (- max min)) min)))

(defn print-agent-location
 [agent]
 (do
   (print (get-in agent [:vars :x]))
   (print " : ")
   (println (get-in agent [:vars :y]))))