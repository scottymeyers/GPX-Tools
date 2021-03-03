(ns gpx-tools.utilities)

(defn dom-parse
  "Extracts the data from GPX file"
  [xml]
  (-> (js/DOMParser.)
      (.parseFromString xml "text/xml")
      (.-firstChild)))

(defn set-error
  "Displays an Error"
  [error]
  (js/console.log error))
  ;; (reset! app-state {:error error})

(defn get-activity-name
  "Extracts the name from the supplied GPX"
  [gpx]
  (let [trk (. gpx getElementsByTagName "trk")
        name (.-innerHTML (first (. (first trk) getElementsByTagName "name")))]
    name))

(defn get-activity-trkpts
  "Extracts the trkpts from the supplied GPX"
  [gpx]
  (let [trkseg (. gpx getElementsByTagName "trkseg")
        trkpts (. (first trkseg) getElementsByTagName "trkpt")]
    trkpts))

(defn extract-gpx-from-files
  "Parses the GPX files & returns the parsed GPX"
  [files handler]
  (-> (js/Promise.all (map (fn [file]
                             (-> (.then (.text file))
                                 (.then #(dom-parse %))
                                 (.catch #(set-error "Cannot proccess file"))))
                           files))
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
  ;; [goog.string :as gstring]
  ;; (if (or (= (.-type file) "gpx") (gstring/endsWith (.-name file) ".gpx")))
  ;; (set-error "File format not supported")
  (let [files (.-files (.-dataTransfer e))]
    (extract-gpx-from-files files handler)))