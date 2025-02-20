import java.text.DecimalFormat;

public class DivisaoDouble {
    //int a = 200;
    //int b = 10;
    //int c = a / b;
    double a,b,c;

    public static void main(String[] args) {
        DivisaoDouble divisao = new DivisaoDouble();
        divisao.entrada();
        divisao.calcula();
        divisao.resposta();
    }

    public void entrada(){
        a = 10;
        b = 23;
    }

    public void calcula(){
        c = a/b;
    }

    public void resposta(){
        System.out.println("O valor de A é " + a);
        System.out.println("O valor de B é " + b);
        System.out.println("A divisao dos dois = " + c);
        DecimalFormat arredonda = new DecimalFormat("###,###,###");
        System.out.println("O valor do decial é " + arredonda.format(c));
    }


    
}
