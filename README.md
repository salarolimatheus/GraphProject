# Gráfico de Barras
![Badge Desenvolvido](http://img.shields.io/static/v1?label=STATUS&message=DESENVOLVIDO&color=GREEN&style=for-the-badge)

[![](https://jitpack.io/v/salarolimatheus/GraphProject.svg)](https://jitpack.io/#salarolimatheus/GraphProject)

Um gráfico de barras para Android focada para a visualização de componentes harmônicas de um sinal. 
Fique a vontade para alterar as coisas que forem necessárias para adequar ao seu projeto.
- Caso precise mudar as informações na legenda: modifique a função atualizaLegenda()

## 📱 Visualização

![tela-app](https://user-images.githubusercontent.com/34732144/185512310-e4a8d777-209f-4a19-a94d-7282f7bf8584.gif)

## 🛠️ Como funciona
### XML
Para adicionar o objeto no XML, basta utilizar a seguinte estrutura:
```
<sorocaba.peteca.com.graphgenerator.Grafico
android:layout_width="match_parent"
android:layout_height="match_parent"
android:id="@+id/grafico"
android:layout_centerHorizontal="true"/>
```

### Java
Configurando os eixos e o gráfico em geral:
```
grafico = findViewById(R.id.grafico);
grafico.setGraficoFechado(true);
grafico.setGradeStatus(true);
grafico.setNomeEixoX("Ordem");
grafico.setNomeEixoY("Magnitude [A]");
grafico.setNumeroGradesHorizontais(4);
grafico.setGrandezaMagnitude("A");
grafico.setLegenda("Ordem", "f", "Magnitude", "Fase");
```

Manipulando os dados para serem apresentados no gráfico:
```
SerieBarras series = new SerieBarras();
series.addBarra(1, 80.21513959303451, 1, 60);
series.addBarra(2, 1.2014214,1, 120);
series.reiniciar();
series.addBarra(3, 30,1, 180);
series.addBarra(4, 1,1, 240);
series.addBarra(5, 1,1, 240);
series.addBarra(6, 50,1, 300);
series.addBarra(8, 30,1, 180);
series.addBarra(9, 1,1, 240);

grafico.addSerie(series);

grafico.setOnTouchListener(new View.OnTouchListener() {
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {
	grafico.ToqueNaTela(motionEvent);
	return true;
    }
});
```

#### Funções
Gráfico de Barras:
| Função                                                  	| Descrição da ação                                                                                                     	|
|---------------------------------------------------------	|-----------------------------------------------------------------------------------------------------------------------	|
| setGradeStatus(boolean gradeStatus)                     	| Adiciona as linhas de grade caso gradeStatus for verdadeiro. O padrão é verdadeiro                                    	|
| setGraficoFechado(boolean graficoFechado)               	| Deixa o gráfico retangular (fecha as arestas superior e direito) caso graficoFechado for verdadeiro. O padrão é falso 	|
| setNomeEixoX(String nomeEixoX)                          	| Insero o nome do eixo X para nomeEixoX                                                                                	|
| setNomeEixoY(String nomeEixoY)                          	| Insere o nome do eixo Y para nomeEixoY                                                                                	|
| setNumeroGradesHorizontais(int numeroGradesHorizontais) 	| Modifica o numero de divisões horizontais da grade                                                                    	|
| setBarraColor (int color)                               	| Modifica a cor padrão das barras                                                                                      	|
| setBarraSelecionadaColor(int color)                     	| Modifica a cor da barra selecionada                                                                                   	|
| setGrandezaPhase(String grandezaPhase)                  	| Adiciona a grandeza do parametro fase da legenda                                                                      	|
| setGrandezaFreq(String grandezaFreq)                    	| Adiciona a grandeza do parametro frequência da legenda                                                                	|
| setGrandezaMagnitude(String grandezaMagnitude)          	| Adiciona a grandeza do parametro magnitude da legenda                                                                 	|
| addSerie(SerieBarras series)                            	| Adiciona uma serie de barras                                                                                          	|

Série de Barras:
| Função                                                                  	| Descrição da ação                                                                                    	|
|-------------------------------------------------------------------------	|------------------------------------------------------------------------------------------------------	|
| addBarra ( int  valor_x,  double  valor_y,  double  arg1,  double arg2) 	| Adiciona uma barra com índice valor_x, valor final de valor_y e dois argumentos relacionados a barra 	|
| addBarra ( int  valor_x,  double valor_y)                               	| Adiciona uma barra com indice valor_x e valor final de valor_y                                       	|
| reiniciar()                                                             	| Retira todos os valores inseridos na série                                                           	|
| tamanho()                                                               	| Retorna o tamanho da série                                                                           	|
| getValorMaximo()                                                        	| Retorna o maior valor inserido na série                                                              	|
