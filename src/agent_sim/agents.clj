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

(defn get-drawable-shape
  "Works with quil ellipse and rect"
  [agent]
  (let [x (get-in agent [:vars :x])
        y (get-in agent [:vars :y])
        w (get-in agent [:vars :width])
        h (get-in agent [:vars :height])]
    ((get-in agent [:vars :shape]) x y w h)))
