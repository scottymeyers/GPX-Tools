(ns cycling.map
  (:require
   [cycling.core :as core :refer [gmap]]))

(defn convert-trkpt-to-lat-lng
  "Extracts Latitude & Longitude trkpt attributes"
  [trkpt]
  (js/google.maps.LatLng.
   (. trkpt getAttribute "lat")
   (. trkpt getAttribute "lon")))

(defn set-map-boundary
  "Sets the Boundaries"
  [latLngs]
  (let [bounds (js/google.maps.LatLngBounds.)]
    (doseq [latLng latLngs]
      (.extend bounds latLng))
    (.fitBounds gmap bounds)))

(defn draw-polyline
  "Draws a Polyline"
  [trkpts]
  (let [path (map #(convert-trkpt-to-lat-lng %) trkpts)
        polygon (js/google.maps.Polyline.
                 (clj->js {:path path
                           :geodesic true
                           :strokeColor "FF0000"
                           :strokeWeight 3
                           :map gmap}))]
    (set-map-boundary path)
    (.addListener js/google.maps.event polygon "click" #(js/console.log "selected polyline" %))))

(defn draw-marker
  "Draws a Marker"
  [trkpt]
  (let [position (convert-trkpt-to-lat-lng trkpt)]
    (js/google.maps.Marker. (clj->js {:position position
                                      :map gmap}))
    (set-map-boundary position)))