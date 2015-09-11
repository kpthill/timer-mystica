(defproject timer-mystica "0.1.0-SNAPSHOT"
  :description "A mobile-based game timer for the board game Terra Mystica"
  :url "https://github.com/dphilipson/timer-mystica"
  :license {:name "MIT License"
            :url "https://opensource.org/licenses/MIT"}

  :dependencies [[org.clojure/clojure "1.7.0"]
                 [org.clojure/clojurescript "0.0-3297"]
                 [org.clojure/core.async "0.1.346.0-17112a-alpha"]
                 [reagent "0.5.0" :exclusions [cljsjs/react]]
                 [cljsjs/react-with-addons "0.12.2-4"]]

  :plugins [[lein-cljsbuild "1.0.5"]
            [lein-figwheel "0.3.5"]]

  :source-paths ["src"]

  :clean-targets ^{:protect false} ["resources/public/js/compiled"
                                    "resources/public/js/test"
                                    "target"]

  :cljsbuild {
    :builds [{:id "dev"
              :source-paths ["src"]
              :figwheel { :on-jsload "timer-mystica.core/on-js-reload" }
              :compiler {:main timer-mystica.core
                         :asset-path "js/compiled/out"
                         :output-to "resources/public/js/compiled/timer_mystica.js"
                         :output-dir "resources/public/js/compiled/out"
                         :source-map-timestamp true
                         :cache-analysis true}}
             {:id "test"
              :source-paths ["src" "test"]
              :compiler {:output-to "resources/public/js/test/test.js"
                         :output-dir "resources/public/js/test/out"
                         :optimizations :none
                         :main timer-mystica.core-test
                         :asset-path "js/test/out"
                         :source-map-timestamp true
                         :cache-analysis true}}
             {:id "min"
              :source-paths ["src"]
              :compiler {:output-to "resources/public/js/compiled/timer_mystica.js"
                         :main timer-mystica.core
                         :optimizations :advanced
                         :pretty-print false}}]}

  :figwheel {
             ;; :http-server-root "public" ;; default and assumes "resources" 
             ;; :server-port 3449 ;; default
             ;; :server-ip "127.0.0.1" 

             :css-dirs ["resources/public/css"] ;; watch and update CSS

             ;; Start an nREPL server into the running figwheel process
             ;; :nrepl-port 7888

             ;; Server Ring Handler (optional)
             ;; if you want to embed a ring handler into the figwheel http-kit
             ;; server, this is for simple ring servers, if this
             ;; doesn't work for you just run your own server :)
             ;; :ring-handler hello_world.server/handler

             ;; To be able to open files in your editor from the heads up display
             ;; you will need to put a script on your path.
             ;; that script will have to take a file path and a line number
             ;; ie. in  ~/bin/myfile-opener
             ;; #! /bin/sh
             ;; emacsclient -n +$2 $1
             ;;
             ;; :open-file-command "myfile-opener"

             ;; if you want to disable the REPL
             ;; :repl false

             ;; to configure a different figwheel logfile path
             ;; :server-logfile "tmp/logs/figwheel-logfile.log" 
             })
