package sorocaba.peteca.com.graphgenerator;

public class SerieBarras {
    public static final int tamanhoPontos = 15;
    public int[] valor_x;
    public double[] valor_y, arg1, arg2;
    private int numeroPontos;
    public double valorMaximo = 0;
    private boolean primeiroValor = true;

    public SerieBarras() {
        valor_x = new int[tamanhoPontos];
        valor_y = new double[tamanhoPontos];
        arg1 = new double[tamanhoPontos];
        arg2 = new double[tamanhoPontos];
        numeroPontos = 0;
    }

    public boolean addBarra(int valor_x, double valor_y, double arg1, double arg2) {
        this.arg1[numeroPontos] = arg1;
        this.arg2[numeroPontos] = arg2;
        return addUmaBarra(valor_x, valor_y);
    }
    public boolean addBarra(int valor_x, double valor_y) {
        this.arg1[numeroPontos] = 0;
        this.arg2[numeroPontos] = 0;
        return addUmaBarra(valor_x, valor_y);
    }

    private boolean addUmaBarra(int valor_x, double valor_y) {
        if (numeroPontos > tamanhoPontos)
            return false;

        this.valor_x[numeroPontos] = valor_x;
        this.valor_y[numeroPontos] = valor_y;
        numeroPontos++;

        if(primeiroValor) {
            valorMaximo = valor_y;
            primeiroValor = false;
            return true;
        }

        if (valor_y > valorMaximo)
            valorMaximo = valor_y;
        return true;
    }

    public void reiniciar() {
        for (numeroPontos = tamanhoPontos; numeroPontos >= 0; numeroPontos--){
            valor_x[numeroPontos] = 0;
            valor_y[numeroPontos] = 0;
        }
    }
    public int tamanho() {
        return numeroPontos;
    }

    public double getValorMaximo() {
        return this.valorMaximo;
    }
}
