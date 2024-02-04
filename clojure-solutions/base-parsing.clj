(defn -return [value tail] {:value value :tail tail})
(def -valid? boolean)
(def -value :value)
(def -tail :tail)

(defn -show [result]
  (if (-valid? result)
    (str "-> " (pr-str (-value result)) " | " (pr-str (apply str (-tail result))))
    "!"))
(defn tabulate [parser inputs]
  (run! (fn [input] (printf "    %-10s %s\n" (pr-str input) (-show (parser input)))) inputs))

(def _empty (partial partial -return))

(defn _char [p]
  (fn [[c & cs]]
    (if (and c (p c))
      (-return c cs))))

(defn _map [f result]
  (if (-valid? result)
    (-return (f (-value result)) (-tail result))))

(defn _combine [f a b]
  (fn [input]
    (let [ar (a input)]
      (if (-valid? ar)
        (_map (partial f (-value ar))
              ((force b) (-tail ar)))))))

(defn _either [a b]
  (fn [input]
    (let [ar (a input)]
      (if (-valid? ar)
        ar
        ((force b) input)))))

(defn _parser [parser]
  (let [pp (_combine (fn [v _] v) parser (_char #{\u0000}))]
    (fn [input] (-value (pp (str input \u0000))))))

(defn +char [chars]
  (_char (set chars)))

(defn +char-not [chars]
  (_char (comp not (set chars))))

(defn +map [f parser]
  (comp (partial _map f) parser))

(def +parser _parser)

(def +ignore
  (partial +map (constantly 'ignore)))

(defn- iconj [coll value]
  (if (= value 'ignore)
    coll
    (conj coll value)))
(defn +seq [& parsers]
  (reduce (partial _combine iconj) (_empty []) parsers))

(defn +seqf [f & parsers]
  (+map (partial apply f) (apply +seq parsers)))

(defn +seqn [n & parsers]
  (apply +seqf #(nth %& n) parsers))

(defn +or [parser & parsers]
  (reduce _either parser parsers))

(defn +opt [parser]
  (+or parser (_empty nil)))

(defn +star [parser]
  (letfn [(rec [] (+or (+seqf cons parser (delay (rec))) (_empty ())))]
    (rec)))

(defn +plus [parser]
  (+seqf cons parser (+star parser)))

(defn +str [parser]
  (+map (partial apply str) parser))