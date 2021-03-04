(ns gpx-tools.map
  (:require
   [gpx-tools.utilities :as utilities]))

(defn lat-lng
  "Creates Google LatLng from a trkpt"
  [trkpt]
  (js/google.maps.LatLng.
   (. trkpt getAttribute "lat")
   (. trkpt getAttribute "lon")))

;; TODO: Extend boundary based on all activities
(defn set-map-boundary
  "Sets the Boundaries"
  [latLngs gmap]
  (let [bounds (js/google.maps.LatLngBounds.)]
    (doseq [latLng latLngs]
      (.extend bounds latLng))
    (.fitBounds gmap bounds)))

(defn polyline
  "Draws a Polyline"
  [activity gmap]
  (let [path (map lat-lng (utilities/get-activity-trkpts activity))
        name (utilities/get-activity-name activity)
        polygon (js/google.maps.Polyline.
                 (clj->js {:path path
                           :geodesic true
                           :strokeColor "FF0000"
                           :strokeWeight 3
                           :map gmap}))]
    (set-map-boundary path gmap)
    (.addListener js/google.maps.event polygon "click" #(js/console.log "selected" name))
    nil))

(defn marker
  "Draws a Marker"
  [activity gmap]
  (let [position (lat-lng (utilities/get-activity-trkpts activity))]
    (js/google.maps.Marker. (clj->js {:position position
                                      :map gmap}))))