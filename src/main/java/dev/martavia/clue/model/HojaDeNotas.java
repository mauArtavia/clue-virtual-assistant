package dev.martavia.clue;

/**
 * Clase Hoja de Notas del programa, el cual lleva las matrices de los objetos.
 * 
 * @author Mauricio Artavia Monge | C10743
 */
public class HojaDeNotas {
    // =-=-= Declaracion de variables tipo double[] en estado privado =-=-= \\
    private double[][] possibilitiesStats;

    // =-=-= Declaracion de variables tipo int en estado privado =-=-= \\
    private int columns;
    private int rows;

    // =-=-= Declaracion de variables tipo String[] en estado privado =-=-= \\
    private String[][] possibilities;

    // =-=-= Metodo Constructor =-=-= \\
    /**
     * Metodo constructor de la clase HojaDeNotas.
     * 
     * @param columns Numero de columnas de la matriz.
     * @param rows    Numero de filas de la matriz.
     */
    public HojaDeNotas(int columns, int rows) {
        this.columns = columns + 2;
        this.rows = rows + 1;
        this.possibilities = new String[rows + 1][columns + 2];
    }

    // =-=-= Constructores de Matriz =-=-= \\
    /**
     * Metodo que crea la matriz principal (Strings).
     * 
     * @param playersList Lista de jugadores.
     * @param cardList    Lista de cartas.
     */
    public void createMatriz(String[] playersList, String[] cardList) {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (row == 0 && col == 0) {
                    this.possibilities[row][col] = "";
                } else if (row == 0 && col == this.columns - 1) {
                    this.possibilities[row][col] = "Sobre";
                } else if (row == 0) {
                    this.possibilities[row][col] = playersList[col - 1];
                } else if (col == 0) {
                    this.possibilities[row][col] = cardList[row - 1];
                } else {
                    this.possibilities[row][col] = "3.0";
                }
            }
        }
    }

    /**
     * Metodo que me crea la matriz secundaria donde se hacen los calculos
     * (doubles).
     */
    public void createDoubleMatriz() {
        this.possibilitiesStats = new double[this.rows + 1][this.columns + 2];

        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (row == 0 || col == 0) {
                    this.possibilitiesStats[row][col] = 2.0;
                } else {
                    this.possibilitiesStats[row][col] = Double.parseDouble(this.possibilities[row][col]);
                }
            }
        }

        for (int fila = 1; fila < this.rows; fila++) {
            int contador = 0;

            for (int col = 1; col < this.columns; col++) {
                if (this.possibilitiesStats[fila][col] != 0.0 && this.possibilitiesStats[fila][col] != 1.0) {
                    contador++;
                }
            }

            for (int col = 1; col < this.columns; col++) {
                if (this.possibilitiesStats[fila][col] != 0.0 && this.possibilitiesStats[fila][col] != 1.0) {
                    possibilitiesStats[fila][col] = 1.0 / contador;
                }
            }
        }
    }

    /**
     * Metodo que convierte la matriz de doubles en la matriz de Strings.
     */
    public void convertDoubleString() {
        for (int row = 1; row < this.rows; row++) {
            for (int col = 1; col < this.columns; col++) {
                this.possibilities[row][col] = Double.toString(this.possibilitiesStats[row][col]);
            }
        }
    }

    /**
     * Metodo que se encarga de recorrer la matriz e imprimirla.
     */
    public void reviewMatriz() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                System.out.print(this.possibilities[row][col] + "\t");
            }

            System.out.print("\n");
        }
    }

    // =-=-= Generadores de Preguntas =-=-= \\
    /**
     * Metodo que da una opcion completamente aleatoria para preguntar.
     * 
     * @return Una posible respuesta.
     */
    public String aleatoryQuestion() {
        int row;

        do {
            row = (int) (Math.random() * this.rows);
        } while (row == 0);

        return this.possibilities[row][0];
    }

    /**
     * Metodo que me da una opcion parcialmente aleatoria para preguntar. Esta se
     * formulara siempre y cuando no se sepa quien tiene dicha carta.
     * 
     * @return Una posible respuesta.
     */
    public String partialAleatoryQuestion() {
        int col, row;

        do {
            row = (int) (Math.random() * this.rows);
            col = (this.columns - 1);

            if (row == 0) {
                col = 0;
            }

            if (this.possibilities[row][col].equals("0.0")) {
                row = 0;
                col = 0;
            }
        } while (row == 0 && col == 0);

        return this.possibilities[row][0];
    }

    /**
     * Metodo que da una opcion con estrategia avanzada para preguntar.
     * 
     * @return Una posible pregunta.
     */
    public String advanceStrategyQuestion() {
        boolean value1Found = false;
        int col;
        int row = 0;
        String answer = "";

        col = (this.columns - 1);

        for (int r = 1; r < this.rows; r++) {
            if (this.possibilities[r][col].equals("1.0")) {
                row = r;
                answer = this.possibilities[row][0];
                value1Found = true;

                break;
            }
        }

        if (!value1Found) {
            boolean validFound = false;
            do {
                row = 1 + (int) (Math.random() * (this.rows - 1));

                if (!this.possibilities[row][col].equals("0.0")) {
                    validFound = true;
                }
            } while (!validFound);

            answer = this.possibilities[row][0];
        }

        return answer;
    }

    /**
     * Metodo que da una opcion con la estrategia ideal para preguntar.
     * 
     * @return Una posible pregunta.
     */
    public String idealStrategyQuestion() {
        int row = 0;
        int col = (this.columns - 1);
        double initialValue = 0.0;

        for (int r = 1; r < this.rows; r++) {
            if (this.possibilitiesStats[r][col] > initialValue) {
                initialValue = this.possibilitiesStats[r][col];
                row = r;
            }
        }

        return this.possibilities[row][0];
    }

    // =-=-= Analizadores & Modificadores de Matriz =-=-= \\
    /**
     * Verifica cuantas cartas de cierto tipo tiene un jugador.
     * 
     * @param playerID Posicion en matriz del jugador.
     * @return Cantidad de cartas.
     */
    public int verifyPlayerCards(int playerID) {
        int col = playerID + 1;
        int counter = 0;

        for (int row = 1; row < this.rows; row++) {
            if (this.possibilitiesStats[row][col] == 1.0) {
                counter++;
            }
        }

        return counter;
    }

    /**
     * Metodo que verifica si ya puedo averiguar que trae el sobre.
     * 
     * @return Una posible solucion.
     */
    public String knownEnvelope() {
        for (int row = 1; row < this.rows; row++) {
            if (this.possibilities[row][this.columns - 1].equals("1.0")) {
                return this.possibilities[row][0];
            }
        }

        return "{DESCONOCIDO}";
    }

    /**
     * Añade y actualiza las cartas que han sido preguntadas.
     * 
     * @param askedCardID   Ubicacion en matriz de la carta.
     * @param askedPlayerID Ubicacion en matriz del jugador al que se le pregunta.
     * @param askingStatus  Boolean de que si tiene o no la carta.
     */
    public void addAskedCards(int askedCardID, int askedPlayerID, boolean askingStatus) {
        if (askingStatus) {
            for (int col = 1; col < this.columns; col++) {
                if (col == askedPlayerID + 1) {
                    this.possibilitiesStats[askedCardID][col] = 1.0;
                } else {
                    if (this.possibilitiesStats[askedCardID][col] != 1.0) {
                        this.possibilitiesStats[askedCardID][col] = 0.0;
                    }
                }
            }
        } else {
            this.possibilitiesStats[askedCardID][askedPlayerID + 1] = 0.0;
        }

        this.convertDoubleString();
    }

    /**
     * Metodo que se encarga de anular las opciones de cartas publicas.
     * 
     * @param index Ubicacion en matriz de la carta publica.
     */
    public void addPublicCards(int index) {
        for (int col = 1; col < this.columns; col++) {
            this.possibilitiesStats[index + 1][col] = 0.0;
        }

        this.convertDoubleString();
    }

    /**
     * Añade y actualiza las cartas del usuario en matriz.
     * 
     * @param index  Ubicacion en matriz de la carta.
     * @param userID Ubicacion en matriz del usuario.
     */
    public void addUserCards(int index, int userID) {
        this.possibilitiesStats[index][userID + 1] = 1.0;

        for (int col = 1; col < this.columns; col++) {
            if (col != userID + 1) {
                this.possibilitiesStats[index][col] = 0.0;
            }
        }

        for (int row = 1; row < this.rows; row++) {
            if (row != index && this.possibilitiesStats[row][userID + 1] != 1.0) {
                this.possibilitiesStats[row][userID + 1] = 0.0;
            }
        }

        this.analizeUserCards(userID + 1);
        this.convertDoubleString();
    }

    /**
     * Analiza y actualiza las cartas del sobre.
     */
    public void analizeEnvelope() {
        int col = (this.columns - 1);
        int counter = 0;

        for (int row = 1; row < this.rows; row++) {
            if (this.possibilitiesStats[row][col] == 0.0) {
                counter++;
            }
        }

        if (counter == this.rows - 2) {
            for (int row = 1; row < this.rows; row++) {
                if (this.possibilitiesStats[row][col] != 0.0) {
                    this.possibilitiesStats[row][col] = 1.0;
                    for (int column = 1; column < this.columns - 1; column++) {
                        this.possibilitiesStats[row][column] = 0.0;
                    }
                }
            }
        }

        this.convertDoubleString();
    }

    /**
     * Analiza las cartas del jugador, y les asigna sus posibilidades.
     * 
     * @param userID Ubicacion en matriz del usuario.
     */
    public void analizeUserCards(int userID) {

        for (int row = 1; row < this.rows; row++) {
            if (this.possibilitiesStats[row][userID] != 1.0) {
                this.possibilitiesStats[row][userID] = 0.0;
            }
        }
    }

    /**
     * Actualiza las cartas conocidas de cierto jugador.
     * 
     * @param playerID Posicion en matriz del jugador.
     */
    public void knownPlayerCards(int playerID) {
        int col = playerID + 1;

        for (int row = 1; row < this.rows; row++) {
            if (this.possibilitiesStats[row][col] != 1.0) {
                this.possibilitiesStats[row][col] = 0.0;
            }
        }

        this.updatePossibilities();
        this.convertDoubleString();
    }

    /**
     * Actualiza las posibilidades de la matriz.
     */
    public void updatePossibilities() {
        int counter;

        for (int fila = 1; fila < this.rows; fila++) {
            counter = 0;

            for (int col = 1; col < this.columns; col++) {
                if (this.possibilitiesStats[fila][col] != 0.0 && this.possibilitiesStats[fila][col] != 1.0) {
                    counter++;
                }
            }

            if (counter > 0) {
                for (int col = 1; col < this.columns; col++) {
                    if (this.possibilitiesStats[fila][col] != 0.0 && this.possibilitiesStats[fila][col] != 1.0) {
                        this.possibilitiesStats[fila][col] = 1.0 / counter;
                    }
                }
            }
        }
    }
}