(ns midje.bootstrap
  (:require [midje.config :as config]
            [midje.emission.api :as emission.api]
            [midje.emission.state :as emission.state]))

(defonce bootstrapped false)

(defn bootstrap []
  (when-not bootstrapped

    (let [saved-ns (ns-name *ns*)]
      (try
        (in-ns 'midje.config)
        ((ns-resolve 'midje.config 'load-config-files))
        ((ns-resolve 'midje.config 'load-env-vars))
      (finally
        (in-ns saved-ns))))

    (emission.api/load-plugin (config/choice :emitter))
    (emission.state/no-more-plugin-installation)

    (alter-var-root #'bootstrapped (constantly true))))
