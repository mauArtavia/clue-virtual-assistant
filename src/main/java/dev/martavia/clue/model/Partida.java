package dev.martavia.clue.model;

/**
 * Clase Partida del programa la cual controla el juego.
 * 
 * @author Mauricio Artavia Monge.
 */
public class Partida {

    // =-=-= Declaracion de variables tipo int en estado privado =-=-= \\
    private int correctAmountCards;
    private int publicCardsAmount;
    private int userID;

    // =-=-= Declaracion de variables tipo HojaDeNotas en estado privado =-=-= \\
    private HojaDeNotas rooms;
    private HojaDeNotas suspects;
    private HojaDeNotas weapons;

    // =-=-= Declaracion de variables tipo String[] en estado privado =-=-= \\
    private String[] askedCards;
    private String[] playerCardList;
    private String[] playersList;
    private String[] publicCards;
    private String[] roomsList;
    private String[] suspectsList;
    private String[] weaponsList;

    /**
     * Metodo que se encarga de construir las matrices.
     */
    private void createMatriz() {
        // =-=-= Creacion de los Objetos tipo HojaDeNotas de Armas, Sospechosos y
        // Habitaciones =-=-= \\
        this.weapons = new HojaDeNotas((this.playersList.length), (this.weaponsList.length));
        this.suspects = new HojaDeNotas((this.playersList.length), (this.suspectsList.length));
        this.rooms = new HojaDeNotas((this.playersList.length), (this.roomsList.length));

        // =-=-= Creacion de las Matrices tipo String[][] de Armas, Sospechosos y
        // Habitaciones =-=-= \\
        this.weapons.createMatriz(this.playersList, this.weaponsList);
        this.suspects.createMatriz(this.playersList, this.suspectsList);
        this.rooms.createMatriz(this.playersList, this.roomsList);

        // =-=-= Creacion de las Matrices tipo double[][] de Armas, Sospechosos y
        // Habitaciones =-=-= \\
        this.weapons.createDoubleMatriz();
        this.suspects.createDoubleMatriz();
        this.rooms.createDoubleMatriz();

        // =-=-= Conversion de las Matrices tipo double[][] de Armas, Sospechosos y
        // Habitaciones a tipo String[][] =-=-= \\
        this.weapons.convertDoubleString();
        this.suspects.convertDoubleString();
        this.rooms.convertDoubleString();
    }

    /**
     * Metodo que se encarga de eliminar las posibilidades relacionadas a las cartas
     * publicas.
     */
    private void addPublicCards() {
        for (int publicCards = 0; publicCards < this.publicCards.length; publicCards++) {
            int[] result = findCardCategory(this.publicCards[publicCards]);

            if (result == null)
                continue;

            int category = result[0];
            int cardIndex = result[1];

            if (category == 0)
                this.weapons.addPublicCards(cardIndex);
            else if (category == 1)
                this.suspects.addPublicCards(cardIndex);
            else
                this.rooms.addPublicCards(cardIndex);
        }
    }

    /**
     * Metodo que verifica cuantas cartas tiene un jugador.
     */
    private void verifyPlayerCards() {
        int roomsCardsAmount = 0;
        int suspectsCardsAmount = 0;
        int totalCards = 0;
        int weaponsCardsAmount = 0;

        for (int players = 0; players < this.playersList.length; players++) {
            weaponsCardsAmount = this.weapons.verifyPlayerCards(players);
            suspectsCardsAmount = this.suspects.verifyPlayerCards(players);
            roomsCardsAmount = this.rooms.verifyPlayerCards(players);

            totalCards = weaponsCardsAmount + suspectsCardsAmount + roomsCardsAmount;

            if (totalCards == this.correctAmountCards) {
                this.weapons.knownPlayerCards(players);
                this.suspects.knownPlayerCards(players);
                this.rooms.knownPlayerCards(players);
            }
        }
    }

    /**
     * Metodo que actualiza a 1.0 si ya solo queda una opcion para el sobre.
     */
    private void analizeEnvelope() {
        this.weapons.analizeEnvelope();
        this.suspects.analizeEnvelope();
        this.rooms.analizeEnvelope();
    }

    /**
     * Metodo que actualiza la lista de cartas del jugador al que se pregunta.
     */
    private void addAskedCards(String askedCard, boolean askingStatus, int askedPlayerID) {
        int[] result = findCardCategory(askedCard);

        if (result == null)
            return;

        int category = result[0];
        int cardIndex = result[1] + 1;

        if (category == 0)
            this.weapons.addAskedCards(cardIndex, askedPlayerID, askingStatus);
        else if (category == 1)
            this.suspects.addAskedCards(cardIndex, askedPlayerID, askingStatus);
        else
            this.rooms.addAskedCards(cardIndex, askedPlayerID, askingStatus);
    }

