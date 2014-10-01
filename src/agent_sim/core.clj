(ns agent-sim.core
  (:require [quil.core :as q])
  (:use [agent-sim.agents :as a])
  (:use [agent-sim.util :as u]))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; IMPORTANT SIMULATION ATOMS

(def sim-argmap
  (atom {:fps 10
         :background-color 250
         :frame-size [600 400]
         :frame-title "Scatter Rects"}))

(def agents (atom []))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; SIMULATION AGENTS (will be in other files eventually)

(def my-agent (->
                (assoc a/dummy-agent :iterate (fn [self]
                                                (->
                                                  (assoc-in self [:vars :x]
                                                            (+ 
                                                              (get-in self [:vars :x])
                                                              (u/sim-random -2 2)))
                                                  (assoc-in [:vars :y]
                                                            (+ 
                                                              (get-in self [:vars :y])
                                                              (u/sim-random -2 2))))))
                (assoc :init (fn [self]
                               (-> 
                                 (assoc-in self [:vars :ID] (u/get-new-id))
                                 (assoc-in [:vars :x] (u/sim-random 
                                                        (get-in @sim-argmap [:frame-size 0])))
                                 (assoc-in [:vars :y] (u/sim-random 
                                                        (get-in @sim-argmap [:frame-size 1]))))))))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
; SIMULATION (and quill) FUNCTIONS

(defn init-all-agents
  []
  (let [k (count @agents)]
    (loop [setupi (atom 0)]
      (if (< @setupi k)
        (do
          (reset! agents (assoc @agents
                                @setupi
                                ((get-in @agents [@setupi :init]) (get @agents @setupi)))))
          (recur (swap! setupi inc))))))

(defn setup "QUIL FUNCTION"[]
  (q/smooth)                                        ;; Turn on anti-aliasing
  (q/frame-rate (:fps @sim-argmap))                 ;; Set framerate
  (q/background (:background-color @sim-argmap))   ;; Set the background colour
  ;(reset! agents (conj @agents my-agent))
  
  (init-all-agents))

(defn iterate-all-agents
  []
  (let [k (count @agents)]
    (loop [i (atom 0)]
      (if (< @i k)
        (do
          (reset! agents (assoc @agents
                                @i 
                                ((get-in @agents [@i :iterate]) (get @agents @i)))))
          (recur (swap! i inc))))))

(defn draw "QUIL FUNCTION" []
  (iterate-all-agents)
  (q/background (:background-color @sim-argmap))  ; Clear the frame
  
  (let [k (count @agents)]
    (loop [i 0]
      (if (< i k)
        (do
          (q/stroke (get-in @agents [i :draw :stroke-color]))
          (q/stroke-weight (get-in @agents [i :draw :stroke-weight]))
          (q/fill (get-in @agents [i :draw :fill-color]))
          (get-drawable-shape (get @agents i))
          (recur (inc i)))))))

(defn run-simulation
  [argmap]
  (do
    (reset! agents (apply conj @agents 
                          (vec (repeat 3 my-agent))))
    (q/defsketch simulation                ;; Define a new sketch named simulation
      :title (:frame-title @sim-argmap)    ;; Set the title of the sketch
      :setup setup                         ;; Specify the setup fn
      :draw draw                           ;; Specify the draw fn
      :size (:frame-size @sim-argmap))))

