package simulation;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Dimension;
import java.awt.Color;

public class GridPanel extends JPanel {
  private int[][] grid;
  private int SIZE;
  private int SCALE;

  public GridPanel( int[][] gridpointer, int size, int scale ){
    grid = gridpointer;
    SIZE = size;
    SCALE = scale;
  }

  public void paintComponent(Graphics g) {
        super.paintComponent(g);

        // Draw Grid
        g.setColor(Color.black);
        for( int i = 0; i < SIZE; i++ ){
          g.drawLine( i*(SCALE+1), 0, i*(SCALE+1), SIZE*(SCALE+1)+1 );
          g.drawLine( 0, i*(SCALE+1), SIZE*(SCALE+1)+1, i*(SCALE+1) );
        }

        //Draw units
        for( int i = 0; i < SIZE; i++ ){
          for( int j = 0; j < SIZE; j++ ){
            //Food
            if( grid[i][j] == 1 ){
              g.setColor(Color.green);
              g.fillRect( i*(SCALE+1)+1, j*(SCALE+1)+1, SCALE, SCALE );
            }
            //Unit
            if( grid[i][j] == 2 ){
              g.setColor(Color.red);
              g.fillRect( i*(SCALE+1)+1, j*(SCALE+1)+1, SCALE, SCALE );
            }
          }
        }
    }

    public Dimension getPreferredSize() {
        return new Dimension(SIZE*(SCALE+1)+1, SIZE*(SCALE+1)+1);
    }

}