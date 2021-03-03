(ns gpx-tools.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [gpx-tools.components :as component]
   [gpx-tools.map :as maptools]
   [gpx-tools.utilities :as utilities]))

(enable-console-print!)

(defonce app-state (r/atom {:error nil
                            :activities []}))
(defonce gmap (r/atom nil))

(defn setup-google-maps []
  (let [api-key (subs (-> js/document .-location .-search) 1)
        center (clj->js {"lat" 40.730610
                         "lng" -73.935242})
        loader (google.maps.plugins.loader.Loader.
                (clj->js {"apiKey" api-key
                          "version" "weekly"}))]

    (.addEventListener
     js/window
     "DOMContentLoaded"
     (-> (.load loader)
         (.then (fn []
                  (set! gmap (google.maps.Map.
                              (. js/document (getElementById "map"))
                              (js-obj
                               "center" center
                               "zoom" 8)))))
         (.catch #(utilities/set-error
                   "Unable to load Google Maps"))))))

(.addEventListener js/window "load" setup-google-maps)

(defn app []
  [:div
   (component/file-uploader "gpx-files" #(swap! app-state assoc :activities %))
   (component/error-message (:error @app-state))
   (for [activity (:activities @app-state)]
     (maptools/polyline activity gmap))])

(rdom/render [app]
             (. js/document (getElementById "app")))

(defn on-js-reload [])