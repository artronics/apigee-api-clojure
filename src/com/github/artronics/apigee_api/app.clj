(ns com.github.artronics.apigee-api.app
  (:require [com.github.artronics.apigee-api.core :refer [api-path get-body auth-header make-body]]
            [clj-http.client :as client]))

(defrecord ApigeeApp [name])

(deftype Attribute [name value])

(defn app-url
  ([app-name, {:keys [developer-email] :as opt}]
   (let [path (str "developers/" developer-email "/apps" (if app-name (str "/" app-name) ""))]
     (api-path opt path)))
  ([opt] (app-url "" opt)))

(defn custom-attr-url
  [app-name, custom-attr-name, opt]
  (let [path (str "attributes" (if custom-attr-name (str "/" custom-attr-name) ""))]
    (str (app-url app-name opt) "/" path)))

(defn get-app
  [app-name, {:keys [token] :as opt}]
  (let [url (app-url app-name opt)]
    (get-body (client/get url {:headers (auth-header token)}))))

(def get-apps (partial get-app ""))

(defn create-app
  [^ApigeeApp app, {:keys [token org developer-email] :as opt}]
  (let [url (app-url "" opt)
        params {:org_name org :developer_email developer-email}]
    (get-body (client/post url {:params       params
                                :headers      (auth-header token)
                                :content-type :json
                                :body         (make-body app)}))))

(defn delete-app
  [app-name, {:keys [token org developer-email] :as opt}]
  (let [url (app-url app-name opt)
        params {:org_name org :developer_email developer-email}]
    (get-body (client/delete url {:params  params
                                  :headers (auth-header token)}))))

(defn get-custom-attribute
  [attr-name, app-name, {token :token :as opt}]
  (let [url (custom-attr-url app-name attr-name opt)]
    (println url)
    (get-body (client/get url {:headers (auth-header token)}))))

(defn create-custom-attributes
  [attributes, app-name, {:keys [token org developer-email] :as opt}]
  (let [url (custom-attr-url app-name "" opt)
        params {:org_name org :developer_email developer-email :app_name app-name}]
    (println url)
    (get-body (client/post url {:params       params
                                :headers      (auth-header token)
                                :content-type :json
                                :body         (make-body {:attribute attributes})}))))

(defn delete-custom-attribute
  [attribute-name, app-name, {:keys [token org developer-email] :as opt}]
  (let [url (custom-attr-url app-name attribute-name opt)
        params {:org_name        org
                :developer_email developer-email
                :app_name        app-name
                :attribute_name  attribute-name}]
    (get-body (client/delete url {:params  params
                                  :headers (auth-header token)}))))
