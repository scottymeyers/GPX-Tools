(ns cycling.core
  (:require
   [reagent.dom :as rdom]))

(enable-console-print!)

(def ^:dynamic *gmap* nil)
(defn map-load []
  (let [apiKey (subs (-> js/document .-location .-search) 1)
        center (clj->js {"lat" 40.730610
                         "lng" -73.935242})
        loader (google.maps.plugins.loader.Loader. (js-obj
                                                    "apiKey" apiKey
                                                    "version" "weekly"))]

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

(.addEventListener
 js/window
 "load"
 map-load)

(defn app []
  [:div "Cycling Data"])

(rdom/render [app]
             (. js/document (getElementById "app")))

(defn on-js-reload [])