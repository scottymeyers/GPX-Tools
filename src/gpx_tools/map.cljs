(ns gpx-tools.map
  (:require
   [gpx-tools.utilities :as utilities]))

(defn lat-lng
  "Creates Google LatLng from a trkpt"
  [trkpt]
  (js/google.maps.LatLng.
   (. trkpt getAttribute "lat")
   (. trkpt getAttribute "lon")))

(defn is-selected?
  "Determines if the referenced activity is the selected one"
  [selected current]
  (if selected
    (if (identical? selected current) true false)
    false))

(defn set-map-boundary
  "Sets the Boundaries on the Map"
  [latLngs gmap]
  (let [bounds (js/google.maps.LatLngBounds.)]
    (doseq [latLng latLngs]
      (.extend bounds latLng))
    (.fitBounds gmap bounds)))

(defn polyline
  "Creates a Polyline on the Map"
  [path gmap handler]
  (let [polyline (js/google.maps.Polyline.
                  (clj->js {:path path
                            :geodesic true
                            :strokeColor "blue"
                            :strokeWeight 6
                            :map gmap}))]
    (.addListener
     js/google.maps.event
     polyline
     "click"
     handler)
    nil))

(defn activity [activity gmap on-select]
  (let [path (map lat-lng (utilities/get-activity-trkpts activity))
        handler #(on-select activity)]
    ;; TODO: Extend boundary based on all activities
    (set-map-boundary path gmap)
    [polyline path gmap handler]))

(defn marker
  "Creates a Marker on the Map"
  [activity gmap]
  (let [position (lat-lng (utilities/get-activity-trkpts activity))
        _ (js/google.maps.Marker. (clj->js {:position position
                                            :map gmap}))]
    nil))
