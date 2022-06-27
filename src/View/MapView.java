package View;

import Controller.MapViewController;
import Model.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.MatteBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;



public class MapView {

    private HashMap<Integer, JButton> buttons = new HashMap<>();
    public Room[][] map;
    public JButton nextButton = new JButton("Next");
    public JButton resetButton = new JButton("Reset");
    public JLabel label = new JLabel("Player 1, choose a room!");
    public GameManager game = new GameManager();
    JFrame frame = new JFrame("Labyrinth Game");

    public ImageIcon p1Image;
    public ImageIcon p2Image;
    public ImageIcon foeImage;

    MapViewController mvc = new MapViewController();

    // create the view and tie it to the MapViewController with observers/listeners - whatever they're called - I assume
    // with code Jeff gave us it is fairly simple to implement

    public void generateView() {

        label.setVerticalAlignment(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);

        try {
            p1Image = new ImageIcon(ImageIO.read(getClass().getResource("pig.png")));
            p2Image = new ImageIcon(ImageIO.read(getClass().getResource("rabbit.png")));
            foeImage = new ImageIcon(ImageIO.read(getClass().getResource("snake.png")));
        } catch (Exception e) {
            System.out.println("Error finding file");
        }

        mvc.setGame(game);

        frame.setSize(1125, 1125);
        frame.setLayout(null);

        for(int i = 1; i <= 81; i++) {
            JButton curButton = new JButton();
            buttons.put(i, curButton);
        }

        int dicCount = 1;

        for(int y = 0; y < 9; y ++) {
            for(int x = 0; x < 9; x++) {
                int top = 1;
                int bottom = 1;
                int left = 1;
                int right = 1;
//                if(map[x][y].northWall) {
//                    top = 3;
//                }
//                if(map[x][y].westWall) {
//                    left = 3;
//                }
//                if(map[x][y].eastWall) {
//                    right = 3;
//                }
//                if(map[x][y].southWall) {
//                    bottom = 3;
//                }

                JButton curButton = buttons.get(dicCount);
                curButton.setBounds(x*75, y*75, 75, 75);
                curButton.setBackground(Color.black);
                curButton.setOpaque(true);
                curButton.setBorder(new MatteBorder(top, left, bottom, right, Color.white));
                int finalX = x;
                int finalY = y;
                curButton.addActionListener(new ActionListener()
                {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.out.println(finalX + " " + finalY);
                        mvc.setRoomPressed(map[finalX][finalY]);
                        mvc.buttonClicked();
                        String labelMessage = mvc.labelMessage;
                        label.setText(labelMessage);
                        update();
                        if(game.gameOver) {
                            gameOver();
                        }
                    }
                });

                frame.add(curButton);

                dicCount++;
            }
        }

        nextButton.setBounds(337, 675, 336, 40);
        nextButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                mvc.nextButtonClicked();
                String labelMessage = mvc.labelMessage;
                label.setText(labelMessage);
                // update turn and move monster if necessary
                update();
                if(game.gameOver) {
                    gameOver();
                }
            }
        });

        resetButton.setBounds(337, 715, 336, 40);
        resetButton.addActionListener(new ActionListener()
        {
            @Override
            public void actionPerformed(ActionEvent e) {
                LabyrinthDriver ld = new LabyrinthDriver();
                ld.run();
            }
        });

        label.setBounds(0, 675, 337, 80);

        frame.add(nextButton);
        frame.add(resetButton);
        frame.add(label);

        frame.setVisible(true);

    }

    public void update() {
        int dicCount = 1;

        for(int y = 0; y < 9; y ++) {
            for(int x = 0; x < 9; x++) {
                JButton curButton = buttons.get(dicCount);
                curButton.setIcon(null);
                curButton.setBackground(Color.black);

                if (!game.p1.eliminated) {
                    if (map[x][y].equalPlace(game.p1.curRoom)) {
                        curButton.setIcon(p1Image);
                    }

                    if(map[x][y].equalPlace(game.p1.secretRoom)) {
                        curButton.setBackground(Color.blue);
                    }
                }

                if (!game.p2.eliminated) {
                    if(map[x][y].getCoords().get("x") == game.p2.getCurRoom().getCoords().get("x") && map[x][y].getCoords().get("y") == game.p2.getCurRoom().getCoords().get("y")) {
                        curButton.setIcon(p2Image);
                    }

                    if(map[x][y].getCoords().get("x") == game.p2.secretRoom.getCoords().get("x") && map[x][y].getCoords().get("y") == game.p2.secretRoom.getCoords().get("y")) {
                        curButton.setBackground(Color.RED);
                    }
                }

                if(game.started) {

                    int top = 0;
                    int bottom = 0;
                    int left = 0;
                    int right = 0;
                    if(map[x][y].northWall && map[x][y].northWallVisible) {
                        top = 3;
                    }
                    if(map[x][y].westWall && map[x][y].westWallVisible) {
                        left = 3;
                    }
                    if(map[x][y].eastWall && map[x][y].eastWallVisible) {
                        right = 3;
                    }
                    if(map[x][y].southWall && map[x][y].southWallVisible) {
                        bottom = 3;

                    }
                    curButton.setBorder(new MatteBorder(top, left, bottom, right, Color.orange));
                    if((map[x][y].equalPlace(game.foe.curRoom)) && game.foe.isVisible) {
                        curButton.setIcon(foeImage);
                    }

                    if((map[x][y].equalPlace(game.treasure.curRoom)) && game.treasure.isVisible) {
                        curButton.setBackground(Color.YELLOW);
                    }
                }
                dicCount++;
            }
        }
    }

    public void setMap(Room[][] labyrinth) {
        this.map = labyrinth;
    }

    public void setGame(GameManager game) {
        this.game = game;
    }

    public void gameOver() {
        for(int i = 1; i <= buttons.size(); i ++) {
            buttons.get(i).setEnabled(false);
        }
        nextButton.setEnabled(false);
        resetButton.setEnabled(true);
    }



}
