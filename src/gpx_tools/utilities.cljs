(ns gpx-tools.utilities)

(defn lat-lng
  "Creates Google LatLng from a trkpt"
  [trkpt]
  (js/google.maps.LatLng.
   (. trkpt getAttribute "lat")
   (. trkpt getAttribute "lon")))

(defn get-activity-name
  "Extracts the name from the supplied GPX"
  [gpx]
  (let [trk (. gpx getElementsByTagName "trk")
        name (.-innerHTML (first (. (first trk) getElementsByTagName "name")))]
    name))

(defn get-activity-time
  "Extracts the time from the supplied GPX"
  [gpx]
  (let [metadata (. gpx getElementsByTagName "metadata")
        time (.-innerHTML (first (. (first metadata) getElementsByTagName "time")))]
    time))

(defn get-activity-trkpts
  "Extracts the trkpts from the supplied GPX"
  [gpx]
  (let [trkseg (. gpx getElementsByTagName "trkseg")
        trkpts (. (first trkseg) getElementsByTagName "trkpt")]
    trkpts))

(defn get-activity-points
  "Extracts the LatLng Points from an Activity"
  [activity]
  (map lat-lng (get-activity-trkpts activity)))

(defn friendly-date
  "Returns the date string for a Date"
  [date] (.toDateString (js/Date. date)))

(defn friendly-time
  "Returns the time string for a Date"
  [date] (.toTimeString (js/Date. date)))

(defn is-selected?
  "Determines if the referenced activity is the selected one"
  [selected current]
  (if selected
    (if (identical? selected current) true false)
    false))
