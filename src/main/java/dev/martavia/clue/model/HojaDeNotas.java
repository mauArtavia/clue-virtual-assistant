package dev.martavia.clue;

/**
 * Clase Hoja de Notas del programa, el cual lleva las matrices de los objetos.
 * @author Mauricio Artavia Monge | C10743
 */
public class HojaDeNotas {
    // =-=-= Declaracion de variables tipo double[] en estado privado =-=-= \\
    private double[][] posibilitiesStats;
    
    // =-=-= Declaracion de variables tipo int en estado privado =-=-= \\
    private int columns;
    private int rows;
    
    // =-=-= Declaracion de variables tipo String[] en estado privado =-=-= \\
    private String[][] posibilities;
    
    // =-=-= Metodo Constructor =-=-= \\
    /**
     * Metodo constructor de la clase HojaDeNotas.
     * @param columns Numero de columnas de la matriz.
     * @param rows Numero de filas de la matriz.
     */
    public HojaDeNotas(int columns, int rows) {
        this.columns = columns+2;
        this.rows = rows+1;
        this.posibilities = new String[rows+1][columns+2];
    }
    
    // =-=-= Constructores de Matriz =-=-= \\
    /**
     * Metodo que crea la matriz principal (Strings).
     * @param playersList Lista de jugadores.
     * @param cardList Lista de cartas.
     */
    public void createMatriz(String[] playersList, String[] cardList) {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (row == 0 && col == 0) {
                    this.posibilities[row][col] = "";
                } else if (row != 0 && col == 0) {
                    this.posibilities[row][col] = cardList[row-1];
                } else if (col == (columns-1) && row == 0) {
                    this.posibilities[row][col] = "Sobre";
                } else if (col != 0 && row == 0) {
                    this.posibilities[row][col] = playersList[col-1];
                } else {
                    this.posibilities[row][col] = ("3.0");
                }
            }
        }
    }
    
    /**
     * Metodo que me crea la matriz secundaria donde se hacen los calculos (doubles).
     */
    public void createDoubleMatriz() {
        int contador, divisor;
        this.posibilitiesStats = new double[this.rows+1][this.columns+2];
        
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                if (row == 0 && col == 0) {
                    this.posibilitiesStats[row][col] = 2.0;
                } else if (row != 0 && col == 0) {
                    this.posibilitiesStats[row][col] = 2.0;
                } else if (col == (columns-1) && row == 0) {
                    this.posibilitiesStats[row][col] = 2.0;
                } else if (col != 0 && row == 0) {
                    this.posibilitiesStats[row][col] = 2.0;
                } else {
                    this.posibilitiesStats[row][col] = Double.parseDouble(this.posibilities[row][col]);
                }
            }
        }
        for (int fila = 1; fila < this.rows; fila++) {
            contador = 0;
            for (int col = 1; col < this.columns; col++) {
                if (this.posibilitiesStats[fila][col] != 0.0 && this.posibilitiesStats[fila][col] != 1.0) {
                    contador++;
                }
            }
            divisor = 100/contador;
            for (int col = 1; col < this.columns; col++) {
                if (this.posibilitiesStats[fila][col] != 0.0 && this.posibilitiesStats[fila][col] != 1.0) {
                    posibilitiesStats[fila][col] = divisor/100.;
                }
            }
        }
    }
    
    /**
     *  Metodo que convierte la matriz de doubles en la matriz de Strings.
     */
    public void convertDoubleString() {
        for (int row = 1; row < this.rows; row++) {
            for (int col = 1; col < this.columns; col++) {
                this.posibilities[row][col] = Double.toString(this.posibilitiesStats[row][col]);
            }
        }
    }
    
    /**
     * Metodo que se encarga de recorrer la matriz e imprimirla.
     */
    public void reviewMatriz() {
        for (int row = 0; row < this.rows; row++) {
            for (int col = 0; col < this.columns; col++) {
                System.out.print(this.posibilities[row][col] + "\t");
            }
            System.out.print("\n");
        }
    }
    
    
    
    // =-=-= Generadores de Preguntas =-=-= \\
    /**
     * Metodo que da una opcion completamente aleatoria para preguntar.
     * @return Una posible respuesta.
     */
    public String aleatoryQuestion() {
        int row;
        String answer;
        
        do {
            row = (int) (Math.random() * this.rows);
        } while (row == 0);
        answer = this.posibilities[row][0];
        return answer;
    }
    
    /**
     * Metodo que me da una opcion parcialmente aleatoria para preguntar. Esta se formulara siempre y cuando no se sepa quien tiene dicha carta.
     * @return Una posible respuesta.
     */
    public String partialAleatoryQuestion() {
        int col, row;
        String answer;
        
        do {
            row = (int) (Math.random() * this.rows);
            col = (this.columns-1);
            if (row == 0) {
                row = 0;
                col = 0;
            }
            if (this.posibilities[row][col].equals("0.0")) {
                row = 0;
                col = 0;
            }
        } while (row == 0 && col == 0);
        answer = this.posibilities[row][0];
        return answer;
    }
    
    /**
     * Metodo que da una opcion con estrategia avanzada para preguntar.
     * @return Una posible pregunta.
     */
    public String advanceStrategyQuestion() {
        boolean value1Found = false;
        int col, row;
        String answer = "";
        
        col = (this.columns-1);
        
        for (int r = 1; r < this.rows; r++) {
            if (this.posibilities[r][col].equals("1.0")) {
                row = r;
                answer = this.posibilities[row][0];
                value1Found = true;
                break;
            }
        }
        
        if (!value1Found) {
            do {
                row = (int) (Math.random() * this.rows);
                if (this.posibilities[row][col].equals("0.0")) {
                    row = 0;
                    col = 0;
                }
            } while (row == 0 && col == 0);
            answer = this.posibilities[row][0];
        }
        return answer;
    }
    
    /**
     * Metodo que da una opcion con la estrategia ideal para preguntar.
     * @return Una posible pregunta.
     */
    public String idealStrategyQuestion() {
        int row = 0;
        int col = (this.columns-1);
        double initialValue = 0.0;
        String answer = "";
        
        for (int r = 1; r < this.rows; r++) {
            if (this.posibilitiesStats[r][col] > initialValue) {
                initialValue = this.posibilitiesStats[r][col];
                row = r;
            }
        }
        answer = this.posibilities[row][0];
        return answer;
    }
    
    
    
    // =-=-= Analizadores & Modificadores de Matriz =-=-= \\
    /**
     * Verifica cuantas cartas de cierto tipo tiene un jugador.
     * @param playerID Posicion en matriz del jugador.
     * @return Cantidad de cartas.
     */
    public int verifyPlayerCards(int playerID) {
        int col = playerID+1;
        int counter = 0;
        
        for (int row = 1; row < this.rows; row++) {
            if (this.posibilitiesStats[row][col] == 1.0) {
            counter++;
            }
        }
        return counter;
    }
    
    /**
     * Metodo que verifica si ya puedo averiguar que trae el sobre.
     * @return Una posible solucion.
     */
    public String knownEnvelope() {
        boolean canReturn = false;
        int objectPos = 0;
        String answer = "";
        
        for (int row = 1; row < this.rows; row++) {
            if (this.posibilities[row][this.columns-1].equals("1.0")) {
                objectPos = row;
                canReturn =true;
                break;
            }
        }
        if (canReturn) {
            answer = this.posibilities[objectPos][0];
        } else {
           answer = "{DESCONOCIDO}";
        }
        return answer;
    }
    
    /**
     * Añade y actualiza las cartas que han sido preguntadas.
     * @param askedCardID Ubicacion en matriz de la carta.
     * @param askedPlayerID Ubicacion en matriz del jugador al que se le pregunta.
     * @param askingStatus Boolean de que si tiene o no la carta.
     */
    public void addAskedCards(int askedCardID, int askedPlayerID, boolean askingStatus) {
        if (askingStatus) {
            for (int col = 1; col < this.columns; col++) {
                if (col == askedPlayerID+1) {
                    this.posibilitiesStats[askedCardID][col] = 1.0;
                } else {
                    if (this.posibilitiesStats[askedCardID][col] != 1.0) {
                        this.posibilitiesStats[askedCardID][col] = 0.0;
                    }
                }
            }
        } else {
            this.posibilitiesStats[askedCardID][askedPlayerID+1] = 0.0;
        }
        this.convertDoubleString();
    }
    
    /**
     * Metodo que se encarga de anular las opciones de cartas publicas.
     * @param index Ubicacion en matriz de la carta publica.
     */
    public void addPublicCards(int index) {
        for (int col = 1; col < this.columns; col++) {
            this.posibilitiesStats[index+1][col] = 0.0;
            this.convertDoubleString();
        }
    }

    /**
     * Añade y actualiza las cartas del usuario en matriz.
     * @param index Ubicacion en matriz de la carta.
     * @param userID Ubicacion en matriz del usuario.
     */
    public void addUserCards(int index, int userID) {
        for (int row = 1; row < this.rows; row++) {
            for (int col = 1; col < this.columns; col++) {
                if (col == userID+1 && row == index) {
                    this.posibilitiesStats[row][col] = 1.0;
                } else if (col == userID+1 && row != index) {
                    if (this.posibilitiesStats[row][col] != 1.0) {
                        this.posibilitiesStats[row][col] = 0.0;
                    }
                } else if (row == index && col != userID+1) {
                    this.posibilitiesStats[row][col] = 0.0;
                }
            }
        }
        this.analizeUserCards(userID+1);
        this.convertDoubleString();
    }
    
    /**
     * Analiza y actualiza las cartas del sobre.
     */
    public void analizeEnvelope() {
        int col = (this.columns-1);
        int counter = 0;
        
        for (int row = 1; row < this.rows; row++) {
            if (this.posibilitiesStats[row][col] == 0.0) {
                counter++;
            }
        }
        if (counter == this.rows-2) {
            for (int row = 1; row < this.rows; row++) {
                if (this.posibilitiesStats[row][col] != 0.0) {
                    this.posibilitiesStats[row][col] = 1.0;
                    for (int column = 1; column < this.columns-1; column++) {
                        this.posibilitiesStats[row][column] = 0.0;
                    }
                }
            }
        }
        this.convertDoubleString();
    }
    
    /**
     * Analiza las cartas del jugador, y les asigna sus posibilidades.
     * @param userID Ubicacion en matriz del usuario.
     */
    public void analizeUserCards(int userID) {
        int col = userID;
        for (int row = 1; row < this.rows; row++) {
            if (this.posibilitiesStats[row][col] != 1.0) {
                this.posibilitiesStats[row][col] = 0.0;
            }
        }
    }
    
    /**
     * Actualiza las cartas conocidas de cierto jugador.
     * @param playerID Posicion en matriz del jugador.
     */
    public void knownPlayerCards(int playerID) {
        int col = playerID+1;
        
        for (int row = 1; row < this.rows; row++) {
            if (this.posibilitiesStats[row][col] != 1.0) {
                this.posibilitiesStats[row][col] = 0.0;
            }
        }
        this.updatePosibilities();
        this.convertDoubleString();
    }
    
    /**
     * Actualiza las posibilidades de la matriz.
     */
    public void updatePosibilities() {
        int counter;
        double divisor = 100;
        
        for (int fila = 1; fila < this.rows; fila++) {
            counter = 0;
            for (int col = 1; col < this.columns; col++) {
                if (this.posibilitiesStats[fila][col] != 0.0 && this.posibilitiesStats[fila][col] != 1.0) {
                   counter++;
                }
            }
            if (counter > 0) {
                divisor = 100/counter;
            }
            for (int col = 1; col < this.columns; col++) {
                if (this.posibilitiesStats[fila][col] != 0.0 && this.posibilitiesStats[fila][col] != 1.0) {
                    this.posibilitiesStats[fila][col] = divisor/100;
                }
            }
        }
    }
}