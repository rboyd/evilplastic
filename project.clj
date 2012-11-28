(defproject evilplastic "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}

  :plugins [[lein-localrepo "0.4.1"]]
  :jvm-opts ["-d32"]
  :dependencies [[org.clojure/clojure "1.4.0"]
                 [log4j/log4j "1.2.16"]
                 [xstream/xstream "1.3.1"]
                 [bluecove/bluecove "2.1.0"]
[pccomm/pccomm "1.0.0"]
                 [robustpc/robustpc "1.0.0"]])
