(ns gpx-tools.map
  (:require
   [gpx-tools.utilities :as util]))

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

(defn marker
  "Creates a Marker on the Map"
  [lat-lng gmap]
  (let [position lat-lng
        _ (js/google.maps.Marker. (clj->js {:position position
                                            :map gmap}))]
    nil))

(defn activity [activity gmap]
  (let [path (util/get-activity-points activity)]
    (set-map-boundary path gmap)
    (marker (first path) gmap)
    (marker (last path) gmap)
    [polyline path gmap]))
