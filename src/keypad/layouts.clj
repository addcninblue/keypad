(ns keypad.layouts)

(def keypad-layout [[1, 1, 1],
                    [1, 1, 1],
                    [1, 1, 1]])

(defn space
  [width]
  [:space, width])

(defn space?
  [space]
  (and (vector? space) (= (first space) :space)))

(defn space-width
  [space]
  (assert space? space)
  (second space))

(defn knob [] :knob)

(defn knob? [elem] (= :knob elem))
