# **EL BEFUNGEHINADOR™**

**Creado por el infame Dr. Doofenshmirtz, con la ayuda de sus dos talentosos asistentes Lautaro Telmo y Leandro Aloisi, el Befungehinador™ ha llegado para revolucionar la programación... o al menos para hacerla más caótica.**

El Dr. Doofenshmirtz, siempre en busca de formas de dominar el área limítrofe, ha decidido incursionar en el mundo de los lenguajes esotéricos. Después de horas de pensar y realizar planos innecesariamente complicados, ha diseñado un artefacto capaz de interpretar Befunge-93, un lenguaje que desafía las leyes de la lógica y las buenas prácticas de programación.

Pero, como en todo buen plan del Dr. Doofenshmirtz, necesitaba ayuda para convertir su idea en realidad. Ahí entramos nosotros, sus brillantes asistentes, para implementar este intérprete en **Clojure**, asegurando que el Befungehinador™ sea lo suficientemente confuso y efectivo para sus maquiavélicos fines.
---

⚠️ **Advertencia:** No nos hacemos responsables si alguien logra conquistar el área limítrofe con él. Cualquier mal uso es responsabilidad exclusiva de su creador.

---

![App Screenshot](https://wallpapercave.com/wp/wp4746788.jpg)

## *UTILIZACION*
Para utilizarlo deberá abrir la consola en el directorio e instalar Java, Clojure y Leiningen en el sistema.

Luego ejecutar los siguientes comandos:

```bash
lein repl
```
Esto iniciara un servidor REPL con clojure y deberá ver por pantalla:

```bash
bf.core=> 
```
Aquí deberá utilizar los distintos comandos:
```bash
(use '[bf.core] :reload)
```
```bash
(def comandos (procesar-archivo "nombre_archivo.bf"))
```
cambiar `nombre_archivo` por el archivo que desea ejecutar.
Finalmente utilizar:
```bash
(recorrer comandos)
```
## *ENUNCIADO DEL TP*

https://algoritmos3ce.github.io/tps/tp2-2024c2/#implementaci%C3%B3n-en-clojure


