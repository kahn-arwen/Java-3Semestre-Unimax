import java.util.Scanner;
import java.util.Random;

public class JogoChute {
    public static void main(String[] args){
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);

        int tentativas = 10;
        int numeroAleatorio = random.nextInt(20);

        System.out.println("\n\nTente adivinhar o número gerado pelo sistema!! \n");
        System.out.println("Você terá 10 tentativas para acertar o número dado");
        System.out.println("Caso você errar, você perderá uma tentativa");
        System.out.println("\nBoa sorte!"); 

        for(int i = 0; i < tentativas; i++){
            System.out.println("Chute um Número: ");
            int chute = scanner.nextInt();

            if(chute == numeroAleatorio){
                System.out.println("Parabéns, você acertou o número gerado pelo sistema");
                System.out.println("O número gerado foi: " + numeroAleatorio);
                System.out.println("Você acertou em " + (i + 1) + " tentativas");
                break;
            } else {
                System.out.println("Você errou, tente novamente!! ");
                System.out.println("Você ainda tem " + (tentativas - (i + 1)) + " tentativas");
            }
        }

    }
    
}
