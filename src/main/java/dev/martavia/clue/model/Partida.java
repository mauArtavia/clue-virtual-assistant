package dev.martavia.clue;

import java.util.Scanner;

/**
 * Clase Partida del programa la cual controla el juego.
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
     * Metodo encargado de la interacion directa con el usuario y manejo de los distintos metodos 
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
    public void players() {
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
    public void user() {
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
    public void cardLists() {
        String enterRooms;
        String enterSuspects;
        String enterWeapons;
        
        // =-=-= Pregunta por las cartas de armas para ser agregadas a la lista de Armas =-=-= \\
        System.out.print("Ingrese las cartas de armas, separadas por guion (-): ");
        
        enterWeapons = scan.nextLine();
        this.weaponsList = enterWeapons.split("-");
        
        // =-=-= Pregunta por las cartas de sospechosos para ser agregadas a la lista de Sospechosos =-=-= \\
        System.out.print("Ingrese las cartas de sospechosos, separadas por guion (-): ");
        
        enterSuspects = scan.nextLine();
        this.suspectsList = enterSuspects.split("-");
        
        // =-=-= Pregunta por las cartas de habitaciones para ser agregadas a la lista de Habitaciones =-=-= \\
        System.out.print("Ingrese las cartas de habitaciones, separadas por guion(-): ");
        
        enterRooms = scan.nextLine();
        this.roomsList = enterRooms.split("-");
    }
    
    /**
     * Metodo que se encarga de construir las matrices..
     */
    public void createMatriz() {
        // =-=-= Creacion de los Objetos tipo HojaDeNotas de Armas, Sospechosos y Habitaciones =-=-= \\
        this.weapons = new HojaDeNotas((this.playersList.length), (this.weaponsList.length));
        this.suspects = new HojaDeNotas((this.playersList.length), (this.suspectsList.length));
        this.rooms = new HojaDeNotas((this.playersList.length), (this.roomsList.length));
        
        // =-=-= Creacion de las Matrices tipo String[][] de Armas, Sospechosos y Habitaciones =-=-= \\
        this.weapons.createMatriz(this.playersList, this.weaponsList);
        this.suspects.createMatriz(this.playersList, this.suspectsList);
        this.rooms.createMatriz(this.playersList, this.roomsList);
        
        // =-=-= Creacion de las Matrices tipo double[][] de Armas, Sospechosos y Habitaciones =-=-= \\
        this.weapons.createDoubleMatriz();
        this.suspects.createDoubleMatriz();
        this.rooms.createDoubleMatriz();
        
        // =-=-= Conversion de las Matrices tipo double[][] de Armas, Sospechosos y Habitaciones a tipo String[][] =-=-= \\
        this.weapons.convertDoubleString();
        this.suspects.convertDoubleString();
        this.rooms.convertDoubleString();
    }
    
    /**
     * Metodo que se encarga de construir la lista de cartas publicas.
     */
    public void publicCardsList() {
        String enterPublicCards;
        String[] addPublicCards;
        
        this.publicCardsAmount = (((this.weaponsList.length)+(this.suspectsList.length)+(this.roomsList.length)) 
            % (playersList.length));
        
        System.out.println("=== Tenemos un total de " + this.publicCardsAmount + " cartas publicas ===");
        
        if (this.publicCardsAmount != 0) {
            this.publicCards = new String[this.publicCardsAmount];
            
            for (int i = 0; i < this.publicCardsAmount; i++) {
                System.out.print("Por favor, digite la carta publica #" + (i+1) + " : ");
                
                enterPublicCards = scan.nextLine();
                addPublicCards = enterPublicCards.split("-");
                this.publicCards[i] = addPublicCards[0];
            }

            this.addPublicCards();
        }
    }
    
    /**
     * Metodo que se encarga de eliminar las posibilidades relacionadas a las cartas publicas.
     */
    public void addPublicCards() {
        for (int publicCards = 0; publicCards < this.publicCards.length; publicCards++) {
            for (int weaponCards = 0; weaponCards < this.weaponsList.length; weaponCards++) {
                if (this.publicCards[publicCards].equals(this.weaponsList[weaponCards])) {
                    int index = weaponCards;
                    
                    this.weapons.addPublicCards(index);
                    break;
                }
            }

            for (int suspectsCards = 0; suspectsCards < this.suspectsList.length; suspectsCards++) {
                if (this.publicCards[publicCards].equals(this.suspectsList[suspectsCards])) {
                    int index = suspectsCards;
                    
                    this.suspects.addPublicCards(index);
                    break;
                }
            }

            for (int roomCards = 0; roomCards < this.roomsList.length; roomCards++) {
                if (this.publicCards[publicCards].equals(this.roomsList[roomCards])) {
                    int index = roomCards;
                    
                    this.rooms.addPublicCards(index);
                    break;
                }
            }
        }
    }
    
    /**
     * Metodo que se encarga de construir la lista de cartas del jugador.
     */
    public void userCards() {
        this.correctAmountCards = ((((this.weaponsList.length) + (this.suspectsList.length) 
            + (this.roomsList.length)) - this.publicCardsAmount)-3)/this.playersList.length;
        
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
            for (int weaponsCards = 0; weaponsCards < this.weaponsList.length; weaponsCards++) {
                if (this.playerCardList[userCards].equals(this.weaponsList[weaponsCards])) {
                    int index = weaponsCards+1;

                    this.weapons.addUserCards(index, this.userID);
                    break;
                }
            }

            for (int suspectsCards = 0; suspectsCards < this.suspectsList.length; suspectsCards++) {
                if (this.playerCardList[userCards].equals(this.suspectsList[suspectsCards])) {
                    int index = suspectsCards+1;

                    this.suspects.addUserCards(index, this.userID);
                    break;
                }
            }

            for (int roomCards = 0; roomCards < this.roomsList.length; roomCards++) {
                if (this.playerCardList[userCards].equals(this.roomsList[roomCards])) {
                    int index = roomCards+1;

                    this.rooms.addUserCards(index, this.userID);
                    break;
                }
            }
        }
    }
    
    /**
     * Metodo que se encarga de imprimir las matrices.
     */
    public void printMatriz() {
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
    public void verifyPlayerCards() {
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
    public void analizeEnvelope() {
        this.weapons.analizeEnvelope();
        this.suspects.analizeEnvelope();
        this.rooms.analizeEnvelope();
    }
    
    /**
     * Metodo que se encarga de manejar el menu interactivo del programa.
     */
    public void menu() {
        boolean stayPlaying = true;
        int optionPlay;
        
        
        while (stayPlaying) {
            try {
                System.out.println("=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\nQue se conoce del asesino hasta el momento?\nArma: " 
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
                    this.getInfoObtained();
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
                            
                            this.getInfoAsked();
                            
                            correctQuestion = true;
                        } else if (questionType.equalsIgnoreCase("B")) {
                            System.out.println("Podria preguntar por: "
                                    + (this.weapons.partialAleatoryQuestion()) + ", "
                                    + (this.suspects.partialAleatoryQuestion()) + " & "
                                    + (this.rooms.partialAleatoryQuestion()) + ".");
                            
                            this.getInfoAsked();
                            
                            correctQuestion = true;
                        } else if (questionType.equalsIgnoreCase("C")) {
                            System.out.println("Podria preguntar por: "
                                    + (this.weapons.advanceStrategyQuestion()) + ", "
                                    + (this.suspects.advanceStrategyQuestion()) + " & "
                                    + (this.rooms.advanceStrategyQuestion()) + ".");
                            
                            this.getInfoAsked();
                            
                            correctQuestion = true;
                        } else if (questionType.equalsIgnoreCase("D")) {
                            System.out.println("Podria preguntar por: "
                                    + (this.weapons.idealStrategyQuestion()) + ", "
                                    + (this.suspects.idealStrategyQuestion()) + " & "
                                    + (this.rooms.idealStrategyQuestion()) + ".");
                            
                            this.getInfoAsked();
                            
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
     * Metodo que permite ingresar informacion obtenida por medio de otros usuarios.
     */
    public void getInfoObtained() {
        boolean askingStatus;
        boolean canContinue = false;
        boolean continueAsking = true;
        boolean correctAmountCards = false;
        boolean found = false;
        boolean haveAnswer = false;
        int askedPlayerID = 0;
        int counter = 0;
        String askedCard = "";
        String askedPlayer = "";
        String haveInfo = "";
        String questionAnswer = "";
        
        while (!canContinue) {
            while (!correctAmountCards) {
                System.out.print("Digite la combinacion de 3 cartas por la cual han preguntado, separadas por guion (-): ");
            
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

                    if (counter == this.playersList.length-1) {
                        found = true;
                        haveAnswer = true;
                        continueAsking = false;
                    }

                    while (!found) {
                        System.out.print("Por favor, ingrese quien responde: ");

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
                            haveAnswer = true;
                            counter = this.playersList.length+1;
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
                        this.weapons.convertDoubleString();
                        this.suspects.convertDoubleString();
                        this.rooms.convertDoubleString();
                    }
                }
            } else {
                System.out.println("=== OJO!!! Usted ha digitado cartas con las cuales usted no esta jugando, tenga " 
                    + "cuidado y digite cartas existentes ===");
            }
        }
    }
    
    /**
     * Metodo que me pregunta por la pregunta final que hice, y me pide los datos que me respondieron.
     * Metodo que permite ingresar informacion que el usuario pregunta.
     */
    public void getInfoAsked() {
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
                System.out.print("Digite la combinacion de 3 cartas por la cual ha preguntado, separadas por guion (-): ");
            
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

                    if (counter == this.playersList.length-1) {
                        found = true;
                        haveAnswer = true;
                        continueAsking = false;
                    }

                    while (!found) {
                        System.out.print("Por favor, ingrese quien le responde: ");

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
                            askingStatus = true;

                            System.out.print("Que carta tiene? ");

                            askedCard = scan.nextLine();

                            this.addAskedCards(askedCard, askingStatus, askedPlayerID);
                            haveAnswer = true;
                            counter = this.playersList.length+1;
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
                        this.weapons.convertDoubleString();
                        this.suspects.convertDoubleString();
                        this.rooms.convertDoubleString();
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
     * @return Un valor verdadero o falso segun existan las cartas.
     */
    public boolean validateCards() {
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
    public void addAskedCards(String askedCard, boolean askingStatus, int askedPlayerID) {
        for (int weaponCards = 0; weaponCards < this.weaponsList.length; weaponCards++) {
            if (askedCard.equals(this.weaponsList[weaponCards])) {
                int askedCardID = weaponCards+1;

                this.weapons.addAskedCards(askedCardID, askedPlayerID, askingStatus);
                break;
            }
        }
        for (int suspectsCards = 0; suspectsCards < this.suspectsList.length; suspectsCards++) {
            if (askedCard.equals(this.suspectsList[suspectsCards])) {
                int askedCardID = suspectsCards+1;

                this.suspects.addAskedCards(askedCardID, askedPlayerID, askingStatus);
                break;
            }
        }
        for (int roomCards = 0; roomCards < this.roomsList.length; roomCards++) {
            if (askedCard.equals(this.roomsList[roomCards])) {
                int askedCardID = roomCards+1;

                this.rooms.addAskedCards(askedCardID, askedPlayerID, askingStatus);
                break;
            }
        }
        
    }
    
}