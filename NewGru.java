public class NewGru {
    //variáveis globais 
    int a = 9;
    int b = 10;
    int c = a + b;

    public static void main(String[] args) { //método estático consegue ser visto por todos!!
        NewGru g = new NewGru();
        g.processamento();
    }

    public void processamento(){
        System.out.println("O valor de A é " + a);
        System.out.println("O valor de Pi é " + b);
        System.out.println("O valor de C é " + c);
    }
    
}
