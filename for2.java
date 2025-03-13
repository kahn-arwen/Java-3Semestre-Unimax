import java.util.Scanner;

public class for2 {

    public for2(){ // usa o for qnd sabe que vai parar

    }

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        System.out.println("Digite um valor:");
        int valor = scanner.nextInt();

        for(int i = 0; i < valor; i++){ //contador
            System.out.println(i);
        }

        scanner.close();

    }
    
}
