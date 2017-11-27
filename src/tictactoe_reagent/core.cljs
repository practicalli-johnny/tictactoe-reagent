(ns tictactoe-reagent.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)
;; (devtools.core/set-pref! :dont-detect-custom-formatters true)

;; Testing output to the web developer console in the browser
(println "This text is printed from src/tictactoe-reagent/core.cljs. Go ahead and edit it and see reloading in action.")


;; Hard coded game board size
;; As the game board is square, we only use one value for height and width
(def board-dimension 3)

(defn game-board
  "Create a data structure to represent the values of cells in the game board.
  A vector is used to hold the overall game board
  Each nested vector represents a line of the game board.
  Dimension specifies the size of the game board."
  [dimension]
  (vec (repeat dimension (vec (repeat dimension :empty)))))


;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Lets Play TicTacToe"
                      :board (game-board board-dimension)}))

(defn cell-empty
  "Generate a cell that has not yet been clicked on"
  [x-cell y-cell]
  ^{:key (str x-cell y-cell)}
  [:rect {:width 0.9
          :height 0.9
          :fill "grey"
          :x x-cell
          :y y-cell
          :on-click
          (fn rectangle-click [e]
            (println "Cell" x-cell y-cell "was clicked!")
            (println
             (swap! app-state assoc-in [:board y-cell x-cell] :cross)))}])


(defn cell-nought
  "A cell with a nought inside it"
  [x-cell y-cell]
  ^{:key (str x-cell y-cell)}
  [:circle {:r 0.36
            :fill "white"
            :stroke "green"
            :stroke-width 0.1
            :cx (+ 0.42 x-cell)
            :cy (+ 0.42 y-cell)}])


(defn cell-cross
  "A cell with a cross inside it"
[x-cell y-cell]
  ^{:key (str x-cell y-cell)}
  [:g {:stroke "purple"
       :stroke-width 0.4
       :stroke-linecap "round"
       :transform
       (str "translate(" (+ 0.42 x-cell) "," (+ 0.42 y-cell) ") "
            "scale(0.3)")}
   [:line {:x1 -1 :y1 -1 :x2 1 :y2 1}]
   [:line {:x1 1 :y1 -1 :x2 -1 :y2 1}]])


(defn tictactoe-game []
  [:div
   [:div
    [:h1 (:text @app-state)]
    [:p "Do you want to play a game?"]]
   [:button {:on-click (fn new-game-click [e]
                           (swap! app-state assoc :board (game-board board-dimension)))}
    "Start a new game"]
   [:center
    [:svg {:view-box "0 0 3 3"
           :width 500
           :height 500}
     (for [x-cell (range (count (:board @app-state)))
           y-cell (range (count (:board @app-state)))]
       (case (get-in @app-state [:board y-cell x-cell])
         :empty [cell-empty x-cell y-cell]
         :cross [cell-cross x-cell y-cell]
         :nought [cell-nought x-cell y-cell]))]]])


(reagent/render-component [tictactoe-game]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; REPL Experiments

;;;;;;;;;;;;;;;;;;;;;;;;
;; Generating a data structure to represet the game board

;; We could just hard code the board as follows, although that limits us to a specific size of board:
#_[[:empty :empty :empty]
   [:empty :empty :empty]
   [:empty :empty :empty]]

;; To create a row is simple to do using the repeat function to generate 3 :empty keywords and return them as a list
#_(repeat 3 :empty)
;; => (:empty :empty :empty)

;; To make this a vector we can just wrap that in a vec function
#_(vec (repeat 3 :empty))
;; => [:empty :empty :empty]

;; To create three rows we just repeat the code above 3 times

#_(vec (repeat 3 (vec (repeat 3 :empty))))
;; => [[:empty :empty :empty] [:empty :empty :empty] [:empty :empty :empty]]

;; we can use the above code in a function and replace 3 with a local name that takes the value of the argument passed in
;; so lets write a game-board function.

#_(println (game-board 3))


;;;;;;;;;;;;;;;;;;;;;;;;
;; Iterate over board data structure

;; Retrieve the app state by defererencing the name app-state, (dref app-state) or @app-state
#_@app-state

#_(count (:board @app-state))
#_(range 3)


;;;;;;;;;;;;;;;;;;;;;;;;
;; Redering shapes with SVG

#_[:svg
   :circle {:r 30}]


;; Warning in browser
;; Every element in a sequence should have a unique key


#_([:rect {:width 0.9, :height 0.9, :fill "purple", :x 0, :y 0}]
 [:rect {:width 0.9, :height 0.9, :fill "purple", :x 0, :y 1}]
 [:rect {:width 0.9, :height 0.9, :fill "purple", :x 0, :y 2}]
 [:rect {:width 0.9, :height 0.9, :fill "purple", :x 1, :y 0}]
 [:rect {:width 0.9, :height 0.9, :fill "purple", :x 1, :y 1}]
 [:rect {:width 0.9, :height 0.9, :fill "purple", :x 1, :y 2}]
 [:rect {:width 0.9, :height 0.9, :fill "purple", :x 2, :y 0}]
 [:rect {:width 0.9, :height 0.9, :fill "purple", :x 2, :y 1}]
 [:rect {:width 0.9, :height 0.9, :fill "purple", :x 2, :y 2}])

;; To fix this issue, add a piece of metadata to each rectangle definition
;; ^{:key (str x-cell y-cell)}      ; generate a unique metadata :key for each rectangle, ie. 00, 01, 02, etc
