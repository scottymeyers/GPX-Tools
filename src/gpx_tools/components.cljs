(ns gpx-tools.components
  (:require [gpx-tools.files :as files]
            [gpx-tools.utilities :as util]))

(defn error-message
  "Displays @app-state :error"
  [error set-error]
  (when error [:div {:class "error"
                     :on-click #(set-error nil)}
               [:p error]]))

(defn file-importer
  "Selects files via input or dropped"
  [id send-response]
  (let [handle-files (fn [files]
                       (let [valid-files (filter
                                          (fn [file]
                                            (files/is-gpx-file? file)) files)]
                         (send-response (files/extract-gpx-from-files valid-files))))]
    (fn []
      [:section.file-drop {:on-drag-over #(.preventDefault %)
                           :on-drop #(handle-files (files/get-drop-files %))}
       [:input {:id id
                :accept ".gpx"
                :type "file"
                :multiple true
                :on-change #(handle-files (files/get-input-files %))}]
       [:label {:for id} "Drop GPX"]])))

(defn activity-list-item
  "Displays the Selected Activity data"
  []
  (fn [activity selected-activity on-select]
    [:div
     [:button
      {:type "button"
       :style (if (util/is-selected? selected-activity activity)
                {:background "black"
                 :color "white"}
                {:background "none"})
       :on-click #(on-select activity)}
      [:h2 (util/get-activity-name activity)]
      [:small (util/friendly-date (util/get-activity-time activity))]
      [:small (util/friendly-time (util/get-activity-time activity))]]]))

(defn activities-list [activities selected-activity on-select]
  [:section
   {:class "activities-list"}
   (doall (for [activity activities]
            ^{:key (util/get-activity-time activity)}
            [activity-list-item activity selected-activity on-select]))])

(defn google-map [app-state set-error]
  (let [api-key (subs (-> js/document .-location .-search) 1)
        center (clj->js {"lat" 40.730610
                         "lng" -73.935242})
        loader (google.maps.plugins.loader.Loader.
                (clj->js {:apiKey api-key
                          :version "weekly"}))]
    (fn []
      (.addEventListener
       js/window
       "DOMContentLoaded"
       (-> (.load loader)
           (.then (fn []
                    (swap! app-state assoc :gmap (google.maps.Map.
                                                  (. js/document (getElementById "map"))
                                                  (clj->js
                                                   {:center center
                                                    :zoom 8
                                                    :fullscreenControl false
                                                    :clickableIcons false
                                                    :disableDoubleClickZoom true})))))
           (.catch #(set-error "Unable to load Google Maps"))))
      [:div {:id "map"}])))
