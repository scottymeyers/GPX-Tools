(ns cycling.components)

;; props
;; :change-event: change event handler
(defn input-file [props]
  [:input {:accept ".gpx"
           :type "file"
           :on-change (props :change-event)}])
