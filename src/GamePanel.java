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
    int bodyParts = 6;
    int applesEaten;
    int appleX;
    int appleY;
    Direction direction = Direction.Right;
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
    }

    public void paintComponent(Graphics g){

        super.paintComponent(g);
        draw(g);
    }

    public void draw(Graphics g){

        if(running) {
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            for (int i = 0; i < bodyParts; i++) {
                if (i == 0) {
                    g.setColor(Color.GREEN);
                    g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);

                    // Eyes of the snake
                    g.setColor(Color.BLACK);
                    switch (direction) {
                        case Left:
                            g.fillRect(x[0] + 3, y[0] + 3, 3, 3);
                            g.fillRect(x[0] + 3, y[0] + 15, 3, 3);
                            break;
                        case Right:
                            g.fillRect(x[0] + 15, y[0] + 3, 3, 3);
                            g.fillRect(x[0] + 15, y[0] + 15, 3, 3);
                            break;
                        case Up:
                            g.fillRect(x[0] + 3, y[0] + 3, 3, 3);
                            g.fillRect(x[0] + 15, y[0] + 3, 3, 3);
                            break;
                        case Down:
                            g.fillRect(x[0] + 3, y[0] + 15, 3, 3);
                            g.fillRect(x[0] + 15, y[0] + 15, 3, 3);
                            break;
                    }
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
            }
            //Scoreboard text
            scoreboard(g);
        }
        else {
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
            running = false;
        }

        //check if head touches right border
        if (x[0] > SCREEN_WIDTH){
            running = false;
        }

        //check if head touches top border
        if (y[0] < 0){
            running = false;
        }

        //check if head touches bottom border
        if (y[0] > SCREEN_HEIGHT){
            running = false;
        }

        if(!running){
            timer.stop();
        }
    }

    public void gameOver(Graphics g){
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
        public void keyPressed(KeyEvent e){
            switch(e.getKeyCode()){
                case KeyEvent.VK_LEFT:
                    if(direction != Direction.Right){
                        direction = Direction.Left;
                    }
                    break;
                case KeyEvent.VK_RIGHT:
                    if(direction != Direction.Left){
                        direction = Direction.Right;
                    }
                    break;
                case KeyEvent.VK_UP:
                    if(direction != Direction.Down){
                        direction = Direction.Up;
                    }
                    break;
                case KeyEvent.VK_DOWN:
                    if(direction != Direction.Up){
                        direction = Direction.Down;
                    }
                    break;
            }
        }
    }
}
