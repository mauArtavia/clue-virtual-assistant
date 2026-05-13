package dev.martavia.clue.ui;

import java.util.Scanner;

import dev.martavia.clue.model.Partida;

/**
 * Clase encargada de la interaccion con el usuario via consola.
 * Orquesta el flujo del juego comunicandose con Partida.
 * 
 * @author Mauricio Artavia Monge.
 */
public class ConsoleUI {

    private Scanner scan = new Scanner(System.in);
    private Partida partida = new Partida();

    /**
     * Inicia y orquesta el flujo completo del juego.
     */
    public void start() {
        System.out.println("=== Bienvenid@ al Asistente Virtual CLUE, el clasico juego de misterio ===");

        setupPlayers();
        setupUser();
        setupCards();
        setupPublicCards();
        setupUserCards();

        System.out.println("=== EMPEZAMOS ===");

        printMatriz();
        menu();
    }

    /**
     * Solicita y valida los nombres de los jugadores.
     */
    private void setupPlayers() {
        boolean canPlay = false;

        while (!canPlay) {
            System.out.print("Por favor, ingrese el nombre de cada uno de los jugadores, separados por un guion (-): ");

            String[] players = scan.nextLine().split("-");

            if (players.length >= 2 && players.length <= 6) {
                partida.setPlayers(players);
                canPlay = true;
            } else {
                System.out.println("=== ALTO!!! El juego esta pensado para jugarse entre 2 a 6 jugadores, por favor, "
                        + "digite una cantidad de nombres valida ===");
            }
        }
    }

    /**
     * Solicita y valida el nombre del usuario dentro de la lista de jugadores.
     */
    private void setupUser() {
        boolean found = false;

        while (!found) {
            System.out.print("Ingrese su nombre: ");

            String name = scan.nextLine();
            int id = partida.findUserID(name);

            if (id != -1) {
                partida.setUserID(id);
                found = true;
            } else {
                System.out.println("=== Usted ha ingresado un nombre no valido. Por favor, digite su nombre tal y "
                        + "como lo ingreso en la lista de jugadores ===");
            }
        }
    }

    /**
     * Solicita las listas de cartas del juego, o usa las estandar de CLUE.
     */
    private void setupCards() {
        System.out.print("Desea usar las cartas estandar del juego clasico de CLUE? (SI/NO): ");
        String response = scan.nextLine();

        if (response.equalsIgnoreCase("SI")) {
            String[][] defaultCards = partida.getDefaultCards();
            partida.setCards(defaultCards[0], defaultCards[1], defaultCards[2]);
        } else {
            System.out.print("Ingrese las cartas de armas, separadas por guion (-): ");
            String[] weapons = scan.nextLine().split("-");

            System.out.print("Ingrese las cartas de sospechosos, separadas por guion (-): ");
            String[] suspects = scan.nextLine().split("-");

            System.out.print("Ingrese las cartas de habitaciones, separadas por guion (-): ");
            String[] rooms = scan.nextLine().split("-");

            partida.setCards(weapons, suspects, rooms);
        }

        partida.initializeGame();
    }

    /**
     * Solicita las cartas publicas si las hay.
     */
    private void setupPublicCards() {
        int publicCardsAmount = partida.getPublicCardsAmount();

        System.out.println("=== Tenemos un total de " + publicCardsAmount + " cartas publicas ===");

        if (publicCardsAmount != 0) {
            String[] publicCards = new String[publicCardsAmount];

            for (int i = 0; i < publicCardsAmount; i++) {
                System.out.print("Por favor, digite la carta publica #" + (i + 1) + " : ");
                publicCards[i] = scan.nextLine().split("-")[0];
            }

            partida.setPublicCards(publicCards);
            partida.applyPublicCards();
        }
    }

    /**
     * Solicita y valida las cartas del usuario.
     */
    private void setupUserCards() {
        partida.calculateCorrectAmountCards();
        int correctAmount = partida.getCorrectAmountCards();

        String[] cards;

        do {
            System.out.print("Ingrese sus cartas (" + correctAmount + "), separadas por un guion (-): ");

            cards = scan.nextLine().split("-");

            if (cards.length != correctAmount) {
                System.out.print("=== Me parece que has ingresado una cantidad invalida de cartas, deberias de tener "
                        + correctAmount + " cartas en total ===");
            }

        } while (cards.length != correctAmount);

        partida.setUserCards(cards);
        partida.applyUserCards();
    }

    /**
     * Refresca y imprime las matrices de probabilidades.
     */
    private void printMatriz() {
        partida.refreshMatriz();
        partida.printMatriz();
    }

