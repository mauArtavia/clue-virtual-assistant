package dev.martavia.clue.model;

import java.util.Scanner;

/**
 * Clase Partida del programa la cual controla el juego.
 * 
 * @author Mauricio Artavia Monge.
 */
public class Partida {
    // =-=-= Instanciacion de Scanner =-=-= \\
    private Scanner scan = new Scanner(System.in);

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
     * Metodo encargado de la interacion directa con el usuario y manejo de los
     * distintos metodos
     * del programa.
     */
    public void game() {
        // =-=-= Inicio del programa =-=-= \\
        System.out.println("=== Bienvenid@ al Asitente Virtual CLUE, el clásico juego de misterio ===");

        // =-=-= Ingreso de datos de los jugadores participantes =-=-= \\
        this.players();

        // =-=-= Ingreso de datos de usuario =-=-= \\
        this.user();

        // =-=-= Ingreso y creacion de las cartas del juego =-=-= \\
        this.cardLists();

        // =-=-= Construccion de matrices =-=-= \\
        this.createMatriz();

        // =-=-= Calculo de la cantidad de cartas publicas =-=-= \\
        this.publicCardsList();

        // =-=-= Ingreso de las cartas del usuario =-=-= \\
        this.userCards();

        // =-=-= Inicio del Asistente Virtual CLUE =-=-= \\
        System.out.println("=== EMPEZAMOS ===");

        // =-=-= Impresion inicial de la Matriz =-=-= \\
        this.printMatriz();

        // =-=-= Menu De Juego =-=-= \\
        this.menu();
    }

    // =-=-= Metodos incluidos en game =-=-= \\
    /**
     * Metodo que se encarga de pedir el nombre de todos los jugadores.
     */
    private void players() {
        boolean canPlay = false;
        String enterPlayers;

        while (!canPlay) {
            System.out.print("Por favor, ingrese el nombre de cada uno de los jugadores, separados por un guion (-): ");

            enterPlayers = scan.nextLine();
            this.playersList = enterPlayers.split("-");

            if (this.playersList.length >= 3 && this.playersList.length <= 6) {
                canPlay = true;
            } else {
                System.out.println("=== ALTO!!! El juego esta pensado para jugarse entre 2 a 6 jugadores, por favor, "
                        + "digite una cantidad de nombres valida ===");
            }
        }
    }

    /**
     * Metodo que se encarga de pedir el nombre del usuario.
     */
    private void user() {
        boolean found = false;
        String enterUser;

        while (!found) {
            System.out.print("Ingrese su nombre: ");

            enterUser = scan.nextLine();

            for (int i = 0; i < this.playersList.length; i++) {
                if (this.playersList[i].equals(enterUser)) {
                    found = true;
                    userID = i;
                    break;
                }
            }

            if (!found) {
                System.out.println("=== Usted ha ingresado un nombre no valido. Por favor, digite su nombre tal y "
                        + "como lo ingreso en la lista de jugadores ===");
            }
        }
    }