    /**
     * Busca una carta en las tres listas y retorna su categoria e indice.
     * 
     * @param card Nombre de la carta a buscar.
     * @return int[] donde [0] es la categoria (0=weapons, 1=suspects, 2=rooms)
     *         y [1] es el indice. Null si no se encuentra.
     */
    private int[] findCardCategory(String card) {
        for (int i = 0; i < this.weaponsList.length; i++) {
            if (card.equals(this.weaponsList[i])) {
                return new int[] { 0, i };
            }
        }
        for (int i = 0; i < this.suspectsList.length; i++) {
            if (card.equals(this.suspectsList[i])) {
                return new int[] { 1, i };
            }
        }
        for (int i = 0; i < this.roomsList.length; i++) {
            if (card.equals(this.roomsList[i])) {
                return new int[] { 2, i };
            }
        }
        return null;
    }

    // =-=-= SETTERS =-=-= \\
    /**
     * Asigna la lista de jugadores de la partida.
     * 
     * @param players Array con los nombres de los jugadores.
     */
    public void setPlayers(String[] players) {
        this.playersList = players;
    }

    /**
     * Asigna el ID del jugador usuario.
     * 
     * @param id Posicion del usuario en la lista de jugadores.
     */
    public void setUserID(int id) {
        this.userID = id;
    }

    /**
     * Asigna las listas de cartas del juego.
     * 
     * @param weapons  Array con los nombres de las armas.
     * @param suspects Array con los nombres de los sospechosos.
     * @param rooms    Array con los nombres de las habitaciones.
     */
    public void setCards(String[] weapons, String[] suspects, String[] rooms) {
        this.weaponsList = weapons;
        this.suspectsList = suspects;
        this.roomsList = rooms;
    }

    /**
     * Asigna las cartas publicas de la partida.
     * 
     * @param publicCards Array con los nombres de las cartas publicas.
     */
    public void setPublicCards(String[] publicCards) {
        this.publicCards = publicCards;
    }

    /**
     * Asigna las cartas del usuario.
     * 
     * @param cards Array con los nombres de las cartas del usuario.
     */
    public void setUserCards(String[] cards) {
        this.playerCardList = cards;
    }

    /**
     * Asigna las cartas preguntadas en una ronda.
     * 
     * @param cards Array con los nombres de las cartas preguntadas.
     */
    public void setAskedCards(String[] cards) {
        this.askedCards = cards;
    }

    // =-=-= GETTERS =-=-= \\
    /**
     * Retorna la lista de jugadores.
     * 
     * @return Array con los nombres de los jugadores.
     */
    public String[] getPlayersList() {
        return this.playersList;
    }

    /**
     * Retorna el ID del usuario en la lista de jugadores.
     * 
     * @return Posicion del usuario en la lista de jugadores.
     */
    public int getUserID() {
        return this.userID;
    }

    /**
     * Retorna la cantidad de cartas publicas de la partida.
     * 
     * @return Cantidad de cartas publicas.
     */
    public int getPublicCardsAmount() {
        return this.publicCardsAmount;
    }

    /**
     * Retorna la cantidad correcta de cartas por jugador.
     * 
     * @return Cantidad de cartas que debe tener cada jugador.
     */
    public int getCorrectAmountCards() {
        return this.correctAmountCards;
    }

    /**
     * Retorna el arma conocida del sobre, o DESCONOCIDO si no se sabe.
     * 
     * @return Nombre del arma en el sobre.
     */
    public String getEnvelopeWeapon() {
        return this.weapons.knownEnvelope();
    }

    /**
     * Retorna el sospechoso conocido del sobre, o DESCONOCIDO si no se sabe.
     * 
     * @return Nombre del sospechoso en el sobre.
     */
    public String getEnvelopeSuspect() {
        return this.suspects.knownEnvelope();
    }

    /**
     * Retorna la habitacion conocida del sobre, o DESCONOCIDO si no se sabe.
     * 
     * @return Nombre de la habitacion en el sobre.
     */
    public String getEnvelopeRoom() {
        return this.rooms.knownEnvelope();
    }

