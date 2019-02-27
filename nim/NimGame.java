package nim;

import java.util.Scanner;

/**
 * Game of Nim workhorse that provides UI, IO, and prompts both
 * player and agent for their action choices as the game progresses
 */
public class NimGame {
    
    public static final int MAX_REMOVAL = 3, INIT_STATE = 7;
    
    public static void main (String[] args) {
        Scanner input = new Scanner(System.in);
        
        // Game State & Agent Setup:
        NimPlayer nimesis = new NimPlayer(MAX_REMOVAL);
        int remaining = INIT_STATE, taken = 0;
        boolean playersTurn = true;
        
        System.out.println("===============================");
        System.out.println("=       THE GAME OF NIM       =");
        System.out.println("===============================");
        
        // Continue to pull stones as long as there are some remaining
        while (remaining > 0) {
            System.out.println("[!] Remaining: " + remaining);
            
            // Player's turn
            if (playersTurn) {
                System.out.println("  Enter the number you would like to take [1 - " + MAX_REMOVAL + "]");
                System.out.print("  [Player's Turn] > ");
                String taking = input.nextLine();
                taken = Integer.parseInt(taking);
                if (!(taken >= 1 && taken <= MAX_REMOVAL)) {
                    System.out.println("[X] Improper move, l2p. Try again.");
                    continue;
                }
            
            // Agent's turn
            } else {
                taken = nimesis.choose(remaining);
                System.out.println("  [Nimesis' Turn] > " + taken);
            }
            
            remaining -= taken;
            playersTurn = !playersTurn;
        }
        
        System.out.println(playersTurn ? "[L] You Lose!" : "[W] You Win!");
        input.close();
    }

}
