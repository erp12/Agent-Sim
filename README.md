This project has been put on hold, most likely forever, in favor of helping develop https://github.com/lspector/pucks

# Agent-Sim

A Clojure library designed to allow user to define custom behaviors for 2D agents.

Agent-Sim uses quil for graphics processing.

## Usage

First you will have to define your own agents. Agents are a map containing 4 values.
Here is the template agent, called dummy-agent:
```clojure
(ns agent-sim.agents
  (:use [quil.core :as q])
  (:use [agent-sim.util :as u]))

(def dummy-agent {:vars {;ID, Location, Size, Shape, and problem specific vars
                         :x 0
                         :y 0
                         :width 10
                         :height 10
                         :shape #(q/rect %1 %2 %3 %4); x, y, w, h
                         :ID :not-initialized-yet}
                  :draw {;Color and Stroke Thickness
                         :stroke-color (u/sim-random 255)
                         :stroke-weight (u/sim-random 5)
                         :fill-color (u/sim-random 255)}
                  :init (fn [self]
                          (do 
                            (assoc-in self [:vars :ID] (u/get-new-id))
                            :no-init-for-agent))
                  :iterate (fn [self]
                             :no-iterate-for-agent)})
```
When defineing your own agents, you can start from dummy-agent and replace :init and :iterate with functions that agents and modify their contents.

The variables in :vars are required for quil, but you can modify their values and add more elements to :vars as needed.

To run the simulation you will need a problem specific argmap and a run-simulation function. Here is an example.
```clojure
(def sim-argmap
  (atom {:fps 10
         :background-color 250
         :frame-size [600 400]
         :frame-title "Scatter Rects"}))
         
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
```
The argmap must hold information for the quil sketch and the run-simulation function must 1) add all the agents atom 2) start the quil sketch.
