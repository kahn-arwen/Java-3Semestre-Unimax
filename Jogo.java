import java.util.Scanner;
import java.util.Random;

public class Jogo {
    //Sistema gera um valor, o usuário pode configurar o valor gerado no range  necessario
    public static void main(String[] args){
        Random random = new Random();
        Scanner scanner = new Scanner(System.in);
        int valor = random.nextInt(10); //gera um valor aleatório de 0 a 9
        int chute = -1;
        int tentativas = 0;
        System.out.println("Tente adivinhar o valor gerado pelo sistema");
        while (chute != valor) {
            System.out.println("Digite um valor: ");
            chute = scanner.nextInt();
            tentativas++;
            if (chute == valor) {
                System.out.println("Parabéns, você acertou o valor gerado pelo sistema");
                System.out.println("O valor gerado foi: " + valor);
                System.out.println("Você acertou em " + tentativas + " tentativas");
            } else {
                System.out.println("Você errou, tente novamente");
            }
        }
    }
    
}
