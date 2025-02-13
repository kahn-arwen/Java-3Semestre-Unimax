import java.util.Scanner;

public class Media {
    public static void main(String[] args) {
        Entrada entrada = new Entrada();
        Processamento processamento = new Processamento();
        Saida saida = new Saida();

        double nota1 = entrada.lerNota1();
        double nota2 = entrada.lerNota2();
        double nota3 = entrada.lerNota3();

    }

    public double lerNota1(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a Primeira Nota:");
        return scanner.nextDouble();

    }

    public double lerNota2(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a Segunda Nota:");
        return scanner.nextDouble();

    }

    public double lerNota3(){
        Scanner scanner = new Scanner(System.in);
        System.out.print("Digite a Terceira Nota:");
        return scanner.nextDouble();

    }
    class Processamento{
        public double calcularMedia(double nota1, double nota2, double nota3){
            return ((nota1, nota2, nota3)/3);
        }
    }

    class Saida{
        public void exibirResultado(double media){
            System.out.println("A média das notas é: "+ media);
        }
    }

}
