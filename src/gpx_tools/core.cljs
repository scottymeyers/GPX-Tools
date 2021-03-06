(ns gpx-tools.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [gpx-tools.components :as component]
   [gpx-tools.map :as maptools]
   [gpx-tools.utilities :as util]))

(enable-console-print!)

(defonce app-state (r/atom {:error nil
                            :gmap nil
                            :activities []
                            :selected-activity nil}))

(defn set-error
  "Stores Errors that should be displayed"
  [error]
  (reset! app-state {:error error}))

(defn select-activity [activity]
  (if (and (:selected-activity @app-state)
           (identical?
            (util/get-activity-time (:selected-activity @app-state))
            (util/get-activity-time activity)))
    (swap! app-state assoc :selected-activity nil)
    (do
      (swap! app-state assoc :selected-activity activity)
      (let [path (map maptools/lat-lng (util/get-activity-trkpts activity))]
        (maptools/set-map-boundary path (:gmap @app-state))))))

(defn store-activities
  [activities]
  (.then activities #(swap! app-state assoc-in [:activities] %)))

(defn app []
  (fn []
    [:div
     [component/google-map app-state set-error]
     [:div {:id "content"}
      [component/error-message (:error @app-state) set-error]
      [component/file-importer "gpx" store-activities]
      [component/activities-list
       (:activities @app-state)
       (:selected-activity @app-state)
       select-activity]
      (doall (for [activity (:activities @app-state)]
               ^{:key (util/get-activity-time activity)}
               [maptools/activity
                activity
                (:gmap @app-state)]))]]))

(rdom/render [app]
             (. js/document (getElementById "app")))

;; TODO: adjust boundaries based on all activities
;; (maptools/set-map-boundary position)