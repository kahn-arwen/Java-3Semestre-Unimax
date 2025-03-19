import java.util.Scanner;

public class CompraProduto {

    //faça um programe que conste a compra de um produto : Nome, preço, quantidade, seção
    //Calcular : preço total, descontos : se valor total for até 500, sem desconto;
    // se for acima desconto de 4.5%; se a seção for 'eletronicos' mais um desconto de 2%

    public static void main(String[] args) {
     
        String categorias[] = {"eletronicos", "alimentos", "vestuario", "moveis"};
        double tv = 1500.00;
        double celular = 800.00;
        double notebook = 2500.00;
        double tablet = 1200.00;
        double arroz = 10.00;
        double feijao = 8.00;
        double macarrao = 5.00;
        double carne = 20.00;
        double camisa = 50.00;
        double calça = 80.00;
        double sapato = 100.00;
        double meia = 5.00;
        double sofa = 1500.00;
        double cama = 2000.00;
        double mesa = 500.00;
        double cadeira = 150.00;

        String produtosEletronicos[] = {"tv", "celular", "notebook", "tablet"};
        String produtosAlimentos[] = {"arroz", "feijao", "macarrao", "carne"};
        String produtosVestuario[] = {"camisa", "calça", "sapato", "meia"};
        String produtosMoveis[] = {"sofa", "cama", "mesa", "cadeira"};
        double eletronicos[] = {tv, celular, notebook, tablet};
        double alimentos[] = {arroz, feijao, macarrao, carne};
        double vestuario[] = {camisa, calça, sapato, meia};
        double moveis[] = {sofa, cama, mesa, cadeira};

        double precoTotal = 0;
        double desconto = 0;

        while (true) {

            System.out.println("Escolha a categoria dos produtos: ");
            for (int i = 0; i < categorias.length; i++) {
                System.out.println((i + 1) + " - " + categorias[i]);
            }
            System.out.println("0 - Sair");
            Scanner scanner = new Scanner(System.in);
            int opcao = scanner.nextInt();
            if (opcao == 0) {
                break;
            }  
            if (opcao == 1) {
                System.out.println("Escolha o produto: ");
                for (int i = 0; i < produtosEletronicos.length; i++) {
                    System.out.println((i + 1) + " - " + produtosEletronicos[i]);
                }
                int opcaoProduto = scanner.nextInt();
                System.out.println("Quantidade: ");
                int quantidade = scanner.nextInt();
                precoTotal += eletronicos[opcaoProduto - 1] * quantidade;
                
            }
            if (opcao == 2) {
                System.out.println("Escolha o produto: ");
                for (int i = 0; i < produtosAlimentos.length; i++) {
                    System.out.println((i + 1) + " - " + produtosAlimentos[i]);
                }
                int opcaoProduto = scanner.nextInt();
                System.out.println("Quantidade: ");
                int quantidade = scanner.nextInt();
                precoTotal += alimentos[opcaoProduto - 1] * quantidade;
            }
            if (opcao == 3) {
                System.out.println("Escolha o produto: ");
                for (int i = 0; i < produtosVestuario.length; i++) {
                    System.out.println((i + 1) + " - " + produtosVestuario[i]);
                }
                int opcaoProduto = scanner.nextInt();
                System.out.println("Quantidade: ");
                int quantidade = scanner.nextInt();
                precoTotal += vestuario[opcaoProduto - 1] * quantidade;
            }
            if (opcao == 4) {
                System.out.println("Escolha o produto: ");
                for (int i = 0; i < produtosMoveis.length; i++) {
                    System.out.println((i + 1) + " - " + produtosMoveis[i]);
                }
                int opcaoProduto = scanner.nextInt();
                System.out.println("Quantidade: ");
                int quantidade = scanner.nextInt();
                precoTotal += moveis[opcaoProduto - 1] * quantidade;
            }

            
            if (precoTotal > 500) {
                desconto = precoTotal * 0.045;
            }
            if (opcao == 1) {
                desconto += precoTotal * 0.02;
            }
            


            
        }

        System.out.println("Preço total: " + precoTotal);
        System.out.println("Desconto: " + desconto);
        System.out.println("Preço final: " + (precoTotal - desconto));

        
        
    }
    
}
