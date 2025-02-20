import java.util.Scanner;

public class NotasAula {

    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        System.out.println("Digite a nota 1:");
        double nota1 = teclado.nextDouble();

        System.out.println("Digite a nota 2:");
        double nota2 = teclado.nextDouble();

        double media = (nota1+nota2)/2;
        System.out.println("A média das notas é: " + media);

    }

    
}
