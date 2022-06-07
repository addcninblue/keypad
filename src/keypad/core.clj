(ns keypad.core
  (:require [scad-clj.scad :as scad])
  (:require [scad-clj.model :as model])
  (:require [keypad.layouts :as layouts])
  (:require [keypad.lib :as lib])
  (:require [keypad.const :as const]))

(def cutout-x-offset (/ (layouts/max-width layouts/keypad-layout) 2))
(def cutout-y-offset (+ (/ const/BORDER_RADIUS 2) (layouts/max-height layouts/keypad-layout)))

(defn generate-layer
  [cutout-type]
  (model/extrude-linear
    {:height const/PLATE_HEIGHT
     :center false}
    (model/difference
      (lib/add-border
        (lib/generate-cluster layouts/keypad-layout :full))
      (lib/generate-cluster layouts/keypad-layout cutout-type)
      (model/translate [cutout-x-offset cutout-y-offset]
                       (lib/generate-cutout const/CUTOUT_WIDTH const/BORDER_RADIUS cutout-type)))))

; TODO: Figure out stem height of rotary knob. If > 5mm, we're good
(def top (generate-layer :top))
(def plate (generate-layer :plate))
(def wiring (generate-layer :wiring))

(def bottom
  (model/extrude-linear
    {:height const/BASE_HEIGHT
     :center false}
    (lib/add-border
      (lib/generate-cluster layouts/keypad-layout :full))))

(def full
  (model/union
    (model/translate [0 0 (+ const/BASE_HEIGHT const/PLATE_HEIGHT const/PLATE_HEIGHT)] top)
    (model/translate [0 0 (+ const/BASE_HEIGHT const/PLATE_HEIGHT)] plate)
    (model/translate [0 0 (+ const/BASE_HEIGHT)] wiring)
    bottom))

(def parts
  {"top.scad" top
   "plate.scad" plate
   "wiring.scad" wiring
   "bottom.scad" bottom
   "full.scad" full
   })

(defn -main
  [& _args]
  (dorun
    (for [[filename component] parts]
      (spit (str "scad/" filename) (scad/write-scad component)))))
