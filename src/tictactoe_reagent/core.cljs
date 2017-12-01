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

(defn computer-move
  "Takes a turn for the computer, adding an X shape to the board"
  []
  (swap! app-state assoc-in [:board 0 0] :cross))


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
             (swap! app-state assoc-in [:board y-cell x-cell] :nought))
            (computer-move))}])


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


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Testing the Game Board

;; As we drive the board by changes in the app-state, we can test by simply updating the app-state directly

;; To stop the REPL from running all these app-state updates, we comment them out with the reader macro #_ as it will ignore the next expression (so we dont need to comment out each line)

;; Nought winner - center column
#_(swap! app-state assoc :board
         [[:cross :nought :empty]
          [:empty :nought :empty]
          [:cross :nought :empty]])

#_(swap! app-state assoc :board
         [[:cross :nought :nought]
          [:empty :cross :empty]
          [:cross :nought :cross]])

;; Reset board by setting all cell values back to :empty
#_(reset! app-state {:text "Lets Play TicTacToe"
                   :board (game-board board-dimension)})

(defn set-game-board! [game-board-state]
  (swap! app-state assoc :board game-board-state))

;; A reset function is a nice helper function for development
;; To reset the game board simply call this function any time
(defn reset-game-board!
  "Resets the app-state to an empty game board"
  []
  (reset! app-state {:text "Lets Play TicTacToe"
                     :board (game-board board-dimension)}))

;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Determine winner by pattern matching

;; As there are only 8 winning combinations, we could just create a pattern for each win
;; As the patterns are the same for :nought and :cross we could just compare that each value is equal

;; For just a row we can compare the values in the vector with each other

#_(apply = [:empty :empty :empty])

#_(apply = [:cross :cross :cross])

#_(apply = [:nought :nought :nought])
#_(apply = [:empty :cross :empty])
;; => false

#_(apply = [:cross :nought :empty])

;; However, this approach also matches an :empty row or column

;; Using an anonymous function over the collection allows us to compare each value,
;; if all values are not= :empty then we can return true
#_(apply  (fn [cell-value] (not= :empty cell-value))[:empty :cross :cross])

;; As we will probably call this multiple times, lets convert it into a named function.
(defn cell-empty?
  [cell-value] (not= :empty cell-value))

;; Hmm, still not idea, as any combination that does not contain :empty will return true
#_(cell-empty? [[:cross :nought :cross]])

;; Applying both checks will give the right results

(defn winning-line? [cell-row]
  (and
   (apply = cell-row)
   (apply cell-empty? cell-row)))

#_(winning-line? [:cross :cross :cross])
;; => true

#_(winning-line? [:nought :nought :nought])
;; => true

#_(winning-line? [:cross :nought :cross])
;; => false

#_(winning-line? [:empty :empty :empty])
;; => false

#_(winning-line? [:empty :nought :cross])
;; => false


;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; Pattern matching diagonals

#_(= [:cross :cross :cross] [:cross :cross :cross])

(def board-of-crosses   [[:cross :cross :cross]
                         [:cross :cross :cross]
                         [:cross :cross :cross]])

(def board-of-stalemate [[:cross :cross :nought]
                         [:nought :nought :cross]
                         [:cross :cross :nought]])

#_(=  [[:cross :cross :cross] [:cross :cross :cross] [:cross :cross :cross]]
    [[:cross :cross :cross] [:cross :cross :cross] [:cross :cross :cross]])
;; => true

#_(=  [[:cross :cross :cross] [:cross :cross :cross] [:cross :cross :cross]]
    [[:cross :cross :cross] [:cross :cross :cross] [:cross :cross :nought]])

#_(=
    [[:cross :cross :cross] [:cross :cross :cross] [:cross :cross :cross]]
    [[:cross _ _] [_ :cross _] [_ _ :cross]])


;; Using destructuring we can just look at the values we are interested in
;; The let destructuring pattern pulls out the values for the diagonal line,
;; from top left to bottom right
(let [[[cell-00 _ _][_ cell-11 _][_ _ cell-22]] board-of-crosses]
  (= cell-00 cell-11 cell-22))
;; => true

(let [[[cell-00 _ _][_ cell-11 _][_ _ cell-22]] board-of-stalemate]
  (println cell-00 ":" cell-11 ":" cell-22)
  (= cell-00 cell-11 cell-22))
;; => false

;; Not sure naming these patterns with a def is that useful
;; Not this way, as its not correct clojure (names are not defined)
;; Should create two functions instead
#_(def diagonal-row--top-left-to-bottom-right
  [[cell-00 _ _]
   [_ cell-11 _]
   [_ _ cell-22]])

#_(def diagonal-row--top-right-to-bottom-left
  [[_ _ cell-02]
   [_ cell-11 _]
   [cell-20_ _]])
