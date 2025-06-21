import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.*;

/*Este programa tem como objetivo implementar uma interface gráfica capaz de desenhar um
 * triângulo conectando os 3 primeiros pontos onde o usuário clicar, além de mostrar o
 * tamanho dos lados da figura, medidos em pixels, e o ângulo de cada ponto, aproximado 
 * em graus.
*/

//A classe principal extende de JPanel, para que possamos sobrescrever alguns métodos, e
//implementa a interface MouseListener, para conseguir detectar os cliques do usuário
public class App extends JPanel implements MouseListener {
    
    private int points = 0; //Contador de quantos pontos existem na tela

    private int[][] posicoes = {{0, 0}, {0, 0}, {0, 0}}; //Matriz representando as
    //coordenadas dos vértices do triângulo: cada linha é um ponto, a 1° coluna é o valor de
    //x e a segunda é o y desse ponto

    private int ovalLength = 20; //Tamanho dos vértices
    private int posX; //Coordenada x do próximo ponto a ser desenhado
    private int posY; //Coordenada y do próximo ponto a ser desenhado
    
    public App() { //Construtor da classe App, que será o painel de desenho
        super(); 
        addMouseListener(this); //Adiciona o evento de leitura de clicks à classe
    }

    @Override //Sobrescreve o método que pinta o painel, para podermos editá-lo
    public void paintComponent(Graphics g) {
        g.setColor(Color.RED); //Define a cor de escrita como vermelha
        g.drawString("O valor dos ângulos é uma aproximação", 50, 50);

        if(points != 0) { //Se o usuário já clicou pelo menos uma vez
            
            if(points <= 3) { //Se o usuário clicou pela primeira, segunda ou terceira vez
                g.fillOval(posX, posY, ovalLength, ovalLength); //Desenha um círculo
            }
            
            if(points == 3) { //Se o usuário clicou pela terceira vez
                //Desenha linhas conectando os centros dos vértices, armazenados em posicoes
                g.drawLine(posicoes[0][0], posicoes[0][1], posicoes[1][0], posicoes[1][1]);
                g.drawLine(posicoes[0][0], posicoes[0][1], posicoes[2][0], posicoes[2][1]);
                g.drawLine(posicoes[2][0], posicoes[2][1], posicoes[1][0], posicoes[1][1]);

                int[] sidesSizes = calculateSides(posicoes); //Calcula os tamanhos dos lados
                
                //Calcula as posições onde os tamanhos dos lados serão mostrados
                int[][] textPositions = calculateTextPositions(posicoes);

                //Escreve os tamanhos dos lados nas posições certas
                g.drawString(String.valueOf(sidesSizes[0]), textPositions[0][0], 
                textPositions[0][1]);

                g.drawString(String.valueOf(sidesSizes[1]), textPositions[1][0], 
                textPositions[1][1]);

                g.drawString(String.valueOf(sidesSizes[2]), textPositions[2][0], 
                textPositions[2][1]);

                //Calcula os valores dos ângulos de cada vértice
                double[] angles = calculateAngles(sidesSizes);

                //Escreve os valores dos ângulos, em graus, um pouco acima dos vértices
                g.drawString(String.valueOf(angles[0]) + "°", posicoes[0][0], posicoes[0][1]
                - (ovalLength / 2));

                g.drawString(String.valueOf(angles[1]) + "°", posicoes[1][0], posicoes[1][1]
                - (ovalLength / 2));

                g.drawString(String.valueOf(angles[2]) + "°", posicoes[2][0], posicoes[2][1]
                - (ovalLength / 2));
            }
        }
    }

    //Função que calcula os valores dos lados
    private int[] calculateSides(int[][] posicoes) {
        //Array de retorno com os valores dos lados:
        //[0] -> Lado entre o primeiro e o segundo ponto;
        //[1] -> Lado entre o segundo e o terceiro ponto;
        //[2] -> Lado entre o primeiro e o terceiro ponto
        int[] result = new int [3];

        //Usa a fórmula da distância entre dois pontos num plano cartesiano para calcular o
        //tamanho de cada lado
        result[0] = (int)(Math.sqrt(Math.pow(posicoes[1][0] - posicoes[0][0], 2.0) 
        + Math.pow(posicoes[1][1] - posicoes[0][1], 2.0)));

        result[1] = (int)(Math.sqrt(Math.pow(posicoes[1][0] - posicoes[2][0], 2.0) 
        + Math.pow(posicoes[1][1] - posicoes[2][1], 2.0)));

        result[2] = (int)(Math.sqrt(Math.pow(posicoes[2][0] - posicoes[0][0], 2.0) 
        + Math.pow(posicoes[2][1] - posicoes[0][1], 2.0)));

        return result; //Retorna o resultado
    }

