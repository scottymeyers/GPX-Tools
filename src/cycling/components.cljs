(ns cycling.components
  (:require [cycling.utilities :as utilities]))

(defn file-drop [id]
  [:div.file-drop {:on-drag-over #(.preventDefault %)
                   :on-drop utilities/handle-file-drop}
   [:input {:id id
            :accept ".gpx"
            :type "file"
            :on-change utilities/handle-file-input}]
   [:label {:for id} "Drop GPX"]])
