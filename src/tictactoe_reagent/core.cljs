(ns tictactoe-reagent.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)
;; (devtools.core/set-pref! :dont-detect-custom-formatters true)

;; Testing output to the web developer console in the browser
(println "This text is printed from src/tictactoe-reagent/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Lets Play TicTacToe"}))

(defn game-board
  "Create a data structure to represent the values of cells in the game board.
  A vector is used to hold the overall game board
  Each nested vector represents a line of the game board.
  Dimension specifies the size of the game board."
  [dimension]
  (vec (repeat dimension (vec (repeat dimension :empty)))))


(defn tictactoe-game []
  [:div
   [:h1 (:text @app-state)]
   [:p "Do you want to play a game?"]])

(reagent/render-component [tictactoe-game]
                          (. js/document (getElementById "app")))

(defn on-js-reload []
  ;; optionally touch your app-state to force rerendering depending on
  ;; your application
  ;; (swap! app-state update-in [:__figwheel_counter] inc)
)




;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;;
;; REPL Experiments

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
