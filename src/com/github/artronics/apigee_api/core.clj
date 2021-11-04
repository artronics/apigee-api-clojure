(ns com.github.artronics.apigee-api.core
  (:require [clj-http.client :as client]
            [clojure.data.json :as json]))

(defrecord Apigee [org developer-email app-name product-name])
(defrecord Config [org token base-url])

(defn api-path
  ([{:keys [base-url org-name]} & path-segments]
   (let [base (if base-url (str base-url "/organizations") "https://api.enterprise.apigee.com/v1/organizations")
         all (concat [base org-name] path-segments)]
     (clojure.string/join "/" all))))

(defn get-body
  [res]
  (json/read-str (:body res)))

(defn make-body
  [body]
  (json/write-str (into {} body)))

(defn app-url
  [{:keys [org developer-email app-name]}]
  (str (api-path org) "/developers/" developer-email "/apps/" app-name))

(defn product-url
  [{:keys [org product-name]}]
  (str (api-path org) "/" "apiproducts/" product-name))

(defn auth-header
  [token]
  {:Authorization (str "Bearer " token)})

(defn get-app
  [token ^Apigee apigee]
  (let [url (app-url apigee)]
    (get-body (client/get url {:headers (auth-header token)}))))


(defn get-product
  [token ^Apigee apigee]
  (let [url (product-url apigee)]
    (get-body (client/get url {:headers (auth-header token)}))))

(defn create-app
  [token
   ^Apigee {:keys [org developer-email] :as apigee}
   ^ApigeeApp app]
  (let [url (app-url (dissoc apigee :app-name))
        params {:org_name org :developer_email developer-email}]
    (get-body (client/post url {:params params
                                :headers (auth-header token)
                                :content-type :json
                                :body (make-body app)}))))

(defn -main
  []
  (println "hello"))
