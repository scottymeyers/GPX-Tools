(ns cycling.utilities
  (:require [goog.string :as gstring]
            [cycling.core :as core :refer [app-state]]
            [cycling.map :as map :refer [draw-polyline]]))

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

;; TODO: allow multiple files
;; TODO: clear input value after its processed
(defn handle-file-input
  "Parses the File Input event"
  [e]
  (.preventDefault e)
  (let [file (first (-> e .-nativeEvent .-target .-files))]
    (if file
      (-> (.text file)
          (.then #(draw-polyline (get-trkpts (dom-parse %))))
          (.catch #(set-error "Cannot process file")))
      (set-error "No File Selected"))))

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
          (.catch #(set-error "Error processing file")))
      (set-error "File format not supported"))))

;; TODO: Revisit when working on timing tool
;;   {:lat (. trkpt getAttribute "lat")
;;    :lng (. trkpt getAttribute "lon")
;;    :el (.-textContent (first (. trkpt getElementsByTagName "ele")))
;;    :time (.-textContent (first (. trkpt getElementsByTagName "time")))})