    // =-=-= METODOS DE LOGICA =-=-= \\
    /**
     * Busca el ID de un jugador por su nombre.
     * 
     * @param name Nombre del jugador a buscar.
     * @return Posicion del jugador en la lista, o -1 si no existe.
     */
    public int findUserID(String name) {
        for (int i = 0; i < this.playersList.length; i++) {
            if (this.playersList[i].equals(name)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * Inicializa las matrices del juego y calcula la cantidad de cartas publicas.
     */
    public void initializeGame() {
        this.createMatriz();
        this.publicCardsAmount = (((this.weaponsList.length) + (this.suspectsList.length)
                + (this.roomsList.length)) % (this.playersList.length));
    }

    /**
     * Aplica las cartas publicas a las matrices de probabilidades.
     */
    public void applyPublicCards() {
        this.addPublicCards();
    }

    /**
     * Calcula y aplica las cartas del usuario a las matrices de probabilidades.
     */
    public void applyUserCards() {
        for (int userCards = 0; userCards < this.playerCardList.length; userCards++) {
            int[] result = findCardCategory(this.playerCardList[userCards]);

            if (result == null)
                continue;

            int category = result[0];
            int cardIndex = result[1] + 1;

            if (category == 0)
                this.weapons.addUserCards(cardIndex, this.userID);
            else if (category == 1)
                this.suspects.addUserCards(cardIndex, this.userID);
            else
                this.rooms.addUserCards(cardIndex, this.userID);
        }
    }

    /**
     * Actualiza las matrices verificando cartas conocidas y el sobre.
     */
    public void refreshMatriz() {
        this.verifyPlayerCards();
        this.analizeEnvelope();
    }

    /**
     * Valida si un conjunto de cartas existe en las listas del juego.
     * Verifica que haya exactamente una carta por categoria.
     * 
     * @param cards Array de 3 cartas a validar.
     * @return true si existe una arma, un sospechoso y una habitacion validos.
     */
    public boolean validateCards(String[] cards) {
        this.askedCards = cards;
        boolean hasWeapon = false;
        boolean hasSuspect = false;
        boolean hasRoom = false;

        for (String card : cards) {
            int[] result = findCardCategory(card);
            if (result == null)
                continue;

            if (result[0] == 0)
                hasWeapon = true;
            else if (result[0] == 1)
                hasSuspect = true;
            else
                hasRoom = true;
        }

        return hasWeapon && hasSuspect && hasRoom;
    }

    /**
     * Procesa la informacion obtenida en una ronda de preguntas.
     * 
     * @param cards       Array de 3 cartas preguntadas.
     * @param responderID ID del jugador que responde.
     * @param hadInfo     true si el jugador tenia alguna de las cartas.
     * @param knownCard   Nombre de la carta conocida, null si no se sabe cual es.
     */
    public void processInfo(String[] cards, int responderID, boolean hadInfo, String knownCard) {
        this.askedCards = cards;

        if (hadInfo && knownCard != null) {
            this.addAskedCards(knownCard, true, responderID);
        } else if (!hadInfo) {
            for (String card : cards) {
                this.addAskedCards(card, false, responderID);
            }
        }
    }

    /**
     * Retorna una sugerencia de pregunta segun la estrategia indicada.
     * 
     * @param strategy Estrategia a usar: "A" aleatorio, "B" parcial, "C" avanzado,
     *                 "D" ideal.
     * @return Array de 3 cartas sugeridas: [arma, sospechoso, habitacion].
     */
    public String[] getSuggestedQuestion(String strategy) {
        return switch (strategy.toUpperCase()) {
            case "B" -> new String[] {
                    this.weapons.partialAleatoryQuestion(),
                    this.suspects.partialAleatoryQuestion(),
                    this.rooms.partialAleatoryQuestion()
            };
            case "C" -> new String[] {
                    this.weapons.advanceStrategyQuestion(),
                    this.suspects.advanceStrategyQuestion(),
                    this.rooms.advanceStrategyQuestion()
            };
            case "D" -> new String[] {
                    this.weapons.idealStrategyQuestion(),
                    this.suspects.idealStrategyQuestion(),
                    this.rooms.idealStrategyQuestion()
            };
            default -> new String[] {
                    this.weapons.aleatoryQuestion(),
                    this.suspects.aleatoryQuestion(),
                    this.rooms.aleatoryQuestion()
            };
        };
    }

    /**
     * Imprime las matrices de probabilidades de armas, sospechosos y habitaciones.
     */
    public void printMatriz() {
        System.out.println("=-=-=\nARMAS\n=-=-=");
        this.weapons.reviewMatriz();

        System.out.println("=-=-=-=-=-=\nSOSPECHOSOS\n=-=-=-=-=-=");
        this.suspects.reviewMatriz();

        System.out.println("=-=-=--=-=-=\nHABITACIONES\n=-=-=--=-=-=");
        this.rooms.reviewMatriz();
    }

    /**
     * Calcula la cantidad de cartas que debe tener cada jugador.
     */
    public void calculateCorrectAmountCards() {
        this.correctAmountCards = ((((this.weaponsList.length) + (this.suspectsList.length)
                + (this.roomsList.length)) - this.publicCardsAmount) - 3) / this.playersList.length;
    }
}