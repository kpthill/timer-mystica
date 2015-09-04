(ns timer-mystica.components
  (:require
    [reagent.core :as r]
    [clojure.string :as str]))

;; Utilities

(defn class-string [classes]
  (->> classes (map name) (str/join " ")))

;; Faction info

(def faction-color-class
  {:auren           :green
   :witches         :green
   :alchemists      :black
   :darklings       :black
   :halflings       :brown
   :cultists        :brown
   :engineers       :gray
   :dwarves         :gray
   :mermaids        :blue
   :swarmlings      :blue
   :chaos-magicians :red
   :giants          :red
   :fakirs          :yellow
   :nomads          :yellow
   :ice-maidens     :white
   :yetis           :white
   :dragonlords     :orange
   :acolytes        :orange
   :changelings     :variable
   :riverwalkers    :variable})

(defn faction-text-color [faction]
  (if (= (faction-color-class faction) :black)
    :text-light
    :text-dark))

(defn faction-name [faction]
  (->>
    (-> faction name (str/split "-"))
    (map str/capitalize)
    (str/join " ")))

;; Meta buttons

(defn undo-button [{:keys [history history-index]} on-undo]
  (let [disabled-class (if (pos? history-index) nil :disabled)]
    [:button.undo-button.btn.btn-default {:class disabled-class :on-click on-undo}
     [:span.glyphicon.glyphicon-step-backward]]))

(defn redo-button [{:keys [history history-index]} on-redo]
  (let [disabled-class (if (< history-index (dec (count history)))
                         nil
                         :disabled)]
    [:button.redo-button.btn.btn-default {:class disabled-class :on-click on-redo}
     [:span.glyphicon.glyphicon-step-forward]]))

(defn pause-button [{:keys [paused?]} {:keys [on-pause on-resume]}]
  (let [action (if paused? on-resume on-pause)
        glyphicon (if paused? :glyphicon-play :glyphicon-pause)]
    [:button.pause-button.btn.btn-default {:on-click action}
     [:span.glyphicon {:class glyphicon}]]))

(defn meta-button-area [state {:keys [on-undo on-redo] :as actions}]
  [:div.meta-button-area
   [undo-button state on-undo]
   [redo-button state on-redo]
   [pause-button state actions]])

;; Clock

(defn format-time [ms]
  (let [total-seconds (quot ms 1000)
        minutes (quot total-seconds 60)
        seconds (rem total-seconds 60)
        seconds-str (if (< seconds 10) (str "0" seconds) (str seconds))]
    (str minutes ":" seconds-str)))

(defn main-clock [time-used-ms]
  [:div.clock-area
   [:p.clock (format-time time-used-ms)]])

;; Buttons

(defn start-round-button [round on-start-round]
  (let [text (str "Start Round " round)]
    [:button.start-round-button.btn.btn-primary.btn-lg {:on-click on-start-round} text]))

(defn game-over-button []
  [:button.game-over-button.btn.btn-primary.btn-lg.disabled "Game Over"])

(defn pass-button [on-pass]
  [:button.pass-button.btn.btn-default.btn-lg {:on-click on-pass} "Pass"])

(defn last-player-next-button []
  [:button.next-button.btn.btn-primary.btn-lg.disabled "Last Player"])

(defn next-button [on-next]
  [:button.next-button.btn.btn-primary.btn-lg {:on-click on-next} "Next Player"])

(defn button-area [{:keys [between-rounds? round active-players]}
                   {:keys [on-start-round on-pass on-next]}]
  [:div.button-area
   (cond
     (> round 6) [game-over-button]
     between-rounds? [start-round-button round on-start-round]
     :else (list ^{:key :pass-button} [pass-button on-pass]
                 (if (seq active-players)
                   ^{:key :next-button} [next-button on-next]
                   ^{:key :last-player-next-button} [last-player-next-button])))])

;; Current player area

(defn current-player-area [{{:keys [faction time-used-ms]} :current-player
                            :as                            state}
                           actions]
  [:div.current-player-area {:class (faction-text-color faction)}
   [:p.active-faction-label (faction-name faction)]
   [main-clock time-used-ms]
   [button-area state actions]])

;; Active player area

(defn player-list-item [{:keys [faction time-used-ms]}]
  [:div.player-item {:class (class-string [(faction-color-class faction)
                                           (faction-text-color faction)])}
   [:p.faction-label (faction-name faction)]
   [:p.timer (format-time time-used-ms)]])

(defn active-players-area [active-players]
  (when (seq active-players)
    [:div.active-players-area
     [:p.player-list-label "Next:"]
     (for [player active-players]
       ^{:key (:faction player)} [player-list-item player])]))

;; Passed player area

(defn passed-players-area [passed-players]
  (when (seq passed-players)
    [:div.passed-players-area
     [:p.player-list-label "Passed:"]
     (for [player passed-players]
       ^{:key (:faction player)} [player-list-item player])]))

;; Main component

(defn main [app-state actions]
  (let [{:keys [current-player active-players passed-players]
         :as   state} @app-state
        current-faction (:faction current-player)]
    [:div.timer-mystica {:class (class-string [(faction-color-class current-faction)
                                               (faction-text-color current-faction)])}
     [meta-button-area state actions]
     [current-player-area state actions]
     [active-players-area active-players]
     [passed-players-area passed-players]]))
