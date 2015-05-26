(ns gilded-rose.core-spec
  (:require [speclj.core :refer :all]
            [gilded-rose.core :as core]))

(describe "core/update-quality"
  (it "updates an inventory of items"
    (should= [{ :name "+5 Dexterity Vest", :sell-in 9, :quality 19 }
              { :name "Aged Brie", :sell-in 1, :quality 1 }
              { :name "Elixir of the Mongoose", :sell-in 4, :quality 6 }
              { :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 }
              { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 14, :quality 21 }]
             (core/update-current-inventory))))

(describe "+5 Dexterity Vest"
  (it "degrades quality -1 when sell-in is above 0"
    (should= [{ :name "+5 Dexterity Vest", :sell-in 9, :quality 19 }]
             (core/update-quality [{ :name "+5 Dexterity Vest", :sell-in 10, :quality 20 }])))

  (it "degrades quality -2 when sell-in falls below 0"
    (should= [{ :name "+5 Dexterity Vest", :sell-in -1, :quality 8 }]
             (core/update-quality [{ :name "+5 Dexterity Vest", :sell-in 0, :quality 10 }])))
  
  (it "cannot fall below 0 in quality"
    (should= [{ :name "+5 Dexterity Vest", :sell-in 9, :quality 0 }]
             (core/update-quality [{ :name "+5 Dexterity Vest", :sell-in 10, :quality 0 }]))))

(describe "Aged Brie"
  (it "increases in quality as time passes"
    (should= [{ :name "Aged Brie", :sell-in 1, :quality 1 }]
             (core/update-quality [{ :name "Aged Brie", :sell-in 2, :quality 0 }])))
  
  (it "cannot exceed 50 quality"
    (should= [{ :name "Aged Brie", :sell-in 1, :quality 50 }]
             (core/update-quality [{ :name "Aged Brie", :sell-in 2, :quality 50 }]))))

(describe "Elixir of the Mongoose"
  (it "degrades quality -1 when sell-in is above 0"
    (should= [{ :name "Elixir of the Mongoose", :sell-in 4, :quality 6 }]
             (core/update-quality [{ :name "Elixir of the Mongoose", :sell-in 5, :quality 7 }])))

  (it "degrades quality -2 when sell-in is reaches 0"
    (should= [{ :name "Elixir of the Mongoose", :sell-in -1, :quality 8 }]
             (core/update-quality [{ :name "Elixir of the Mongoose", :sell-in 0, :quality 10 }])))
  
  (it "cannot fall below 0 in quality"
    (should= [{ :name "Elixir of the Mongoose", :sell-in -1, :quality 0 }]
             (core/update-quality [{ :name "Elixir of the Mongoose", :sell-in 0, :quality 0 }]))))

(describe "Sulfuras, Hand of Ragnaros"
  (it "never changes in quality or sell-in"
    (should= [{ :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 }]
             (core/update-quality [{ :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 }]))))

(describe "Backstage passes to a TAFKAL80ETC concert"
  (it "increases in quality +1 if greater than 10 sell-in"
    (should= [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 14, :quality 21 }]
             (core/update-quality [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 15, :quality 20 }])))
  
  (it "increases in quality +2 if between 6 and 10 sell-in"
    (should= [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 9, :quality 12 }]
             (core/update-quality [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 10, :quality 10 }])))
  
  (it "increases in quality +3 if between 1 and 5 sell-in"
    (should= [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 4, :quality 13 }]
             (core/update-quality [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 5, :quality 10 }])))
  
  (it "falls to 0 quality if 0 sell-in days remaining"
    (should= [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in -1, :quality 0 }]
             (core/update-quality [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 0, :quality 10 }])))
  
  (it "cannot fall below 0 in quality"
    (should= [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in -2, :quality 0 }]
             (core/update-quality [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in -1, :quality 0 }])))
  
  (it "cannot exceed 50 quality"
    (should= [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 4, :quality 50 }]
             (core/update-quality [{ :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 5, :quality 50 }]))))

