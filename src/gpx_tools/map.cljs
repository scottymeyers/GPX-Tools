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
  "Sets the Boundaries on the Map"
  [latLngs gmap]
  (let [bounds (js/google.maps.LatLngBounds.)]
    (doseq [latLng latLngs]
      (.extend bounds latLng))
    (.fitBounds gmap bounds)))

(defn polyline
  "Creates a Polyline on the Map"
  [activity gmap on-select]
  (let [path (map lat-lng (utilities/get-activity-trkpts activity))
        polyline (js/google.maps.Polyline.
                  (clj->js {:path path
                            :geodesic true
                            :strokeColor "blue"
                            :strokeWeight 4
                            :map gmap}))]
    (set-map-boundary path gmap)
    (.addListener
     js/google.maps.event
     polyline
     "click"
     #(on-select activity))
    nil))

(defn marker
  "Creates a Marker on the Map"
  [activity gmap]
  (let [position (lat-lng (utilities/get-activity-trkpts activity))
        _ (js/google.maps.Marker. (clj->js {:position position
                                            :map gmap}))]
    nil))
