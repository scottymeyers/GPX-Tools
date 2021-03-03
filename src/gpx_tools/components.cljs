(ns gpx-tools.components
  (:require
   [gpx-tools.utilities :as utilities]))

(defn error-message
  "Displays @app-state :error"
  [error]
  (when error [:div {:class "error"
                     :on-click #(utilities/set-error nil)}
               [:p error]]))

;; TODO: enforce only GPX files from within this componenent?
(defn file-uploader
  "Allows files to be selected or dropped"
  [id handler]
  [:div.file-drop {:on-drag-over #(.preventDefault %)
                   :on-drop (fn [e]
                              (utilities/handle-file-drop e handler))}
   [:input {:id id
            :accept ".gpx"
            :type "file"
            :multiple true
            :on-change (fn [e]
                         (utilities/handle-file-input e handler))}]
   [:label {:for id} "Drop GPX"]])
