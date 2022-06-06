(ns keypad.core
  (:require [scad-clj.scad :as scad])
  (:require [scad-clj.model :as model])
  (:require [keypad.layouts :as layouts])
  (:require [keypad.lib :as lib]))

(def plate
  (model/difference
    (lib/add-border
      (lib/generate-cluster layouts/keypad-layout :full))
    (lib/generate-cluster layouts/keypad-layout :plate)))

(def top
  (model/difference
    (lib/add-border
      (lib/generate-cluster layouts/keypad-layout :full))
    (lib/generate-cluster layouts/keypad-layout :top)))

(def parts
  {"top.scad" top
   "plate.scad" plate})

(defn -main
  [& _args]
  (dorun
    (for [[filename component] parts]
      (spit (str "render/" filename) (scad/write-scad component)))))
