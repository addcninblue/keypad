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

(def keypad-layout {:layout [[(knob), (space 1), (knob)]
                             [1, 1, 1],
                             [1, 1, 1],
                             [1, 1, 1]]
                    :x-offset 0
                    :y-offset 0})
