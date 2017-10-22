(ns tictactoe-reagent.core
    (:require [reagent.core :as reagent :refer [atom]]))

(enable-console-print!)

(println "This text is printed from src/tictactoe-reagent/core.cljs. Go ahead and edit it and see reloading in action.")

;; define your app data so that it doesn't get over-written on reload

(defonce app-state (atom {:text "Lets Play TicTacToe"}))



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
