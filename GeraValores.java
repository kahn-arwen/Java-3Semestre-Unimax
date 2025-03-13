import java.util.Random;

public class GeraValores {

    public static void main(String[] args){
        Random random = new Random();
        int valor = random.nextInt(10); //gera um valor aleatório de 0 a 9

        System.out.println("O valor gerado foi: " + valor);

    }

    
}
