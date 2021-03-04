(ns gpx-tools.components
  (:require
   [gpx-tools.utilities :as utilities]))

(defn error-message
  "Displays @app-state :error"
  [error set-error]
  (when error [:div {:class "error"
                     :on-click #(set-error nil)}
               [:p error]]))

;; TODO: enforce only GPX files from within this componenent?
(defn file-importer
  "Allows files to be selected or dropped"
  [id handler error-handler]
  [:div.file-drop {:on-drag-over #(.preventDefault %)
                   :on-drop #(utilities/handle-file-drop % handler error-handler)}
   [:input {:id id
            :accept ".gpx"
            :type "file"
            :multiple true
            :on-change #(utilities/handle-file-input % handler error-handler)}]
   [:label {:for id} "Drop GPX"]])
