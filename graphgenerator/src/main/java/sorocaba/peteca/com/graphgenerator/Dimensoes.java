package sorocaba.peteca.com.graphgenerator;

public class Dimensoes {
    private float alturaTotal, larguraTotal;
    private float alturaGrafico, larguraGrafico;
    private float alturaEixoX, alturaEixoY;
    private float larguraEixoX, larguraEixoY;

    public Dimensoes(float alturaTotal, float larguraTotal) {
        this.alturaTotal = alturaTotal;
        this.larguraTotal = larguraTotal;
        Calculos();
    }

    private void Calculos() {
        alturaEixoY = 0.04f * alturaTotal;
        alturaGrafico = 0.92f * alturaTotal;
        alturaEixoX = 0.04f * alturaTotal;

        larguraEixoY = 0.09f * larguraTotal;
        larguraGrafico = 0.90f * larguraTotal;
        larguraEixoX = 0.01f * larguraTotal;
    }

    public float getAlturaGrafico() {
        return alturaGrafico;
    }
    public void setAlturaGrafico(float alturaGrafico) {
        this.alturaGrafico = alturaGrafico;
    }
    public float getLarguraGrafico() {
        return larguraGrafico;
    }
    public void setLarguraGrafico(float larguraGrafico) {
        this.larguraGrafico = larguraGrafico;
    }
    public float getAlturaEixoX() {
        return alturaEixoX;
    }
    public void setAlturaEixoX(float alturaEixoX) {
        this.alturaEixoX = alturaEixoX;
    }
    public float getAlturaEixoY() {
        return alturaEixoY;
    }
    public void setAlturaEixoY(float alturaEixoY) {
        this.alturaEixoY = alturaEixoY;
    }
    public float getLarguraEixoX() {
        return larguraEixoX;
    }
    public void setLarguraEixoX(float larguraEixoX) {
        this.larguraEixoX = larguraEixoX;
    }
    public float getLarguraEixoY() {
        return larguraEixoY;
    }
    public void setLarguraEixoY(float larguraEixoY) {
        this.larguraEixoY = larguraEixoY;
    }

}
