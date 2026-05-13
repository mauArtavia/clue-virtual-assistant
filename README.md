# Asistente Virtual CLUE
**Autor:** Mauricio Artavia Monge  
**Repositorio:** [github.com/mauArtavia/clue-virtual-assistant](https://github.com/mauArtavia/clue-virtual-assistant)

Asistente de consola para el juego de mesa CLUE. Lleva un registro de probabilidades por jugador y carta, y sugiere preguntas estratégicas durante la partida.

---

## Estado actual
El proyecto fue migrado del repo de la universidad a `martavia.dev`, refactorizado con convenciones Java estándar, y limpiado de bugs de lógica conocidos.

### Bugs corregidos
- [X] Bug de división entera en cálculo de probabilidades (`createDoubleMatriz`, `updatePossibilities`)
- [X] Columna "Sobre" nunca se asignaba correctamente en `createMatriz`
- [X] Recursión en `menu()` que podía causar `StackOverflow`
- [X] `convertDoubleString()` se llamaba dentro de loops innecesariamente
- [X] Rango de random incorrecto en `advanceStrategyQuestion`
- [X] Variables redundantes e intermedias eliminadas

---

## Roadmap y TODO

### Fase 1 — Refactoring interno (sin cambiar funcionalidad)
> Objetivo: dejar el código listo para crecer sin deuda técnica.

- [X] **Encapsular métodos internos** — cambiar a `private` todos los métodos que no deben ser accesibles desde fuera de la clase: `players()`, `user()`, `cardLists()`, `createMatriz()`, `publicCardsList()`, `addPublicCards()`, `userCards()`, `printMatriz()`, `verifyPlayerCards()`, `analizeEnvelope()`, `getInfoObtained()`, `getInfoAsked()`, `validateCards()`, `addAskedCards()`
- [X] **Unificar `getInfoObtained()` y `getInfoAsked()`** — ambos métodos son casi idénticos. Refactorizar en un solo método `getInfo(boolean isUserTurn)` que maneje ambos casos
- [X] **Extraer helper de búsqueda de cartas** — el patrón de buscar una carta en las tres listas (`weapons`, `suspects`, `rooms`) se repite en `addPublicCards()`, `userCards()`, `addAskedCards()`. Extraer en un método privado `findCardCategory(String card)`

### Fase 2 — Arquitectura (reorganizar responsabilidades)
> Objetivo: separar la lógica de negocio de la interacción con el usuario.

- [X] **Mover archivos a subpaquetes** — organizar según responsabilidad:
  ```
  dev/martavia/clue/
  ├── App.java
  ├── model/
  │   ├── Partida.java
  │   └── HojaDeNotas.java
  ├── ui/
  └── util/
  ```
- [X] **Separar UI de lógica en `Partida`** — extraer toda interacción con `Scanner` a una clase `ConsoleUI` en el paquete `ui/`. `Partida` solo debería manejar estado del juego, no imprimir ni leer input
- [X] **Eliminar dependencia de `Scanner` en `Partida`** — inyectar el input como parámetro o mediante una interfaz, para facilitar testing futuro

### Fase 3 — Calidad y testing
> Objetivo: verificar que la lógica es correcta y documentar el comportamiento esperado.

- [X] **Escribir tests unitarios para `HojaDeNotas`** — cubrir: `createMatriz`, `createDoubleMatriz`, `addUserCards`, `addAskedCards`, `analizeEnvelope`, `updatePossibilities`
- [X] **Verificar lógica de `validateCards()`** — actualmente retorna `true` si *alguna* de las 3 cartas existe, no si *todas* existen. Revisar si el comportamiento esperado es validar las 3 o al menos 1
- [X] **Revisar condición de `analizeEnvelope()`** — el `rows - 2` es frágil, documentar o extraer como constante con nombre descriptivo

### Fase 4 — Funcionalidad nueva (ideas futuras)
> Objetivo: mejorar la experiencia de uso.

- [X] **Soporte para cartas del juego estándar precargadas** — el usuario puede elegir usar las cartas del CLUE clásico o ingresar las propias al inicio de la partida.

- [ ] **Interfaz gráfica simple** — migrar de consola a una UI básica con Java Swing o JavaFX. Pendiente para una versión futura.

- [ ] **Guardar y cargar partida** — ***descartado***. El asistente está diseñado para usarse en tiempo real durante una partida física, por lo que pausar y retomar no es un caso de uso frecuente ni prioritario. Si se necesita, es más práctico reiniciar el asistente con los datos actuales.

---

## Convenciones del proyecto
- Java 17+, Maven, `dev.martavia.clue`
- Commits en inglés siguiendo [Conventional Commits](https://www.conventionalcommits.org): `feat:`, `fix:`, `refactor:`, `style:`, `chore:`
- Indentación: 4 espacios, máximo 100 caracteres por línea
- Javadoc en todos los métodos públicos