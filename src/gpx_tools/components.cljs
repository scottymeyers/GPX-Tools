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

(defn selected-activity
  "Displays the Selected Activity data"
  [activity]
  [:section
   (if activity
     [:div
      [:h2 (util/get-activity-name activity)]
      ;; TODO: return a friendlier date funct
      [:small (util/get-activity-time activity)]]
     [:h2 "Select an Activity"])])

