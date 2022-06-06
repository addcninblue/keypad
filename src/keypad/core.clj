(ns keypad.core
  (:require [scad-clj.scad :as scad])
  (:require [scad-clj.model :as model])
  (:require [keypad.layouts :as layouts])
  (:require [keypad.lib :as lib])
  (:require [keypad.const :as const]))

; TODO: Figure out stem height of rotary knob. If > 5mm, we're good
(def top
  (model/extrude-linear
    {:height const/PLATE_HEIGHT
     :center false}
    (model/difference
      (lib/add-border
        (lib/generate-cluster layouts/keypad-layout :full))
      (lib/generate-cluster layouts/keypad-layout :top))))

(def plate
  (model/extrude-linear
    {:height const/PLATE_HEIGHT
     :center false}
    (model/difference
      (lib/add-border
        (lib/generate-cluster layouts/keypad-layout :full))
      (lib/generate-cluster layouts/keypad-layout :plate))))

(def wiring
  (model/extrude-linear
    {:height const/PLATE_HEIGHT
     :center false}
    (model/difference
      (lib/add-border
        (lib/generate-cluster layouts/keypad-layout :full))
      (lib/generate-cluster layouts/keypad-layout :wiring))))

(def bottom
  (model/extrude-linear
    {:height const/BASE_HEIGHT
     :center false}
    (lib/add-border
      (lib/generate-cluster layouts/keypad-layout :full))))

(def parts
  {"top.scad" top
   "plate.scad" plate
   "wiring.scad" wiring
   "bottom.scad" bottom
   })

(defn -main
  [& _args]
  (dorun
    (for [[filename component] parts]
      (spit (str "scad/" filename) (scad/write-scad component)))))
