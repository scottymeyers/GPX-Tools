(ns cycling.utilities
  (:require [goog.string :as gstring]
            [cycling.core :as core :refer [app-state]]))

(defn dom-parse
  "Extracts the data from GPX file"
  [xml]
  (-> (js/DOMParser.)
      (.parseFromString xml "text/xml")
      (.-firstChild)))

(defn set-error
  "Sets error in @app-state"
  [error]
  (reset! app-state {:error error}))

(defn get-trkpts
  "Extracts the trkpts from a GPX file"
  [gpx]
  (let [trkseg (. gpx getElementsByTagName "trkseg")
        trkpts (. (first trkseg) getElementsByTagName "trkpt")]
    trkpts))

(defn extract-gpx-from-files
  "Parses the GPX files & returns a Promise"
  [files handler]
  (-> (js/Promise.all (map (fn [file]
                             (-> (.then (.text file))
                                 (.then #(dom-parse %))
                                 (.catch #(set-error "Cannot proccess file"))))
                           files))
      ;; TODO: figure out best way to store gpx in @app-state
      (.then #(handler %))))

(defn handle-file-input
  "Extracts files from the File Input event"
  [e handler]
  (.preventDefault e)
  (let [files (-> e .-nativeEvent .-target .-files)]
    (extract-gpx-from-files files handler)))

(defn handle-file-drop
  "Extracts files from the File Drop event"
  [e handler]
  (.stopPropagation e)
  (.preventDefault e)
  ;; TODO: filter out non GPX files
  ;; (if (or (= (.-type file) "gpx") (gstring/endsWith (.-name file) ".gpx")))
  ;; (set-error "File format not supported")
  (let [files (.-files (.-dataTransfer e))]
    (extract-gpx-from-files files handler)))