    //Função que calcula as posições onde os tamanhos dos lados serão escritos
    private int[][] calculateTextPositions(int[][] posicoes) {
        int[][] result = new int[3][2]; //Array de retorno

        //Calcula a média entre X e Y de cada ponto para obter os valores finais
        //Linha [0] -> Primeiro ponto; Linha [1] -> Segundo ponto; Linha [2] ->Terceiro ponto

        result[0][0] = (posicoes[0][0] + posicoes[1][0]) / 2;
        result[0][1] = (posicoes[0][1] + posicoes[1][1]) / 2;

        result[1][0] = (posicoes[1][0] + posicoes[2][0]) / 2;
        result[1][1] = (posicoes[1][1] + posicoes[2][1]) / 2;

        result[2][0] = (posicoes[0][0] + posicoes[2][0]) / 2;
        result[2][1] = (posicoes[0][1] + posicoes[2][1]) / 2;

        return result; //Retorna o resultado
    }

    //Função que calcula os ângulos de cada vértice
    private double[] calculateAngles(int[] sidesSizes) {
        //Ponto A = Entre os lados [0] e [2]; Lado oposto = sidesSizes[1] = a
        //Ponto B = Entre os lados [0] e [1]; Lado oposto = sidesSizes[2] = b
        //Ponto C = Entre os lados [1] e [2]; Lado oposto = sidesSizes[0] = c
        //Lei dos Cossenos -> a² = b² + c² - 2bc * cos(A) -> (a² - b² - c²) / -2bc = cos(A)
        
        double[] result = new double[3]; //Array de retorno

        //Usa a Lei dos Cossenos para calcular o cosseno de cada ângulo e a função 
        //Arco Cosseno (Math.acos()) para convertê-los no valor de cada ângulo, em radianos
        result[0] = Math.acos((Math.pow(sidesSizes[1], 2) - Math.pow(sidesSizes[2], 2) -
        Math.pow(sidesSizes[0], 2)) / (-2 * sidesSizes[2] * sidesSizes[0])); //Ângulo de A

        result[1] = Math.acos((Math.pow(sidesSizes[2], 2) - Math.pow(sidesSizes[1], 2) -
        Math.pow(sidesSizes[0], 2)) / (-2 * sidesSizes[1] * sidesSizes[0])); //Ângulo de B

        result[2] = Math.acos((Math.pow(sidesSizes[0], 2) - Math.pow(sidesSizes[1], 2) -
        Math.pow(sidesSizes[2], 2)) / (-2 * sidesSizes[1] * sidesSizes[2])); //Ângulo de C

        //Converte os valores de radianos para graus
        result[0] = Math.toDegrees(result[0]);
        result[1] = Math.toDegrees(result[1]);
        result[2] = Math.toDegrees(result[2]);

        //Arredonda os valores dos ângulos para diminuir o número de casas decimais
        result[0] = Math.floor(result[0]);
        result[1] = Math.floor(result[1]);
        result[2] = Math.floor(result[2]);

        return result; //Retorna o resultado
    }

    //Sobrescreve os eventos de mouseClick, dos quais só usaremos o mouseClicked
    @Override
    public void mouseClicked(MouseEvent e) {
        //Define as coordenadas do vértice como o ponto onde o usuário clicou
        posX = e.getX() - (ovalLength / 2);
        posY = e.getY() - (ovalLength / 2);

        boolean coordSaved = false; //Variável de controle

        //Preenche as posições dos 
        for (int i = 0; i < posicoes.length; i++) {
            for (int j = 0; j < posicoes[j].length; j++) {
                if(posicoes[i][j] == 0) {
                    posicoes[i][0] = posX + (ovalLength / 2); //Outra correção para que as
                    posicoes[i][1] = posY + (ovalLength / 2); //linhas saiam ponto certo
                    coordSaved = true;
                    break;
                }
            }

            if(coordSaved) {break;}
        }

        points++; //Aumenta o número de vértices
        repaint(); //Invoca o método paintComponent()
    }

    //Aqui estão os eventos que não serão utilizados, mas precisam ser sobrescritos
    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }

    @Override
    public void mousePressed(MouseEvent e) {
        
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        
    }
    public static void main(String[] args) {
        //Inicia um objeto da classe App, que herda de JPanel, para executar seu construtor
        App painel = new App(); 

        //Faz a configuração da tela a ser mostrada e adiciona o painel a ela
        JFrame tela = new JFrame("Trigonometria");
        tela.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        tela.setSize(800, 500);
        tela.add(painel);
        tela.setVisible(true);
        tela.setTitle("Trigonometria");
        tela.setLocationRelativeTo(null);
    }
}
