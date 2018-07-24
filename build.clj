; Build script based on http://www.functionalbytes.nl/clojure/nodejs/figwheel/repl/clojurescript/cli/2017/12/20/tools-deps-figwheel.html
(require '[cljs.build.api :as api])

(defn try-require
  [ns-sym]
  (try (require ns-sym) true (catch Exception e false)))

(defmacro with-namespaces
  [namespaces & body]
  (if (every? try-require namespaces)
    `(do ~@body)
    `(println "task not available - required dependencies not found")))

(def source-dir "src/cljs")

(def compiler-config
  {:main          'graphql-demo.core
   :output-to     "resources/public/js/compiled/app.js"
   :optimizations :advanced})

(def dev-config
  (merge compiler-config
         {:optimizations   :none
          :source-map      true
          :preloads        '[devtools.preload]
          :asset-path      "js/compiled/out"
          :output-dir      "resources/public/js/compiled/out"
          :external-config {:devtools/config {:features-to-install :all}}}))

(def figwheel-options
  {:css-dirs ["resources/public/css"]})

(defmulti task first)

(defmethod task :default
  [_]
  (let [all-tasks  (-> task methods (dissoc :default) keys sort)
        interposed (->> all-tasks (interpose ", ") (apply str))]
    (println "Unknown or missing task. Choose one of:" interposed)
    (System/exit 1)))

(defmethod task "compile" [args]
  (api/build source-dir compiler-config))

(defmethod task "figwheel" [[_ port]]
  (with-namespaces [figwheel-sidecar.repl-api dev]
                   (dev/-main)
                   (figwheel-sidecar.repl-api/start-figwheel!
                     {:figwheel-options (cond-> figwheel-options
                                                port (merge {:nrepl-port       (some-> port Long/parseLong)
                                                             :nrepl-middleware ["cider.nrepl/cider-middleware"
                                                                                "refactor-nrepl.middleware/wrap-refactor"
                                                                                "cemerick.piggieback/wrap-cljs-repl"]}))
                      :all-builds       [{:id           "dev"
                                          :figwheel     true
                                          :source-paths [source-dir]
                                          :compiler     dev-config}]})
                   (when-not port
                     (figwheel-sidecar.repl-api/cljs-repl))))

(task *command-line-args*)
