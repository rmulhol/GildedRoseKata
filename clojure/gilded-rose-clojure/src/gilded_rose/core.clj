(ns gilded-rose.core)

(defn age-item [item]
  (if (not= (item :name) "Sulfuras, Hand of Ragnaros")
    (merge item { :sell-in (dec (item :sell-in)) })
    item))

(defn age-items [coll]
  (map #(age-item %) coll))

(defn update-quality [items]
  (map
    (fn[item] (cond
      (and (< (:sell-in item) 0) (= "Backstage passes to a TAFKAL80ETC concert" (:name item)))
        (merge item {:quality 0})
      (or (= (:name item) "Aged Brie") (= (:name item) "Backstage passes to a TAFKAL80ETC concert"))
        (if (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert") (>= (:sell-in item) 5) (< (:sell-in item) 10))
          (merge item {:quality (min 50 (inc (inc (:quality item))))})
          (if (and (= (:name item) "Backstage passes to a TAFKAL80ETC concert") (>= (:sell-in item) 0) (< (:sell-in item) 5))
            (merge item {:quality (min 50 (inc (inc (inc (:quality item)))))})
            (if (< (:quality item) 50)
              (merge item {:quality (inc (:quality item))})
              item)))
      (< (:sell-in item) 0)
        (if (= "Backstage passes to a TAFKAL80ETC concert" (:name item))
          (merge item {:quality 0})
          (if (or (= "+5 Dexterity Vest" (:name item)) (= "Elixir of the Mongoose" (:name item)))
            (merge item {:quality (max 0 (dec (dec (:quality item))))})
            item))
      (or (= "+5 Dexterity Vest" (:name item)) (= "Elixir of the Mongoose" (:name item)))
        (merge item {:quality (max 0 (dec (:quality item)))})
      :else item))
  (age-items items)))

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
