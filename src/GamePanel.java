import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;

public class GamePanel extends JPanel implements ActionListener {

    static final int SCREEN_WIDTH = 600;
    static final int SCREEN_HEIGHT = 600;
    static final int UNIT_SIZE = 20;
    static final int GAME_UNITS = (SCREEN_WIDTH*SCREEN_HEIGHT)/UNIT_SIZE;
    static final int DELAY = 75;
    final int[] x = new int[GAME_UNITS];
    final int[] y = new int[GAME_UNITS];

    int snakeEyePosOne = (int)(((int)(UNIT_SIZE / 20)) * 3);
    int snakeEyePosTwo = (int)(((int)(UNIT_SIZE / 4)) * 3);
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    Direction direction = Direction.Right;
    int distanceSinceLastDirectionChange = 0;
    boolean running = false;
    Timer timer;
    Random random;

    GamePanel(){
        random = new Random();
        this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(new MyKeyAdapter());
        startGame();
    }

    public void startGame(){
        newApple();
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

        // Initialize the snake in the center of the screen
        x[0] = SCREEN_WIDTH / 2;
        y[0] = SCREEN_HEIGHT / 2;
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        draw(g);
    }

    public void drawSnakeHead(Graphics g, int i){
        g.setColor(Color.GREEN);
        g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

        // Eyes of the snake
        g.setColor(Color.BLACK);
        switch (direction) {
            case Left:
                g.fillRect(x[0] + snakeEyePosOne, y[0] + snakeEyePosOne, snakeEyePosOne, snakeEyePosOne);
                g.fillRect(x[0] + snakeEyePosOne, y[0] + snakeEyePosTwo, snakeEyePosOne, snakeEyePosOne);
                break;
            case Right:
                g.fillRect(x[0] + snakeEyePosTwo, y[0] + snakeEyePosOne, snakeEyePosOne, snakeEyePosOne);
                g.fillRect(x[0] + snakeEyePosTwo, y[0] + snakeEyePosTwo, snakeEyePosOne, snakeEyePosOne);
                break;
            case Up:
                g.fillRect(x[0] + snakeEyePosOne, y[0] + snakeEyePosOne, snakeEyePosOne, snakeEyePosOne);
                g.fillRect(x[0] + snakeEyePosTwo, y[0] + snakeEyePosOne, snakeEyePosOne, snakeEyePosOne);
                break;
            case Down:
                g.fillRect(x[0] + snakeEyePosOne, y[0] + snakeEyePosTwo, snakeEyePosOne, snakeEyePosOne);
                g.fillRect(x[0] + snakeEyePosTwo, y[0] + snakeEyePosTwo, snakeEyePosOne, snakeEyePosOne);
                break;
        }
    }

    public void drawSnake(Graphics g){
        g.setColor(Color.RED);
        g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

        for (int i = 0; i < bodyParts; i++) {
            if (i == 0) {
                drawSnakeHead(g, i);
            } else {
                if(i%2 == 0){
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
                else {
                    g.setColor(new Color(45, 180, 0));
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
                }
            }
            drawSnakeHead(g, 0);
        }
    }

    public void draw(Graphics g){

        if(running) {
            //draws the snake
            drawSnake(g);
            //Scoreboard text
            scoreboard(g);
            //Increases the value of distanceSinceLastDirectionChange every time the snake is drawn
            distanceSinceLastDirectionChange++;
        }
        else {
            //Draws 'Game Over!' screen
            gameOver(g);
        }
    }

    public void newApple(){

        appleX = random.nextInt((int)(SCREEN_WIDTH/UNIT_SIZE))*UNIT_SIZE;
        appleY = random.nextInt((int)(SCREEN_HEIGHT/UNIT_SIZE))*UNIT_SIZE;
    }

    public void move(){

        for(int i = bodyParts; i > 0; i--){
            x[i] = x[i-1];
            y[i] = y[i-1];
        }

        switch(direction){
            case Up:
                y[0] = y[0] - UNIT_SIZE;
                break;
            case Down:
                y[0] = y[0] + UNIT_SIZE;
                break;
            case Left:
                x[0] = x[0] - UNIT_SIZE;
                break;
            case Right:
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    public void checkApple(){
        if (x[0] == appleX && y[0] == appleY){
            bodyParts++;
            applesEaten++;
            newApple();
        }
    }

    public void scoreboard(Graphics g){
        //Scoreboard text
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman", Font.BOLD, 40));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("Score: "+ applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: "+ applesEaten))/2, g.getFont().getSize());
    }

    public void checkCollisions(){

        //checks if head collides with body
        for(int i = bodyParts; i > 0; i--){
            if (x[0] == x[i] && y[0] == y[i]){
                running = false;
                break;
            }
        }

        //check if head touches left border
        if (x[0] < 0){
            x[0] = SCREEN_WIDTH - UNIT_SIZE;
        }

        //check if head touches right border
        if (x[0] > SCREEN_WIDTH - UNIT_SIZE){
            x[0] = 0;
        }

        //check if head touches top border
        if (y[0] < 0){
            /*running = false;*/
            y[0] = SCREEN_HEIGHT - UNIT_SIZE;
        }

        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT - UNIT_SIZE){
            y[0] = 0;
        }

        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
        //Draw last frame of the game
        drawSnake(g);

        //Scoreboard text
        scoreboard(g);

        //Game Over text
        g.setColor(Color.RED);
        g.setFont(new Font("Times New Roman", Font.BOLD, 75));
        FontMetrics metrics = getFontMetrics(g.getFont());
        g.drawString("GAME OVER!", (SCREEN_WIDTH - metrics.stringWidth("GAME OVER!"))/2, SCREEN_HEIGHT/2);
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        if(running){
            move();
            checkApple();
            checkCollisions();
        }
        repaint();
    }

    public class MyKeyAdapter extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            if (distanceSinceLastDirectionChange >= 1) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_LEFT:
                        if (direction != Direction.Right) {
                            direction = Direction.Left;
                            distanceSinceLastDirectionChange = 0;
                        }
                        break;
                    case KeyEvent.VK_RIGHT:
                        if (direction != Direction.Left) {
                            direction = Direction.Right;
                            distanceSinceLastDirectionChange = 0;
                        }
                        break;
                    case KeyEvent.VK_UP:
                        if (direction != Direction.Down) {
                            direction = Direction.Up;
                            distanceSinceLastDirectionChange = 0;
                        }
                        break;
                    case KeyEvent.VK_DOWN:
                        if (direction != Direction.Up) {
                            direction = Direction.Down;
                            distanceSinceLastDirectionChange = 0;
                        }
                        break;
                }
            }
        }
    }
}