(describe "Conjured"
  (it "degrades quality -2 when sell-in is above 0"
    (should= [{ :name "Conjured", :sell-in 4, :quality 8 }]
             (core/update-quality [{ :name "Conjured", :sell-in 5, :quality 10 }])))
  
  (it "degrades in quality -4 when sell-in falls below 0"
    (should= [{ :name "Conjured", :sell-in -1, :quality 6 }]
             (core/update-quality [{ :name "Conjured", :sell-in 0, :quality 10 }])))

  (it "cannot fall below 0 in quality"
    (should= [{ :name "Conjured", :sell-in -1, :quality 0 }]
             (core/update-quality [{ :name "Conjured", :sell-in 0, :quality 0 }]))))

(describe "age-item"
  (it "decrements the :sell-in time for a normal item"
    (should= { :name "+5 Dexterity Vest", :sell-in 9, :quality 20 }
             (core/age-item { :name "+5 Dexterity Vest", :sell-in 10, :quality 20 })))
  
  (it "does not decrement the :sell-in time for Sulfuras"
    (should= { :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 }
             (core/age-item { :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 }))))

(describe "age-items"
  (it "ages a collection of items, not decrementing :sell-in for Sulfuras"
    (should= [{ :name "+5 Dexterity Vest", :sell-in 9, :quality 20 }
              { :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 }]
             (core/age-items [{ :name "+5 Dexterity Vest", :sell-in 10, :quality 20 }
                              { :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 }]))))

(describe "adjust-quality"
  (it "decrements quality by 1 for normal item with days remaining"
    (should= { :name "+5 Dexterity Vest", :sell-in 10, :quality 9 }
             (core/adjust-quality
               { :name "+5 Dexterity Vest", :sell-in 10, :quality 10 })))
  
  (it "decrements quality by 2 for normal items that are expired"
    (should= { :name "+5 Dexterity Vest", :sell-in -1, :quality 8 }
             (core/adjust-quality
               { :name "+5 Dexterity Vest", :sell-in -1, :quality 10 })))
  
  (it "cannot reduce quality of a normal item below 0"
    (should= { :name "+5 Dexterity Vest", :sell-in -1, :quality 0 }
             (core/adjust-quality
               { :name "+5 Dexterity Vest", :sell-in -1, :quality 0 })))
  
  (it "does not alter quality of Sulfuras"
    (should= { :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 }
             (core/adjust-quality
               { :name "Sulfuras, Hand of Ragnaros", :sell-in 0, :quality 80 })))
  
  (it "increments the quality of Aged Brie by 1"
    (should= { :name "Aged Brie", :sell-in 2, :quality 1 }
             (core/adjust-quality 
               { :name "Aged Brie", :sell-in 2, :quality 0 })))
  
  (it "cannot increase the quality of Aged Brie above 50"
    (should= { :name "Aged Brie", :sell-in 2, :quality 50 }
             (core/adjust-quality
               { :name "Aged Brie", :sell-in 2, :quality 50 })))
  
  (it "increments the quality of Backstage passes by 1 when more then 10 days remaining"
    (should= { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 15, :quality 6 }
               (core/adjust-quality 
                 { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 15, :quality 5 })))

  (it "increments the quality of Backstage passes by 2 when less than 10 days remaining"
    (should= { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 9, :quality 7 }
               (core/adjust-quality 
                 { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 9, :quality 5 })))
  
  (it "increments the quality of backstage passes by 3 when less than 5 days remaining"
    (should= { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 4, :quality 8 }
               (core/adjust-quality
                 { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 4, :quality 5 })))
  
  (it "decrements quality to 0 after concert"
    (should= { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in -1, :quality 0 }
             (core/adjust-quality
               { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in -1, :quality 50 })))

  (it "cannot increase quality greate than 50"
    (should= { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 5, :quality 50 }
             (core/adjust-quality
               { :name "Backstage passes to a TAFKAL80ETC concert", :sell-in 5, :quality 50 }))))
