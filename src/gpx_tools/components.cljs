(ns gpx-tools.components
  (:require
   [gpx-tools.utilities :as utilities]))

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
                                            (utilities/is-gpx-file? file)) files)]
                         (send-response (utilities/extract-gpx-from-files valid-files))))]
    (fn []
      [:div.file-drop {:on-drag-over #(.preventDefault %)
                       :on-drop #(handle-files (utilities/get-drop-files %))}
       [:input {:id id
                :accept ".gpx"
                :type "file"
                :multiple true
                :on-change #(handle-files (utilities/get-input-files %))}]
       [:label {:for id} "Drop GPX"]])))