(ns gpx-tools.utilities
  (:require
   [goog.string :as gstring]))

(defn dom-parse
  "Extracts data from a GPX file"
  [xml]
  (-> (js/DOMParser.)
      (.parseFromString xml "text/xml")
      (.-firstChild)))

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

(defn extract-gpx-from-files
  "Parses the GPX files & returns the parsed GPX"
  [files]
  (-> (js/Promise.all (map (fn [file]
                             (-> (.then (.text file))
                                 (.then #(dom-parse %))
                                 (.catch #(js/console.log "Cannot proccess file"))))
                           files))))

(defn get-input-files
  "Extracts all files from the File Input event"
  [e]
  (.preventDefault e)
  (let [files (-> e .-nativeEvent .-target .-files)]
    files))

(defn get-drop-files
  "Extracts all files from the File Drop event"
  [e]
  (.stopPropagation e)
  (.preventDefault e)
  (let [files (.-files (.-dataTransfer e))]
    files))

(defn is-gpx-file?
  "Confirms whether or not the file type is GPX"
  [file]
  (or (= (.-type file) "gpx")
      (gstring/endsWith (.-name file) ".gpx")))