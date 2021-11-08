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

(defn select-activity
  "Sets the Selected Activity"
  [activity]
  (if (and (:selected-activity @app-state)
           (util/is-selected? (:selected-activity @app-state) activity))
    (swap! app-state assoc :selected-activity nil)
    (do
      (swap! app-state assoc :selected-activity activity)
      (let [path (map util/lat-lng (util/get-activity-trkpts activity))]
        (maptools/set-map-boundary path (:gmap @app-state))))))

(defn store-activities
  "Adds uploaded Activities to the Store"
  [activities]
  (.then activities (fn [a]
                      (apply swap! app-state update-in [:activities] conj a))))

(defn setup-google-maps
  "Load Google Maps w/ API Key in URL"
  []
  (let [api-key (subs (-> js/document .-location .-search) 1)
        center (clj->js {:lat 40.730610
                         :lng -73.935242})
        loader (google.maps.plugins.loader.Loader. (clj->js {:apiKey api-key
                                                             :version "weekly"}))]

    (.addEventListener
     js/window
     "DOMContentLoaded"
     (-> (.load loader)
         (.then (fn []
                  (swap! app-state assoc :gmap (google.maps.Map.
                                                  (. js/document (getElementById "map"))
                                                  (clj->js
                                                   {:center center
                                                    :clickableIcons false
                                                    :disableDoubleClickZoom true
                                                    :fullscreenControl false
                                                    :zoom 8})))))
         (.catch #(set-error "Unable to load Google Maps"))))))

(.addEventListener
 js/window
 "load"
 (setup-google-maps))


(defn app []
  (fn []
    [:div
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
                (:gmap @app-state)
                (util/is-selected? (:selected-activity @app-state) activity)
                ]))]]))

(rdom/render [app]
             (. js/document (getElementById "app")))
