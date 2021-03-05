(ns gpx-tools.files
  (:require
   [goog.string :as gstring]))

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

(defn dom-parse
  "Extracts data from a GPX file"
  [xml]
  (-> (js/DOMParser.)
      (.parseFromString xml "text/xml")
      (.-firstChild)))

(defn is-gpx-file?
  "Confirms whether or not the file type is GPX"
  [file]
  (or (= (.-type file) "gpx")
      (gstring/endsWith (.-name file) ".gpx")))

(defn extract-gpx-from-files
  "Returns a Promise containing all the parsed GPX files"
  [files]
  (-> (js/Promise.all (map (fn [file]
                             (-> (.then (.text file))
                                 (.then #(dom-parse %))
                                 (.catch #(js/console.log "Error: Cannot proccess file"))))
                           files))))