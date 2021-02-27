(ns cycling.utilities
  (:require [goog.string :as gstring]
            [cycling.map :as map :refer [draw-polyline]]))

(defn dom-parse
  "Extracts the data from GPX file"
  [xml]
  (-> (js/DOMParser.)
      (.parseFromString xml "text/xml")
      (.-firstChild)))

;; TODO: create error message component
(defn handle-error
  "Will store error state and display a message"
  [error]
  (js/console.log error))

(defn get-trkpts
  "Extracts the trkpts from a parsed GPX file"
  [gpx]
  (let [trkseg (. gpx getElementsByTagName "trkseg")
        trkpts (. (first trkseg) getElementsByTagName "trkpt")]
    trkpts))

;; TODO: clear input value after its processed
(defn handle-file-input
  "Parses the File Input event"
  [e]
  (.preventDefault e)
  (let [file (first (-> e .-nativeEvent .-target .-files))]
    (-> (.text file)
        (.then #(draw-polyline (get-trkpts (dom-parse %))))
        (.catch #(handle-error "Cannot process file")))))

(defn handle-file-drop
  "Parses the File Drop event"
  [e]
  (.stopPropagation e)
  (.preventDefault e)
  (let [file (first (.-files (.-dataTransfer e)))]
    ;; .gpx filetype returned as empty string?
    (if (or (= (.-type file) "gpx") (gstring/endsWith (.-name file) ".gpx"))
      (-> (.text file)
          (.then #(draw-polyline (get-trkpts (dom-parse %))))
          (.catch #(handle-error "Error processing file")))
      (handle-error "File format not supported"))))


;; TODO: Revisit when working on timing tool
;;   {:lat (. trkpt getAttribute "lat")
;;    :lng (. trkpt getAttribute "lon")
;;    :el (.-textContent (first (. trkpt getElementsByTagName "ele")))
;;    :time (.-textContent (first (. trkpt getElementsByTagName "time")))})