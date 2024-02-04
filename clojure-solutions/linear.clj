(defn v? [v] (and (vector? v) (every? number? v)))
(defn m? [m] (and (vector? m) (every? v? m) (apply = (mapv count m))))
(defn same-size-v? [& args] (and (every? v? args) (apply = (mapv count args))))
(defn same-size-m? [& args] (and (every? m? args) (apply = (mapv count args))))
(defn operation [pred post f & args]
      {:pre [(pred args)]
       :post [(post %)]}
      (apply mapv f args))

(def v-operation (partial operation #(apply same-size-v? %) v?))
(def m-operation (partial operation #(apply same-size-m? %) m?))

(def v+ (partial v-operation +))
(def v- (partial v-operation -))
(def v* (partial v-operation *))
(def vd (partial v-operation /))
(defn scalar [& args]
  {:pre [(apply same-size-v? args)]
   :post [(number? %)]}
  (apply + (apply v* args)))
(defn v*s [v & s]
  {:pre [(v? v) (every? number? s)]
   :post [(v? %)]}
  (let [s-product (apply * s)]
    (mapv #(* % s-product) v)))

(def m+ (partial m-operation v+))
(def m- (partial m-operation v-))
(def m* (partial m-operation v*))
(def md (partial (partial operation #(apply same-size-m? %) m?) vd))
(defn m*s [m & s]
  {:pre [(m? m) (every? number? s)]
   :post [(m? %)]}
  (mapv #(v*s % (apply * s)) m))
(defn transpose [m]
  {:pre [(m? m)]}
  (apply mapv vector m))
(defn m*v [m v]
  {:pre [(m? m) (v? v)]
   :post [(v? %)]}
  (mapv #(scalar % v) m))
(defn m*m [& args]
  {:pre [(every? m? args)]
   :post [(m? %)]}
  (reduce
    (fn [a, b]
      (let [tb (transpose b)]
        (mapv #(m*v tb %) a))) args))
(defn vect [& args]
  {:pre [(apply same-size-v? args)]
   :post [(v? %)]}
  (reduce (fn [u [v1, v2, v3]] (m*v [[0, v3, (- v2)], [(- v3), 0, v1], [v2, (- v1), 0]] u)) args))

(defn form [t] (cond (number? t) (vector)
                     (empty? t) (vector 0)
                     :else (apply conj [(count t)] (form (first t)))))
(defn match-forms? [fa fb] (or
                             (and (>= (count fa) (count fb)) (= fb (subvec fa 0 (count fb))))
                             (and (< (count fa) (count fb)) (= fa (subvec fb 0 (count fa))))))
(defn ts-operation [f] (letfn [(curr [t s] (if (number? t) (f t s) (mapv #(curr % s) t)))] curr))
(defn st-operation [f] (letfn [(curr [s t] (if (number? t) (f s t) (mapv #(curr s %) t)))] curr))
(defn tb-bin [f a b]
  {:pre [(match-forms? (form a) (form b))]}
  (cond (number? a) ((st-operation f) a b)
        (number? b) ((ts-operation f) a b)
        :else (mapv (partial tb-bin f) a b)))
(defn tb-operation [f & args] (reduce (partial tb-bin f) args))
(def tb+ (partial tb-operation +))
(def tb* (partial tb-operation *))
(def tb- (fn ([arg] (tb* -1 arg)) ([arg & args] (apply (partial tb-operation -) (cons arg args)))))
(def tbd (fn ([arg] (tbd 1 arg)) ([arg & args] (apply (partial tb-operation /) (cons arg args)))))