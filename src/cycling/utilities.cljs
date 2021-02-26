(ns cycling.utilities
  (:require [cycling.core :as core :refer [*gmap*]]))

(declare draw-marker draw-polyline)

(defn dom-parse
  "Extracts the data from GPX file"
  [xml]
  (-> (js/DOMParser.)
      (.parseFromString xml "text/xml")
      (.-firstChild)))

;; TODO: create error message component
(defn handle-error
  "Will store error state and display a message"
  [error]
  (js/console.log error))

(defn get-trkpts
  "Extracts the trkpts from the parsed GPX file"
  [gpx]
  (let [trkseg (. gpx getElementsByTagName "trkseg")
        trkpts (. (first trkseg) getElementsByTagName "trkpt")]
    trkpts))

(defn handle-file-input
  "Parses the File Input event"
  [e]
  (.preventDefault e)
  (let [file (first (-> e .-nativeEvent .-target .-files))]
    (-> (.text file)
        (.then #(draw-polyline (get-trkpts (dom-parse %))))
        (.catch #(handle-error "Error processing file")))))

;; TODO: only proccess GPX file, return an error for others
;; TODO: clear input value after its processed
(defn handle-file-drop
  "Parses the File Drop event"
  [e]
  (.stopPropagation e)
  (.preventDefault e)
  (let [file (first (.-files (.-dataTransfer e)))]
    (-> (.text file)
        (.then #(draw-polyline (get-trkpts (dom-parse %))))
        (.catch #(handle-error "Error processing file")))))

(defn create-lat-lng
  "Generate a Google Maps latLng object"
  [lat lng]
  (js/google.maps.LatLng. lat lng))

(defn parse-trkpt
  "Extracts Latitude and Longitude from trkpt attributes"
  [trkpt]
  (create-lat-lng (. trkpt getAttribute "lat") (. trkpt getAttribute "lon")))

;; TODO: set boundary around the polyline
(defn draw-polyline
  "Renders a Polyline from the trkpts"
  [trkpts]
  (let [path (map #(parse-trkpt %) trkpts)]
    (js/google.maps.Polyline. (clj->js {:path path
                                        :geodesic true
                                        :strokeColor "FF0000"
                                        :strokeWeight 3
                                        :map *gmap*}))))

;; TODO: set boundary around the Marker
(defn draw-marker
  "Renders a Marker for a given trkpt"
  [trkpt]
  (let [position (parse-trkpt trkpt)]
    (js/google.maps.Marker. (clj->js {:position position
                                      :map *gmap*}))))

;; TODO: Revisit when working on timing tool
;;   {:lat (. trkpt getAttribute "lat")
;;    :lng (. trkpt getAttribute "lon")
;;    :el (.-textContent (first (. trkpt getElementsByTagName "ele")))
;;    :time (.-textContent (first (. trkpt getElementsByTagName "time")))})