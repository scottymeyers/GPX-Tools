(ns gpx-tools.map
  (:require
   [reagent.core :as r]
   [gpx-tools.utilities :as util]))

(defn set-map-boundary
  "Sets the Boundary on the Map"
  [latLngs gmap]
  (let [bounds (js/google.maps.LatLngBounds.)]
    (doseq [latLng latLngs]
      (.extend bounds latLng))
    (.fitBounds gmap bounds)))

(defn polyline
  "Creates a Polyline on the Map"
  [path gmap]
  (let [ref (r/atom nil)]
    (fn [_ _ is-selected]
      ; if the polyline hasn't been created, create it
      (when (nil? @ref)
        (reset! ref (js/google.maps.Polyline.
           (clj->js {:path path
                     :geodesic true
                     :strokeColor "black"
                     :strokeWeight 6
                     :map gmap})))
      )
      ; if the polyline has been created and toggled
      ; set the appropriate strokeColor
      (when (some? @ref)
        (if (and is-selected (some? @ref))
          (.setOptions @ref (clj->js {:strokeColor "blue"}))
          (.setOptions @ref (clj->js {:strokeColor "black"})))
        )
      nil
      )))

(defn marker
  "Creates a Marker on the Map"
  [lat-lng gmap]
  (let [position lat-lng
        _ (js/google.maps.Marker. (clj->js {:position position
                                            :map gmap}))]
    nil))

(defn activity
  "Responsible for rendering an Activity on the Map"
  [activity gmap selected]
  (let [path (util/get-activity-points activity)]
    (marker (first path) gmap)
    (marker (last path) gmap)
    [polyline path gmap selected]))
