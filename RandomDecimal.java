import java.util.Random;
import java.text.DecimalFormat;


public class RandomDecimal {
              
    public static void main(String[] args){
        Random random = new Random();
        
        for (int i = 1; i < 10; i++) {
            double valorAleatorio = random.nextDouble() * 50; //gera um valor aleatório de 0 a 49.99

            //Criendo uma instância de DecimalFormat para formatar o número	
            DecimalFormat df = new DecimalFormat("000.##");

            System.out.println("O valor original foi: " + valorAleatorio);
            System.out.println("O valor formatado foi: " + df.format(valorAleatorio));

        }

       

    }

    
}