(ns bf.core2)

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

(defn x-siguiente [x dir]
  (case dir
    "R" (inc x)
    "L" (dec x) ;; Si es Oeste, disminuye la X
    x))        ;; Si es Norte o Sur, la X no cambia

(defn y-siguiente [y dir]
  (case dir
    "U" (dec y) ;; Si la dirección es Norte, disminuye la Y
    "D" (inc y) ;; Si es Sur, aumenta la Y
    y))        ;; Si es Este u Oeste, la Y no cambia

(defn recorrer [comandos]
    (loop [x 0 y 0 dir "R" resultado [] pila [] string-mode? false comandos comandos]
        (let [comando (obtener-comando  comandos x y)
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
          (= comando \")
            (case dir
              "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado pila (not string-mode?) comandos)
              "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado pila (not string-mode?) comandos)
              "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado pila (not string-mode?) comandos)
              "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado pila (not string-mode?) comandos))

          (and (not string-mode?)(= comando \.))
(let [nuevo-resultado (if (not (empty? pila)) 
                        (conj resultado (peek-seguro pila))
                        resultado)
      nueva-pila (if (not (empty? pila))
                   (pop-seguro pila)
                   pila)]
  (case dir
    "L" (recur (if (< (dec x) 0) 79 (dec x)) y dir nuevo-resultado nueva-pila string-mode? comandos)
    "R" (recur (if (> (inc x) 79) 0 (inc x)) y dir nuevo-resultado nueva-pila string-mode? comandos)
    "U" (recur x (if (< (dec y) 0) 24 (dec y)) dir nuevo-resultado nueva-pila string-mode? comandos)
    "D" (recur x (if (> (inc y) 24) 0 (inc y)) dir nuevo-resultado nueva-pila string-mode? comandos)))
          
          (and (not string-mode?)(= comando \,))
          (let [nuevo-resultado (if (not (empty? pila)) 
                        (conj resultado (char (peek-seguro pila)))
                        resultado)
      nueva-pila (if (not (empty? pila))
                   (pop-seguro pila)
                   pila)]
          
  (case dir
    "L" (recur (if (< (dec x) 0) 79 (dec x)) y dir nuevo-resultado nueva-pila string-mode? comandos)
    "R" (recur (if (> (inc x) 79) 0 (inc x)) y dir nuevo-resultado nueva-pila string-mode? comandos)
    "U" (recur x (if (< (dec y) 0) 24 (dec y)) dir nuevo-resultado nueva-pila string-mode? comandos)
    "D" (recur x (if (> (inc y) 24) 0 (inc y)) dir nuevo-resultado nueva-pila string-mode? comandos)))

  

          (and (not string-mode?)(= comando \_))
          (let [nuevo-dir (if (and (not (empty? pila)) (not= (peek-seguro pila) 0)) "L" "R")
            nueva-pila (if (not (empty? pila)) (pop-seguro pila) pila)]
            (case nuevo-dir
              "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado nueva-pila string-mode? comandos)
              "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado nueva-pila string-mode? comandos)))   

          (and (not string-mode?)(= comando \|))
          (let [nuevo-dir (if (and (not (empty? pila)) (not= (peek-seguro pila) 0)) "U" "D")
            nueva-pila (if (not (empty? pila)) (pop-seguro pila) pila)]
            (case nuevo-dir
              "U" (recur x (if (< (dec y) 0) 24 (dec y)) dir resultado nueva-pila string-mode? comandos)
              "D" (recur x (if (> (inc y) 24) 0 (inc y)) dir resultado nueva-pila string-mode? comandos)))     
          
          (and (not string-mode?)(= comando \#))
            (case dir
              "L" (recur (if (< (- x 2) 0) (+ 79 (- x 2)) (- x 2)) y "L" resultado pila string-mode? comandos)
              "R" (recur (if (> (+ x 2) 79) (- (+ x 2) 80) (+ x 2)) y "R" resultado pila string-mode? comandos)
              "U" (recur x (if (< (- y 2) 0) (+ 24 (- y 2)) (- y 2)) "U" resultado pila string-mode? comandos)
              "D" (recur x (if (> (+ y 2) 24) (- (+ y 2) 25) (+ y 2)) "D" resultado pila string-mode? comandos))     

          (and (not string-mode?)(= comando \!))          
          (let [nueva-pila (conj (pop-seguro pila) (if (zero? (peek-seguro pila)) 1 0))]
            (case dir
              "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado nueva-pila string-mode? comandos)
              "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado nueva-pila string-mode? comandos)
              "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado nueva-pila string-mode? comandos)
              "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado nueva-pila string-mode? comandos))) 

          (and (not string-mode?)(= comando \`))
          ;;                                                         / segundo          / primero
          (let [nueva-pila (conj (pop-seguro (pop-seguro pila)) (if (< (peek-seguro pila) (peek-seguro (pop-seguro pila))) 1 0))]
            (case dir
              "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado nueva-pila string-mode? comandos)
              "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado nueva-pila string-mode? comandos)
              "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado nueva-pila string-mode? comandos)
              "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado nueva-pila string-mode? comandos)))

          (and (not string-mode?)(= comando \\))   
          (let [nueva-pila (conj (pop-seguro (pop-seguro pila)) (peek-seguro pila) (peek-seguro (pop-seguro pila)))]
            (case dir
              "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado nueva-pila string-mode? comandos)
              "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado nueva-pila string-mode? comandos)
              "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado nueva-pila string-mode? comandos)
              "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado nueva-pila string-mode? comandos)))
          
          (and (not string-mode?)(= comando \g))
          (let [nueva-pila (let [y (peek-seguro pila) x (peek-seguro (pop-seguro pila))
            valor (if (and (>= x 0)(< x 80)(>= y 0)(< y 25))(int (obtener-comando comandos x y)) 0)] 
            (conj (pop-seguro (pop-seguro pila)) valor))] 
            (case dir
              "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado nueva-pila string-mode? comandos)
              "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado nueva-pila string-mode? comandos)
              "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado nueva-pila string-mode? comandos)
              "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado nueva-pila string-mode? comandos)))

(and (not string-mode?)(= comando \p))
(let [y-nueva (peek-seguro pila) ;; Coordenada Y extraída de la pila
      x-nueva (peek-seguro (pop-seguro pila)) ;; Coordenada X extraída de la pila
      valor (peek-seguro (pop-seguro (pop-seguro pila))) ;; Valor ASCII extraído de la pila
      nueva-pila (pop-seguro (pop-seguro (pop-seguro pila))) ;; Actualización de la pila
      comandos-actualizados
      (if (and (>= x-nueva 0) (< x-nueva 80) (>= y-nueva 0) (< y-nueva 25))
        ;; Escribir en el tablero si las coordenadas son válidas
        (assoc-in comandos [y-nueva x-nueva] (char valor))
        ;; Si no son válidas, no hacer cambios
        comandos)]
  ;; Avanzar el puntero a la siguiente posición
  (recur (x-siguiente x dir)(y-siguiente y dir) dir resultado nueva-pila string-mode? comandos-actualizados))

          (= comando \@)
          (do
            (println (apply str resultado)))

        :else
              (do
                ;;mini debugger de la pila xD
                ;;(println "Comando:" comando " | " "Pila actual:" pila " | " "String mode:" string-mode? " | " "Posicion:" x y)
                (Thread/sleep 0)
                (case prox-dir
                  "L" (recur (if (< (dec x) 0) 79 (dec x)) y "L" resultado pila string-mode? comandos)
                  "R" (recur (if (> (inc x) 79) 0 (inc x)) y "R" resultado pila string-mode? comandos)
                  "U" (recur x (if (< (dec y) 0) 24 (dec y)) "U" resultado pila string-mode? comandos)
                  "D" (recur x (if (> (inc y) 24) 0 (inc y)) "D" resultado pila string-mode? comandos)))))))   


(def comandos (procesar-archivo "maze.bf"))
(recorrer comandos)                  

(defn -main [& args]
    (let [entrada (first args)
    comandos (procesar-archivo entrada)]
    (recorrer comandos)
    )
)