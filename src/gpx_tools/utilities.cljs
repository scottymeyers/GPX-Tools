(ns gpx-tools.utilities)

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

(defn friendly-date
  "Returns the date string for a Date"
  [date] (.toDateString (js/Date. date)))
(defn friendly-time
  "Returns the time string for a Date"
  [date] (.toTimeString (js/Date. date)))
