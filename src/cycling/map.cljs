(ns cycling.map
  (:require
   [cycling.core :as core :refer [gmap]]))

(defn parse-trkpt
  "Extracts Latitude & Longitude trkpt attributes"
  [trkpt]
  (js/google.maps.LatLng.
   (. trkpt getAttribute "lat")
   (. trkpt getAttribute "lon")))

(defn clear-map
  "Clears all shapes"
  []
  (js/console.log "Clear the Map" gmap))

(defn set-map-boundary
  "Sets the Boundaries"
  [latLngs]
  (let [bounds (js/google.maps.LatLngBounds.)]
    (doseq [latLng latLngs]
      (.extend bounds latLng))
    (.fitBounds gmap bounds)))

(defn draw-polyline
  "Creates and renders a Polyline"
  [trkpts]
  (let [path (map #(parse-trkpt %) trkpts)
        polygon (js/google.maps.Polyline.
                 (clj->js {:path path
                           :geodesic true
                           :strokeColor "FF0000"
                           :strokeWeight 3
                           :map gmap}))]
    (set-map-boundary path)
    (.addListener js/google.maps.event polygon "click" #(js/console.log "selected polyline" %))))

(defn draw-marker
  "Creates and renders a Marker"
  [trkpt]
  (let [position (parse-trkpt trkpt)]
    (js/google.maps.Marker. (clj->js {:position position
                                      :map gmap}))
    (set-map-boundary position)))