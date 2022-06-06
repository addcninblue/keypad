(ns keypad.core
  (:require [scad-clj.scad :as scad])
  (:require [scad-clj.model :as model])
  (:require [keypad.layouts :as layouts])
  (:require [keypad.lib :as lib]))

(def plate
  (model/difference
    (lib/generate-cluster layouts/keypad-layout false)
    (lib/generate-cluster layouts/keypad-layout true)))

(def parts
  {"plate.scad" plate
   "top.scad" plate})

(defn -main
  [& _args]
  (dorun
    (for [[filename component] parts]
      (spit (str "render/" filename) (scad/write-scad component)))))
