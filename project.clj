(defproject hitman "0.1.0"
  :description "a clojure markdown engine"
  :url "http://github.com/chameco/Hitman"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [hiccup "1.0.4"]
                 [instaparse "1.2.2"]
                 [me.raynes/fs "1.4.4"]]
  :scm {:name "git"
        :url "http://github.com/chameco/Hitman"}
  :main hitman.core)
