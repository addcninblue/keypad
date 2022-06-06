(ns keypad.lib
  (:require [scad-clj.model :as model])
  (:require [keypad.const :as const])
  (:require [keypad.layouts :as layouts]))

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

(defn keycaps
  "2D: Creates a keycaps cutout."
  [n-keys]
  (->> (model/square const/KEYCAP_SQUARE_SIZE const/KEYCAP_SQUARE_SIZE)
       (plate-placeholder-offset n-keys)))

(defmulti generate-elem
  (fn [elem _cutout-type]
    (cond (int? elem) :int
          (layouts/knob? elem) :knob
          (layouts/space? elem) :space
          :else (throw (Exception. "Invalid cutout type when generating key.")))))

(defmethod generate-elem :int [elem cutout-type]
  ((cond (= :full cutout-type) plate-placeholder
         (= :top cutout-type) plate-placeholder
         (= :plate cutout-type) switch
         (= :keycaps cutout-type) keycaps)
   elem))

(defmethod generate-elem :knob [_elem cutout-type]
  (->> (cond (= :full cutout-type) (model/square const/PLATE_SQUARE_SIZE const/PLATE_SQUARE_SIZE)
             (= :top cutout-type) (model/circle const/KY-040_RADIUS_TOP)
             (= :plate cutout-type) (model/circle const/KY-040_RADIUS_PLATE))
       (plate-placeholder-offset 1)))

(defmethod generate-elem :space [elem cutout-type]
  (cond (= :full cutout-type) (plate-placeholder (layouts/space-width elem))
        (= :top cutout-type) nil
        (= :plate cutout-type) nil))

(defn generate-cluster
  "2D: Generate keys based on cluster.
  cutout-type must be one of :full, :plate, :keycaps."
  [cluster cutout-type]
    (some->>
      (model/union
        (for [[i row] (map-indexed vector (rseq (cluster :layout)))]
          (for [[j elem] (map-indexed vector row)]
            (->> (generate-elem elem cutout-type)
                 (model/translate [(* j const/PLATE_SQUARE_SIZE) (* i const/PLATE_SQUARE_SIZE)])))))
      (model/translate [(cluster :x-offset) (cluster :y-offset)])))
