(ns bf.core)

(defn procesar-archivo [ruta] ;; Pre: el texto del archivo debe tener como max 80 columnas por linea y un max de 25 lineas.
  (with-open [lector (clojure.java.io/reader ruta)]
    (let [comandos (into []
                       (map (fn [linea]
                              (let [linea-vector (vec linea)]
                                (if (< (count linea-vector) 80)
                                  (vec (concat linea-vector (repeat (- 80 (count linea-vector)) \space))) ;;Completa cada linea que no tenga 80 comandos con " "
                                  linea-vector)))
                            (line-seq lector)))]
      (if (< (count comandos) 25) ;;De haber menos de 25 lineas agrega lineas vacias hasta llegar a 25
        (vec (concat comandos (repeat (- 25 (count comandos)) (vec (repeat 80 \space)))))
        comandos))))
(defn obtener-comando [comandos x y] ((comandos y) x )) 

(defn random-dir [] (case (rand-int 4)
                        0 "L"
                        1 "R"
                        2 "U"
                        3 "D"
))

(defn peek-seguro [pila]
  (if (empty? pila) 0 (peek pila)))

(defn pop-seguro [pila] 
  (if (empty? pila) pila (pop pila)))

(defn x-siguiente [x dir]
  (case dir
    "R" (if (> (inc x) 79) 0 (inc x))
    "RR" (if (> (+ x 2) 79) (- (+ x 2) 80) (+ x 2))
    "L" (if (< (dec x) 0) 79 (dec x))
    "LL" (if (< (- x 2) 0) (+ 79 (- x 2)) (- x 2))
    x))

(defn y-siguiente [y dir]
  (case dir
    "U" (if (< (dec y) 0) 24 (dec y))
    "UU" (if (< (- y 2) 0) (+ 24 (- y 2)) (- y 2))
    "D" (if (> (inc y) 24) 0 (inc y))
    "DD" (if (> (+ y 2) 24) (- (+ y 2) 25) (+ y 2))
    y)) 

(defn ejecutar-comando-de-pila [comando pila]
  (case comando
    \+ (conj (pop-seguro (pop-seguro pila)) (+ (peek-seguro pila) (peek-seguro (pop-seguro pila))))
    \- (conj (pop-seguro (pop-seguro pila)) (- (peek-seguro (pop-seguro pila)) (peek-seguro pila)))
    \* (conj (pop-seguro (pop-seguro pila)) (* (peek-seguro pila) (peek-seguro (pop-seguro pila))))
    \/ (conj (pop-seguro (pop-seguro pila)) (quot (peek-seguro (pop-seguro pila)) (peek-seguro pila)))
    \% (conj (pop-seguro (pop-seguro pila)) (mod (peek-seguro (pop-seguro pila)) (peek-seguro pila)))
    \: (conj pila (peek-seguro pila)) ;; los : Duplican el valor de arriba de la pila.
    \$ (pop-seguro pila) ;; el $ Elimina el valor en la cima de la pila.
    pila))

(defn recorrer [comandos]
    (loop [x 0 y 0 dir "R" pila [] string-mode? false comandos comandos]
        (let [comando (obtener-comando  comandos x y)
            dir (case comando
                            \< "L"
                            \> "R"
                            \^ "U"
                            \v "D"
                            \? (random-dir)
                            \# (str dir dir)
                            \_ (if (not= (peek-seguro pila) 0 )"L" "R")
                            \| (if (not= (peek-seguro pila) 0 )"U" "D")
                            (str(first dir)) ) 
            comandos (if (= comando \p)
                                        (let [y-nueva (peek-seguro pila)
                                              x-nueva (peek-seguro (pop-seguro pila))
                                              valor (peek-seguro (pop-seguro (pop-seguro pila)))]
                                              (if (and (>= x-nueva 0) (< x-nueva 80) (>= y-nueva 0) (< y-nueva 25))
                                              (assoc-in comandos [y-nueva x-nueva] (char valor)) 
                                              comandos)) 
                                        comandos)              

            pila (if string-mode?
                 (if (= comando \") pila (conj pila (int comando)))

                 (cond
                   (Character/isDigit comando) (conj pila (Character/digit comando 10))

                   (#{\+ \- \* \/ \% \$ \:} comando) (ejecutar-comando-de-pila comando pila)

                   (= comando \.) (do (print (peek-seguro pila)) (pop-seguro pila))

                   (= comando \,) (do (print (char (peek-seguro pila))) (pop-seguro pila))

                   (or(= comando \_)(= comando \|)) (pop-seguro pila)

                   (= comando \!) (conj (pop-seguro pila) (if (zero? (peek-seguro pila)) 1 0))
                                                                  ;;        / segundo           /primero
                   (= comando \`) (conj (pop-seguro (pop-seguro pila)) (if (< (peek-seguro pila) (peek-seguro (pop-seguro pila))) 1 0))

                   (= comando \\) (conj (pop-seguro (pop-seguro pila)) (peek-seguro pila) (peek-seguro (pop-seguro pila)))

                   (= comando \g) (let [y (peek-seguro pila) 
                                    x (peek-seguro (pop-seguro pila))
                                    valor (if (and (>= x 0)(< x 80)(>= y 0)(< y 25))(int (obtener-comando comandos x y)) 0)] 
                                    (conj (pop-seguro (pop-seguro pila)) valor))

                   (= comando \p) (pop-seguro (pop-seguro (pop-seguro pila)))

                   (= comando \~) (let [input-char (read-line)]
                                    (conj pila (int (first input-char))))

                   (= comando \&) (let [input (read-line)] ;;agrega a la pila el primer numero q encuentra en el string.
                                    (if (not= (re-find #"\d+" input) nil) (conj pila (Integer/parseInt (re-find #"\d+" input)))))                  
                   :else pila))

            string-mode? (if (= comando \") (not string-mode?) string-mode?)]
          

          (if (or (not= comando \@) string-mode? )
              (recur (x-siguiente x dir) (y-siguiente y dir) dir pila string-mode? comandos)))))

(defn -main [& args]
    (let [entrada (first args)
    comandos (procesar-archivo (str "programas/" entrada))]
    (recorrer comandos)
    )
)