    /**
     * Metodo que se encarga de construir las listas de cartas.
     */
    private void cardLists() {
        String enterRooms;
        String enterSuspects;
        String enterWeapons;

        // =-=-= Pregunta por las cartas de armas para ser agregadas a la lista de Armas
        // =-=-= \\
        System.out.print("Ingrese las cartas de armas, separadas por guion (-): ");

        enterWeapons = scan.nextLine();
        this.weaponsList = enterWeapons.split("-");

        // =-=-= Pregunta por las cartas de sospechosos para ser agregadas a la lista de
        // Sospechosos =-=-= \\
        System.out.print("Ingrese las cartas de sospechosos, separadas por guion (-): ");

        enterSuspects = scan.nextLine();
        this.suspectsList = enterSuspects.split("-");

        // =-=-= Pregunta por las cartas de habitaciones para ser agregadas a la lista
        // de Habitaciones =-=-= \\
        System.out.print("Ingrese las cartas de habitaciones, separadas por guion(-): ");

        enterRooms = scan.nextLine();
        this.roomsList = enterRooms.split("-");
    }

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
     * Metodo que se encarga de construir la lista de cartas publicas.
     */
    private void publicCardsList() {
        String enterPublicCards;
        String[] addPublicCards;

        this.publicCardsAmount = (((this.weaponsList.length) + (this.suspectsList.length) + (this.roomsList.length))
                % (playersList.length));

        System.out.println("=== Tenemos un total de " + this.publicCardsAmount + " cartas publicas ===");

        if (this.publicCardsAmount != 0) {
            this.publicCards = new String[this.publicCardsAmount];

            for (int i = 0; i < this.publicCardsAmount; i++) {
                System.out.print("Por favor, digite la carta publica #" + (i + 1) + " : ");

                enterPublicCards = scan.nextLine();
                addPublicCards = enterPublicCards.split("-");
                this.publicCards[i] = addPublicCards[0];
            }

            this.addPublicCards();
        }
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
     * Metodo que se encarga de construir la lista de cartas del jugador.
     */
    private void userCards() {
        this.correctAmountCards = ((((this.weaponsList.length) + (this.suspectsList.length)
                + (this.roomsList.length)) - this.publicCardsAmount) - 3) / this.playersList.length;

        do {
            System.out.print("Ingrese sus cartas (" + (this.correctAmountCards) + "), separadas por un guion (-): ");

            String enterUserCards = scan.nextLine();

            this.playerCardList = enterUserCards.split("-");

            if (this.playerCardList.length != this.correctAmountCards) {
                System.out.print("=== Me parece que has ingresado una cantidad invalida de cartas, deberias de tener "
                        + (this.correctAmountCards) + " cartas en total ===");
            }

        } while (this.playerCardList.length != correctAmountCards);

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
     * Metodo que se encarga de imprimir las matrices.
     */
    private void printMatriz() {
        this.verifyPlayerCards();
        this.analizeEnvelope();

        System.out.println("=-=-=\nARMAS\n=-=-=");

        this.weapons.reviewMatriz();

        System.out.println("=-=-=-=-=-=\nSOSPECHOSOS\n=-=-=-=-=-=");

        this.suspects.reviewMatriz();

        System.out.println("=-=-=--=-=-=\nHABITACIONES\n=-=-=--=-=-=");

        this.rooms.reviewMatriz();
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
     * Metodo que se encarga de manejar el menu interactivo del programa.
     */
    private void menu() {
        boolean stayPlaying = true;
        int optionPlay;

        while (stayPlaying) {
            try {
                System.out.println(
                        "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\nQue se conoce del asesino hasta el momento?\nArma: "
                                + this.weapons.knownEnvelope() + "\nSospechoso: "
                                + this.suspects.knownEnvelope() + "\nHabitacion: "
                                + this.rooms.knownEnvelope() + "\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

                System.out.print("Que esta sucediendo?"
                        + "\n[1] Tengo Informacion."
                        + "\n[2] Es mi turno."
                        + "\n[3] Quiero Salir."
                        + "\n -> ");

                optionPlay = Integer.parseInt(scan.nextLine());

                if (optionPlay == 1) {
                    this.getInfo(false);
                    this.printMatriz();
                } else if (optionPlay == 2) {
                    boolean correctQuestion = false;
                    String questionType;

                    while (!correctQuestion) {
                        System.out.print("Que tipo de pregunta desea hacer?"
                                + "\n[A] Pregunta Totalmente al Azar."
                                + "\n[B] Pregunta Parcialmente al Azar."
                                + "\n[C] Pregunta con Estrategia Avanzada."
                                + "\n[D] Pregunta con Estrategia Ideal."
                                + "\n -> ");

                        questionType = scan.nextLine();

                        if (questionType.equalsIgnoreCase("A")) {
                            System.out.println("Podria preguntar por: "
                                    + (this.weapons.aleatoryQuestion()) + ", "
                                    + (this.suspects.aleatoryQuestion()) + " & "
                                    + (this.rooms.aleatoryQuestion()) + ".");

                            this.getInfo(true);

                            correctQuestion = true;
                        } else if (questionType.equalsIgnoreCase("B")) {
                            System.out.println("Podria preguntar por: "
                                    + (this.weapons.partialAleatoryQuestion()) + ", "
                                    + (this.suspects.partialAleatoryQuestion()) + " & "
                                    + (this.rooms.partialAleatoryQuestion()) + ".");

                            this.getInfo(true);

                            correctQuestion = true;
                        } else if (questionType.equalsIgnoreCase("C")) {
                            System.out.println("Podria preguntar por: "
                                    + (this.weapons.advanceStrategyQuestion()) + ", "
                                    + (this.suspects.advanceStrategyQuestion()) + " & "
                                    + (this.rooms.advanceStrategyQuestion()) + ".");

                            this.getInfo(true);

                            correctQuestion = true;
                        } else if (questionType.equalsIgnoreCase("D")) {
                            System.out.println("Podria preguntar por: "
                                    + (this.weapons.idealStrategyQuestion()) + ", "
                                    + (this.suspects.idealStrategyQuestion()) + " & "
                                    + (this.rooms.idealStrategyQuestion()) + ".");

                            this.getInfo(true);

                            correctQuestion = true;
                        } else {
                            System.out.println("=== Usted ha seleccionado una opcion de pregunta invalida, por favor, "
                                    + "digite una opcion valida ===");
                            correctQuestion = false;
                        }
                    }

                    this.printMatriz();
                } else if (optionPlay == 3) {
                    System.out.println("=== Espero el programa le haya sido de suma ayuda. Gracias por usar el "
                            + "Asistente Virtual CLUE ===");

                    stayPlaying = false;
                } else {
                    System.out.println("=== CUIDADO!!! Usted ha digitado una opcion de menu no existente. Por favor, "
                            + "digite una opcion existente ===");

                    stayPlaying = true;
                }
            } catch (NumberFormatException a) {
                System.out.println("=== CUIDADO!!! Usted ha digitado una opcion de menu no existente. Por favor, "
                        + "digite una opcion existente ===");
            }
        }
    }

    // =-=-= Metodos incluidos en menu =-=-= \\
    /**
     * Metodo que permite ingresar informacion obtenida durante la partida.
     * 
     * @param isUserTurn Indica si es el turno del usuario (true) o de otro jugador
     *                   (false).
     */
    private void getInfo(boolean isUserTurn) {
        boolean askingStatus;
        boolean canContinue = false;
        boolean continueAsking = true;
        boolean correctAmountCards = false;
        boolean found = false;
        boolean haveAnswer = false;
        int askedPlayerID = 0;
        int counter = 0;
        String askedCard;
        String askedPlayer;
        String haveInfo;
        String questionAnswer;

        while (!canContinue) {
            while (!correctAmountCards) {
                if (isUserTurn) {
                    System.out.print(
                            "Digite la combinacion de 3 cartas por la cual ha preguntado, separadas por guion (-): ");
                } else {
                    System.out.print(
                            "Digite la combinacion de 3 cartas por la cual han preguntado, separadas por guion (-): ");
                }

                questionAnswer = scan.nextLine();
                this.askedCards = questionAnswer.split("-");

                if (this.askedCards.length != 3) {
                    System.out.println("=== CUIDADO!! Usted no ha digitado una combinacion de 3 cartas ===");

                    correctAmountCards = false;
                } else {
                    correctAmountCards = true;
                }
            }

            canContinue = this.validateCards();

            if (canContinue) {
                while (!haveAnswer) {
                    found = false;

                    if (counter == this.playersList.length - 1) {
                        found = true;
                        haveAnswer = true;
                        continueAsking = false;
                    }

                    while (!found) {
                        if (isUserTurn) {
                            System.out.print("Por favor, ingrese quien le responde: ");
                        } else {
                            System.out.print("Por favor, ingrese quien responde: ");
                        }

                        askedPlayer = scan.nextLine();

                        for (int i = 0; i < this.playersList.length; i++) {
                            if (this.playersList[i].equals(askedPlayer)) {
                                found = true;
                                askedPlayerID = i;
                                break;
                            }
                        }

                        if (!found) {
                            System.out.println("=== Usted ha ingresado un nombre no valido ===");
                        }
                    }

                    if (continueAsking) {
                        System.out.print("Tenia alguna informacion?\nSI O NO? ");

                        haveInfo = scan.nextLine();

                        if (haveInfo.equalsIgnoreCase("SI")) {
                            if (isUserTurn) {
                                System.out.print("Que carta tiene? ");

                                askedCard = scan.nextLine();

                                this.addAskedCards(askedCard, true, askedPlayerID);
                            }

                            haveAnswer = true;
                            counter = this.playersList.length + 1;
                        } else if (haveInfo.equalsIgnoreCase("NO")) {
                            askingStatus = false;

                            for (int i = 0; i < this.askedCards.length; i++) {
                                this.addAskedCards(this.askedCards[i], askingStatus, askedPlayerID);
                            }

                            haveAnswer = false;
                            counter++;
                            found = false;
                        } else {
                            System.out.println("=== ADVERTENCIA!!! Opcion Invalida, vuelva a intentarlo ===");

                            haveAnswer = false;
                        }
                    }
                }
            } else {
                System.out.println("=== OJO!!! Usted ha digitado cartas con las cuales usted no esta jugando, tenga "
                        + "cuidado y digite cartas existentes ===");
            }
        }
    }

    /**
     * Metodo que valida si existen las cartas que el usuario pregunta o no.
     * 
     * @return Un valor verdadero o falso segun existan las cartas.
     */
    private boolean validateCards() {
        boolean status = false;

        for (int askCard = 0; askCard < this.askedCards.length; askCard++) {
            for (int weaCards = 0; weaCards < this.weaponsList.length; weaCards++) {
                if (this.askedCards[askCard].equals(this.weaponsList[weaCards])) {
                    status = true;
                    break;
                }
            }
            for (int susCards = 0; susCards < this.suspectsList.length; susCards++) {
                if (this.askedCards[askCard].equals(this.suspectsList[susCards])) {
                    status = true;
                    break;
                }
            }
            for (int roomCards = 0; roomCards < this.roomsList.length; roomCards++) {
                if (this.askedCards[askCard].equals(this.roomsList[roomCards])) {
                    status = true;
                    break;
                }
            }
        }
        return status;
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
        this.correctAmountCards = ((((this.weaponsList.length) + (this.suspectsList.length)
                + (this.roomsList.length)) - this.publicCardsAmount) - 3) / this.playersList.length;

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
     * 
     * @param cards Array de 3 cartas a validar.
     * @return true si todas las cartas existen, false si alguna no existe.
     */
    public boolean validateCards(String[] cards) {
        this.askedCards = cards;
        return this.validateCards();
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
}