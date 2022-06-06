(ns keypad.lib
  (:require [scad-clj.model :as model])
  (:require [keypad.const :as const]))

(defn plate-placeholder-offset
  "2D: Creates offset for centered n keys-width."
  [n-keys item]
  (let [width (* n-keys const/PLATE_SQUARE_SIZE)
        height const/PLATE_SQUARE_SIZE]
    (model/translate [(/ width 2) (/ height 2)] item)))

(defn plate-placeholder
  "2D: Creates a plate square."
  [n-keys]
  (let [width (* n-keys const/PLATE_SQUARE_SIZE)
        height const/PLATE_SQUARE_SIZE]
    (->> (model/square width height)
         (plate-placeholder-offset n-keys))))

(defn switch
  "2D: Creates a switch cutout."
  [n-keys]
  (->> (model/square const/SWITCH_SQUARE_SIZE const/SWITCH_SQUARE_SIZE)
       (plate-placeholder-offset n-keys)))

(defn generate-cluster
  "2D: Generate keys based on cluster. If cutout?, generates switches;
  else generates plate placeholder."
  [cluster cutout?]
  (let [f (if cutout? switch plate-placeholder)]
    (model/union
      (for [[i row] (map-indexed vector cluster)]
        (for [[j square] (map-indexed vector row)]
          (->> (f square)
               (model/translate [(* i const/PLATE_SQUARE_SIZE) (* j const/PLATE_SQUARE_SIZE)])))))))
