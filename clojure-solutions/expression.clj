;Functional
(defn unchecked-divide ([] 1)
  ([arg] (/ 1.0 arg))
  ([arg & args] (if (some zero? args) ##Inf (apply / (cons arg args)))))

(defn operation [f & args] (fn [var-map] (apply f (mapv #(% var-map) args))))
(defn unary-operation [f arg] (fn [var-map] (f (arg var-map))))
(def add (partial operation +))
(def subtract (partial operation -))
(def multiply (partial operation *))
(def divide (partial operation unchecked-divide))
(def negate (partial unary-operation -))
(defn meansq-op [& args] (/ (apply + (mapv #(* % %) args)) (count args)))
(defn rms-op [& args] (Math/sqrt (apply meansq-op args)))
(def meansq (partial operation meansq-op))
(def rms (partial operation rms-op))
(def constant constantly)
(defn variable [name] (fn [var-map] (get var-map name)))

(def operations {
                 "+"      add
                 "-"      subtract
                 "*"      multiply
                 "/"      divide
                 "negate" negate
                 "meansq" meansq
                 "rms"    rms
                 })

(defn parse [expr expr-map const-func var-func]
  (cond (number? expr) (const-func expr)
        (symbol? expr) (var-func (name expr))
        (seq? expr) (apply (get expr-map (name (first expr))) (mapv #(parse % expr-map const-func var-func) (rest expr)))))

(defn parseFunction [string]
  (parse (read-string string) operations constant variable))

;Object
(use '[clojure.string :only [join lower-case]])

(load-file "base-object.clj")

(def value (field :value))
(def var-name (field :name))
(def short-var-name (fn [this] (lower-case (str (get (proto-get this :name) 0)))))
(def sign (field :sign))
(def args (field :args))
(def evaluate (method :evaluate))
(def diff (method :diff))
(def toString (method :toString))
(def toStringInfix (method :toStringInfix))
(def make-action (method :make-action))

(declare Constant Add Multiply Subtract Divide Negate Sumexp LSE)

(def ConstantPrototype
  {:evaluate      value
   :toString      (fn [this] (str (value this)))
   :toStringInfix toString
   :diff          (fn [_ _] (Constant 0))})
(defn _Constant [this value] (assoc this :value value))
(def Constant (constructor _Constant ConstantPrototype))
(def zero (Constant 0))
(def one (Constant 1))

(def VariablePrototype
  {:evaluate      (fn [this var-map] (get var-map (short-var-name this)))
   :toString      var-name
   :toStringInfix var-name
   :diff          (fn [this var] (if (= (var-name this) var) one zero))})
(defn _Variable [this name] (assoc this :name name))
(def Variable (constructor _Variable VariablePrototype))

(defn diff-add [this var] (apply Add (map #(diff % var) (args this))))
(defn diff-subtract [this var] (apply Subtract (map #(diff % var) (args this))))
(defn diff-multiply [this var] (let [args (args this)
                                     [first & second] args]
                                 (cond (== 0 (count args)) zero
                                       (== 1 (count args)) (diff first var)
                                       :else (Add (apply Multiply (diff first var) second)
                                                  (Multiply first (diff (apply Multiply second) var))))))
(defn diff-divide [this var] (let [first (first (args this)) second (rest (args this))]
                               (if (= 1 (count (args this))) (Negate (Divide (diff first var) first first))
                                                             (Divide
                                                               (Subtract
                                                                 (Multiply (diff first var) (apply Multiply second))
                                                                 (Multiply first (diff (apply Multiply second) var)))
                                                               (apply Multiply second) (apply Multiply second)))))
(defn diff-negate [this var] (Negate (diff (first (args this)) var)))
(defn diff-sumexp [this var] (apply Add (mapv #(Multiply (diff % var) (Sumexp %)) (args this))))
(defn diff-lse [this var] (Divide (diff-sumexp this var) (apply Sumexp (args this))))
(defn diff-plug [_ _] zero)

(def ExpressionPrototype
  {:evaluate (fn [this var-map] (apply (partial make-action this) (map #(evaluate % var-map) (args this))))
   :toString (fn [this] (str "(" (sign this) " " (join " " (mapv toString (args this))) ")"))})

(def BinaryOperationPrototype (assoc ExpressionPrototype
                                :toStringInfix (fn [this] (str "(" (toStringInfix (first (args this))) " " (sign this)
                                                               " " (toStringInfix (last (args this))) ")"))))
(def UnaryOperationPrototype (assoc ExpressionPrototype
                               :toStringInfix (fn [this] (str (sign this) " " (toStringInfix (last (args this)))))))

(defn Expression [this & args] (assoc this :args args))
(defn UnaryOperation [this arg] (Expression this arg))

(defn make-expression [constr proto sign make-action diff] (constructor constr (assoc proto
                                                      :sign sign
                                                      :make-action (fn [_ & args] (apply make-action args))
                                                      :diff diff)))
(def make-binary-operation (partial make-expression Expression BinaryOperationPrototype))
(def make-unary-operation (partial make-expression UnaryOperation UnaryOperationPrototype))

(def Add (make-binary-operation "+" + diff-add))
(def Subtract (make-binary-operation "-" - diff-subtract))
(def Multiply (make-binary-operation "*" * diff-multiply))
(def Divide (make-binary-operation "/" unchecked-divide diff-divide))
(def Negate (make-unary-operation "negate" #(- %) diff-negate))

(defn sumexp-op [& args] (apply + (mapv #(Math/exp %) args)))
(def Sumexp (make-binary-operation "sumexp" sumexp-op diff-sumexp))
(defn lse-op [& args] (Math/log (apply sumexp-op args)))
(def LSE (make-binary-operation "lse" lse-op diff-lse))

(def Not (make-unary-operation "!" (fn [fst & _] (if (not (< 0 fst)) 1 0)) diff-plug))
(defn boolean-op [f] (fn [fst snd & _] (if (f (< 0 fst) (< 0 snd)) 1 0)))
(def And (make-binary-operation "&&" (boolean-op #(and %1 %2)) diff-plug))
(def Or (make-binary-operation "||" (boolean-op #(or %1 %2)) diff-plug))
(def Xor (make-binary-operation "^^" (boolean-op not=) diff-plug))
(def Impl (make-binary-operation "->" (boolean-op #(or (not %1) %2)) diff-plug))
(def Iff (make-binary-operation "<->" (boolean-op =) diff-plug))

(def operationsObj {
                    "+"      Add
                    "-"      Subtract
                    "*"      Multiply
                    "/"      Divide
                    "negate" Negate
                    "lse"    LSE
                    "sumexp" Sumexp
                    "!"      Not
                    "||"     Or
                    "&&"     And
                    "^^"     Xor
                    "->"     Impl
                    "<->"    Iff
                    })

(defn parseObject [string]
  (parse (read-string string) operationsObj Constant Variable))

;Parsing
(load-file "base-parsing.clj")

(def *all-chars (mapv char (range 0 128)))
(def *digit (+char "0123456789"))
(def *space (+char (apply str (filter #(Character/isWhitespace %) *all-chars))))
(def *ws (+ignore (+star *space)))
(defn +word [word] (+str (apply +seq (mapv #(+char (str %)) word))))
(def *minus (+opt (+char "-")))
(def *point (+opt (+char ".")))
(def *number (+map read-string (+seqf str *minus (+str (+plus *digit)) *point (+str (+star *digit)))))
(def *var-sym (+char "xyzXYZ"))
(def *variable (+map Variable (+str (+plus *var-sym))))
(def *const (+map Constant *number))

(defn reduce-op [dir operations-map]
  (fn
    ([fst] fst)
    ([fst op snd & args] (apply (reduce-op dir operations-map)
                                (reduce (operations-map (str op)) (dir [fst, snd])) args))))

(defn make-priority [dir next-priority & ops]
  (let [operations-map (apply assoc {} ops)]
    (+seqf #(apply (reduce-op dir operations-map) (dir (flatten (cons %1 %2))))
           *ws next-priority *ws
           (+star (+seq (apply +or (map +word (keys operations-map))) *ws next-priority *ws)))))

(declare *iff *max-priority)
(def *unary (let [operations-map {"negate" Negate "!" Not}]
  (+seqf #((operations-map %1) %2) *ws (apply +or (map +word (keys operations-map))) *ws (delay *max-priority) *ws)))
(def *max-priority (+or *const *variable *unary
                        (+seqn 1 *ws (+char "(") *ws (delay *iff) *ws (+char ")") *ws)))
(def *first-priority  (make-priority identity *max-priority "*" Multiply "/" Divide))
(def *second-priority (make-priority identity *first-priority "+" Add "-" Subtract))
(def *and  (make-priority identity *second-priority "&&" And))
(def *or   (make-priority identity *and "||" Or))
(def *xor  (make-priority identity *or "^^" Xor))
(def *impl (make-priority reverse *xor "->" Impl))
(def *iff  (make-priority identity *impl "<->" Iff))

(def parseObjectInfix (+parser (+seqn 0 *ws *iff *ws)))
