(ns gpx-tools.map
  (:require
   [gpx-tools.utilities :as util]))

(defn lat-lng
  "Creates Google LatLng from a trkpt"
  [trkpt]
  (js/google.maps.LatLng.
   (. trkpt getAttribute "lat")
   (. trkpt getAttribute "lon")))

(defn set-map-boundary
  "Sets the Boundaries on the Map"
  [latLngs gmap]
  (let [bounds (js/google.maps.LatLngBounds.)]
    (doseq [latLng latLngs]
      (.extend bounds latLng))
    (.fitBounds gmap bounds)))

(defn polyline
  "Creates a Polyline on the Map"
  [path gmap]
  (let [_ (js/google.maps.Polyline.
           (clj->js {:path path
                     :geodesic true
                     :strokeColor "blue"
                     :strokeWeight 6
                     :map gmap}))]
    nil))

(defn activity [activity gmap]
  (let [path (map lat-lng (util/get-activity-trkpts activity))]
    ;; TODO: Extend boundary based on all activities
    (set-map-boundary path gmap)
    [polyline path gmap]))

(defn marker
  "Creates a Marker on the Map"
  [activity gmap]
  (let [position (lat-lng (util/get-activity-trkpts activity))
        _ (js/google.maps.Marker. (clj->js {:position position
                                            :map gmap}))]
    nil))
