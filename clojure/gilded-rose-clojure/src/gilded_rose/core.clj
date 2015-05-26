(ns gilded-rose.core)

(def sulfuras "Sulfuras, Hand of Ragnaros")
(def brie "Aged Brie")
(def backstage-passes "Backstage passes to a TAFKAL80ETC concert")
(def max-quality 50)
(def min-quality 0)

(defn age-item [item]
  (if (not= (item :name) sulfuras)
    (merge item { :sell-in (dec (item :sell-in)) })
    item))

(defn age-items [coll]
  (map #(age-item %) coll))

(defmulti adjust-quality (fn [item-name] (item-name :name)))

(defmethod adjust-quality sulfuras [item]
  item)

(defmethod adjust-quality brie [item]
  (if (< (item :quality) max-quality)
    (merge item { :quality (inc (item :quality)) })
    item))

(defmethod adjust-quality backstage-passes [item]
  (cond
    (< (item :sell-in) 0) (merge item { :quality 0 })
    (< (item :sell-in) 5) (merge item { :quality (min max-quality (+ 3 (item :quality))) })
    (< (item :sell-in) 10) (merge item { :quality (min max-quality (inc (inc (item :quality)))) })
    :else (merge item { :quality (min max-quality (inc (item :quality))) })))

(defmethod adjust-quality :default [item]
  (if (<= (item :quality) min-quality)
    item
    (if (> min-quality (item :sell-in))
      (merge item { :quality (dec (dec (item :quality))) })
      (merge item { :quality (dec (item :quality)) } ))))

(defn update-quality [items]
  (map #(adjust-quality %) (age-items items)))

(defn item [item-name, sell-in, quality]
  {:name item-name, :sell-in sell-in, :quality quality})

(defn update-current-inventory[]
  (let [inventory 
    [
      (item "+5 Dexterity Vest" 10 20)
      (item "Aged Brie" 2 0)
      (item "Elixir of the Mongoose" 5 7)
      (item "Sulfuras, Hand of Ragnaros" 0 80)
      (item "Backstage passes to a TAFKAL80ETC concert" 15 20)
    ]]
    (update-quality inventory)
    ))
