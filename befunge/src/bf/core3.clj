(ns bf.core3)

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
  (if (empty? pila)0 (peek pila)))

(defn pop-seguro [pila] 
  (if (empty? pila)pila (pop pila)))

(defn ejecutar-comando [comando pila]
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
  (loop [x 0 y 0 dir "R" resultado [] pila [] string-mode? false comandos comandos]
    (let [comando (obtener-comando comandos x y)
          prox-dir (case comando
                     \< "L"
                     \> "R"
                     \^ "U"
                     \v "D"
                     \? (random-dir)
                     dir)
          pila (if string-mode?
                 (if (= comando \") pila (conj pila (int comando))) ; Apila caracteres literales si no es `"`.
                 (cond
                   ;; Apilar si el carácter es un dígito
                   (Character/isDigit comando) (conj pila (Character/digit comando 10))
                   ;; Ejecutar comando si es un operador
                   (#{\+ \- \* \/ \% \$ \:} comando) (ejecutar-comando comando pila)
                   :else pila))]

      (cond
        ;; Cambiar estado del `string-mode` cuando se encuentra `"`.
        (= comando \")
        (case dir
          "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado pila (not string-mode?) comandos)
          "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado pila (not string-mode?) comandos)
          "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado pila (not string-mode?) comandos)
          "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado pila (not string-mode?) comandos))

        ;; Comando `!` (ignorado en `string-mode`)
        (and (not string-mode?) (= comando \!))
        (let [nueva-pila (conj (pop-seguro pila) (if (zero? (peek-seguro pila)) 1 0))]
          (case dir
            "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado nueva-pila string-mode? comandos)
            "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado nueva-pila string-mode? comandos)
            "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado nueva-pila string-mode? comandos)
            "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado nueva-pila string-mode? comandos)))
        (= comando \@)
          (do
            (println (apply str resultado)))

        ;; Continuar movimiento sin ejecutar comandos adicionales.
        :else
        (do
                ;;mini debugger de la pila xD
                (println "Comando:" comando " | " "Pila actual:" pila " | " "String mode:" string-mode?)
                (Thread/sleep 300)
                (case prox-dir
                  "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado pila string-mode? comandos)
                  "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado pila string-mode? comandos)
                  "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado pila string-mode? comandos)
                  "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado pila string-mode? comandos)))))))
  


(def comandos (procesar-archivo "hello2.bf"))
(recorrer comandos)                  

(defn -main [& args]
    (let [entrada (first args)
    comandos (procesar-archivo entrada)]
    (recorrer comandos)
    )
)      