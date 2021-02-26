(ns cycling.core
  (:require
   [reagent.dom :as rdom]
   [cycling.components :as component]))

(enable-console-print!)

(def ^:dynamic *gmap* nil)

;; currently we supply google maps API key in url
;; TODO: eventually ask for this once and store in localStorage
(defn setup-google-maps []
  (let [api-key (subs (-> js/document .-location .-search) 1)
        center (clj->js {"lat" 40.730610
                         "lng" -73.935242})
        loader (google.maps.plugins.loader.Loader. (clj->js {"apiKey" api-key
                                                             "version" "weekly"}))]

    (.addEventListener
     js/window
     "DOMContentLoaded"
     (-> (.load loader)
         (.then (fn []
                  (set! *gmap* (google.maps.Map.
                                (. js/document (getElementById "map"))
                                (js-obj
                                 "center" center
                                 "zoom" 8)))))
         (.catch (fn [e] (js/console.log e)))))))

(.addEventListener js/window "load" setup-google-maps)

(defn app []
  [:div
   [:h1 "GPX Route Visualizer"]
   (component/file-drop "gpx-files")])

(rdom/render [app]
             (. js/document (getElementById "app")))

(defn on-js-reload [])