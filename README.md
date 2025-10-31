# Cafetería usando hilos en Java

En este ejercicio tenemos que hacer una aplicación sobre una cafetería donde tendremos clientes y camareros usando hilos, y teniendo una versión con terminal y otra con interfaz.

## Contenido

* Rama `master` con la aplicación en terminal + documentación.
* Rama `javafx` con la aplicación en JavaFX + documentación.
---

## Ejemplo de salida 

```
--> Entra el cliente Diego.
--> Entra el cliente David.
[Camarero: Roberto] Atendiendo al cliente David.
[Camarero: Nuria] Atendiendo al cliente Diego.
[El cliente: Diego] Se marcho porque tuvo que esperar 300 ms.
[El cliente: David] Recibio su café en 425 ms
--> Entra el cliente Agustín.
[Camarero: Roberto] Atendiendo al cliente Agustín.
[Camarero: Nuria] El cliente Diego se fue de la cola sin su café.
--> Entra el cliente Adriano.
[Camarero: Nuria] Atendiendo al cliente Adriano.
[El cliente: Adriano] Recibio su café en 311 ms
--> Entra el cliente John.
[Camarero: Nuria] Atendiendo al cliente John.
[El cliente: Agustín] Se marcho porque tuvo que esperar 600 ms.
[Camarero: Roberto] El cliente Agustín se fue de la cola sin su café.
[Camarero: Roberto] Atendiendo al cliente Iván.
--> Entra el cliente Iván.
[El cliente: John] Recibio su café en 247 ms
[El cliente: Iván] Recibio su café en 316 ms
FIN DEL SERVICIO: Ya no queda nadie en la cola

