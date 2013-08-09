(ns hitman.core
  (:use [hiccup.core])
  (:require [clojure.java.io :as io]
            [clojure.string :as string]
            [instaparse.core :as insta]
            [me.raynes.fs :as fs])
  (:gen-class))

(def parse-md
  (insta/parser
    "<Blocks> = (Paragraph | Header | List | Ordered | Code | Rule)+
    Header = Line Headerline Blankline+
    <Headerline> = h1 | h2
    h1 = '='+
    h2 = '-'+
    List = Listline+ Blankline+
    Listline = Listmarker Whitespace+ Word (Whitespace Word)* EOL
    <Listmarker> = <'+' | '*' | '-'>
    Ordered = Orderedline+ Blankline+
    Orderedline = Orderedmarker Whitespace* Word (Whitespace Word)* EOL
    <Orderedmarker> = <#'[0-9]+\\.'>
    Code = Codeline+ Blankline+
    Codeline = <Space Space Space Space> (Whitespace | Word)* EOL
    Rule = Ruleline Blankline+
    <Ruleline> = <'+'+ | '*'+ | '-'+>
    Paragraph = Line+ Blankline+
    <Blankline> = Whitespace* EOL
    <Line> = Linepre Word (Whitespace Word)* Linepost EOL
    <Linepre> = (Space (Space (Space)? )? )?
    <Linepost> = Space?
    <Whitespace> = #'(\\ | \\t)+'
    <Space> = ' '
    <Word> = #'\\S+'
    <EOL> = <'\\n'>"))

(def span-elems
  [[#"!\[(\S+)\]\((\S+)\)" (fn [[n href]] (html [:img {:src href :alt n}]))]
   [#"\[(\S+)\]\((\S+)\)"  (fn [[n href]] (html [:a {:href href} n]))]
   [#"`(\S+)`"             (fn [s] (html [:code s]))]
   [#"\*\*(\S+)\*\*"       (fn [s] (html [:strong s]))]
   [#"__(\S+)__"           (fn [s] (html [:strong s]))]
   [#"\*(\S+)\*"           (fn [s] (html [:em s]))]
   [#"_(\S+)_"             (fn [s] (html [:em s]))]])

(defn- parse-span [s]
  (let [res (first (filter (complement nil?)
                           (for [[regex func] span-elems]
                             (let [groups (re-matches regex s)]
                               (if groups (func (drop 1 groups)))))))]
    (if (nil? res) s res)))

(defn- output-html [blocks]
  (reduce str
          (for [b blocks]
            (case (first b)
              :List (html [:ul (for [li (drop 1 b)] [:li (apply str (map parse-span (drop 1 li)))])])
              :Ordered (html [:ol (for [li (drop 1 b)] [:li (apply str (map parse-span (drop 1 li)))])])
              :Header (html [(first (last b)) (apply str (map parse-span (take (- (count b) 2) (drop 1 b))))])
              :Code (html [:pre [:code (apply str (interpose "<br />" (for [line (drop 1 b)] (apply str (drop 1 line)))))]])
              :Rule (html [:hr])
              :Paragraph (html [:p (apply str (map parse-span (drop 1 b)))])))))

(def markdown-to-html (comp output-html parse-md))

(defn -main [path & args]
  (spit (str (fs/base-name path true) ".html") (markdown-to-html (slurp path))))