    /**
     * Maneja el menu interactivo del juego.
     */
    private void menu() {
        boolean stayPlaying = true;

        while (stayPlaying) {
            try {
                System.out.println(
                        "=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=\nQue se conoce del asesino hasta el momento?\nArma: "
                                + partida.getEnvelopeWeapon() + "\nSospechoso: "
                                + partida.getEnvelopeSuspect() + "\nHabitacion: "
                                + partida.getEnvelopeRoom() + "\n=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=-=");

                System.out.print("Que esta sucediendo?"
                        + "\n[1] Tengo Informacion."
                        + "\n[2] Es mi turno."
                        + "\n[3] Quiero Salir."
                        + "\n -> ");

                int optionPlay = Integer.parseInt(scan.nextLine());

                if (optionPlay == 1) {
                    getInfo(false);
                    printMatriz();
                } else if (optionPlay == 2) {
                    boolean correctQuestion = false;

                    while (!correctQuestion) {
                        System.out.print("Que tipo de pregunta desea hacer?"
                                + "\n[A] Pregunta Totalmente al Azar."
                                + "\n[B] Pregunta Parcialmente al Azar."
                                + "\n[C] Pregunta con Estrategia Avanzada."
                                + "\n[D] Pregunta con Estrategia Ideal."
                                + "\n -> ");

                        String strategy = scan.nextLine();

                        if (strategy.equalsIgnoreCase("A") || strategy.equalsIgnoreCase("B")
                                || strategy.equalsIgnoreCase("C") || strategy.equalsIgnoreCase("D")) {
                            String[] suggestion = partida.getSuggestedQuestion(strategy);
                            System.out.println("Podria preguntar por: "
                                    + suggestion[0] + ", "
                                    + suggestion[1] + " & "
                                    + suggestion[2] + ".");

                            getInfo(true);
                            correctQuestion = true;
                        } else {
                            System.out.println("=== Usted ha seleccionado una opcion de pregunta invalida, por favor, "
                                    + "digite una opcion valida ===");
                        }
                    }

                    printMatriz();
                } else if (optionPlay == 3) {
                    System.out.println("=== Espero el programa le haya sido de suma ayuda. Gracias por usar el "
                            + "Asistente Virtual CLUE ===");
                    stayPlaying = false;
                } else {
                    System.out.println("=== CUIDADO!!! Usted ha digitado una opcion de menu no existente. Por favor, "
                            + "digite una opcion existente ===");
                }
            } catch (NumberFormatException a) {
                System.out.println("=== CUIDADO!!! Usted ha digitado una opcion de menu no existente. Por favor, "
                        + "digite una opcion existente ===");
            }
        }
    }

    /**
     * Solicita y procesa la informacion obtenida durante una ronda.
     * 
     * @param isUserTurn Indica si es el turno del usuario (true) o de otro jugador
     *                   (false).
     */
    private void getInfo(boolean isUserTurn) {
        boolean canContinue = false;
        boolean continueAsking = true;
        boolean correctAmountCards = false;
        boolean found = false;
        boolean haveAnswer = false;
        int askedPlayerID = 0;
        int counter = 0;
        String[] askedCards = null;

        while (!canContinue) {
            while (!correctAmountCards) {
                if (isUserTurn) {
                    System.out.print(
                            "Digite la combinacion de 3 cartas por la cual ha preguntado, separadas por guion (-): ");
                } else {
                    System.out.print(
                            "Digite la combinacion de 3 cartas por la cual han preguntado, separadas por guion (-): ");
                }

                askedCards = scan.nextLine().split("-");

                if (askedCards.length != 3) {
                    System.out.println("=== CUIDADO!! Usted no ha digitado una combinacion de 3 cartas ===");
                } else {
                    correctAmountCards = true;
                }
            }

            if (partida.validateCards(askedCards)) {
                canContinue = true;

                while (!haveAnswer) {
                    found = false;

                    if (counter == partida.getPlayersList().length - 1) {
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

                        String responder = scan.nextLine();
                        int responderID = partida.findUserID(responder);

                        if (responderID != -1) {
                            found = true;
                            askedPlayerID = responderID;
                        } else {
                            System.out.println("=== Usted ha ingresado un nombre no valido ===");
                        }
                    }

                    if (continueAsking) {
                        System.out.print("Tenia alguna informacion?\nSI O NO? ");

                        String haveInfo = scan.nextLine();

                        if (haveInfo.equalsIgnoreCase("SI")) {
                            String knownCard = null;

                            if (isUserTurn) {
                                System.out.print("Que carta tiene? ");
                                knownCard = scan.nextLine();
                            }

                            partida.processInfo(askedCards, askedPlayerID, true, knownCard);
                            haveAnswer = true;
                            counter = partida.getPlayersList().length + 1;
                        } else if (haveInfo.equalsIgnoreCase("NO")) {
                            partida.processInfo(askedCards, askedPlayerID, false, null);
                            haveAnswer = false;
                            counter++;
                            found = false;
                        } else {
                            System.out.println("=== ADVERTENCIA!!! Opcion Invalida, vuelva a intentarlo ===");
                        }
                    }
                }
            } else {
                System.out.println("=== OJO!!! Usted ha digitado cartas con las cuales usted no esta jugando, tenga "
                        + "cuidado y digite cartas existentes ===");
                correctAmountCards = false;
            }
        }
    }
}