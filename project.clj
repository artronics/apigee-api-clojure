(defproject apigee-api "0.1.0-SNAPSHOT"
  :description "A client library to use Apigee API"
  :url "https://github.com/artronics/apigee-api-clojure.git"
  :license {:name "MIT"
            :url "https://opensource.org/licenses/MIT"}
  :dependencies [[org.clojure/clojure "1.11.0-alpha2"]
                 [clj-http "3.12.3"]
                 [org.clojure/data.json "2.4.0"]
                 ]
  :repl-options {:init-ns com.github.artronics.apigee-api.core}
  :main com.github.artronics.apigee-api.core/-main)
