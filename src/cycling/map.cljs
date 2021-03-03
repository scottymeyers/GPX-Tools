(ns cycling.map
  (:require
   [cycling.utilities :as utilities]))

(defn convert-trkpt-to-lat-lng
  "Extracts Latitude & Longitude trkpt attributes"
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
  "Draws a Polyline on the Map"
  [activity gmap]
  (let [path (map convert-trkpt-to-lat-lng (utilities/get-activity-trkpts activity))
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
  "Draws a Marker on the Map"
  [activity gmap]
  (let [position (convert-trkpt-to-lat-lng (utilities/get-activity-trkpts activity))]
    (js/google.maps.Marker. (clj->js {:position position
                                      :map gmap}))))

;; (set-map-boundary position)