(ns com.github.artronics.apigee-api.product
  (:require [com.github.artronics.apigee-api.core :refer [get-body auth-header make-body]]
            [clj-http.client :as client]))

(def default-base-url "https://api.enterprise.apigee.com/v1")

(defn api-path
  ([base-url org-name]
   (str base-url "/organizations/" org-name))
  ([org-name] (api-path default-base-url org-name)))

(defn product-url
  ([base-url product-name]
   (str base-url "/apiproducts/" product-name))
  ([base-url] (str base-url "/apiproducts")))

(defn make-product
  [{:keys [name displayName approvalType] :as required} &
   [{:keys [description attributes environments proxies scopes]
     :or   {description  ""
            attributes   []
            environments []
            proxies      []
            scopes       []}
     :as   optional}]]

  (merge required optional))


(defn get-product
  ([url token]
   (get-body (client/get url {:headers (auth-header token)}))))

(defn create-product
  [url token product]
  (get-body (client/post url {:headers      (auth-header token)
                              :content-type :json
                              :body         (make-body product)})))

(defn update-product
  [url token product]
  (get-body (client/put url {:headers      (auth-header token)
                             :content-type :json
                             :body         (make-body (dissoc product :name))})))

(defn delete-product
  [url org-name token product-name]
  (let [params {:org_name org-name :apiproduct_name product-name}]
    (get-body (client/delete url {:params  params
                                  :headers (auth-header token)}))))

(defn make-product-api
  [{:keys [base-api-url org-name token] :or {base-api-url default-base-url}}]
  (let [url #(product-url (api-path base-api-url org-name) %)]
    {:get    (fn [& [product-name]] (get-product (url product-name) token))
     :create (fn [product] (create-product (url "") token product))
     :update (fn [product] (update-product (url (:name product)) token product))
     :delete (fn [product-name] (delete-product (url product-name) org-name token product-name))}))


