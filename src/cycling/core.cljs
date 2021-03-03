(ns cycling.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as rdom]
   [cycling.components :as component]
   [cycling.utilities :as utilities]))

(enable-console-print!)

(defonce app-state (r/atom {:error nil
                            :rides []}))
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
  ;;  TODO: add handler for updating @app-state :rides
   (component/file-uploader "gpx-files" #(js/console.log %))
   (component/error-message (:error @app-state))])

(rdom/render [app]
             (. js/document (getElementById "app")))

(defn on-js-reload [])