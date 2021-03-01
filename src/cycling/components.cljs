(ns cycling.components
  (:require
   [cycling.utilities :as utilities]))

(defn error-message
  "Displays @app-state :error"
  [error]
  (when error [:div {:class "error"
                     :on-click #(utilities/set-error nil)}
               [:p error]]))

(defn file-drop [id]
  [:div.file-drop {:on-drag-over #(.preventDefault %)
                   :on-drop utilities/handle-file-drop}
   [:input {:id id
            :accept ".gpx"
            :type "file"
            :multiple true
            :on-change utilities/handle-file-input}]
   [:label {:for id} "Drop GPX"]])
