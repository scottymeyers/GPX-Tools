(ns cycling.utilities)
(defn read-file [e]
  (let [file (first (-> e .-nativeEvent .-target .-files))]
    (-> (.text file)
        (.then #(js/console.log %))
        (.catch #(js/console.log %)))))