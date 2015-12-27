package simulation;
import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.BasicStroke;
import java.awt.Dimension;
import java.awt.Color;

public class GridPanel extends JPanel {
private int[][] grid;
private int SIZE;
private int SCALE;

private BasicStroke stroke;

public GridPanel( int[][] gridpointer, int size, int scale ){
	grid = gridpointer;
	SIZE = size;
	SCALE = scale;

  float hashsize = 5.0f;  //Must be odd
  float gap = (float)SCALE - 2*((hashsize-1.0f)/2.0f);
  float offset = 2*((hashsize-1.0f)/2.0f);

	float dash1[] = { hashsize, gap };
	stroke = new BasicStroke(1,
	                         BasicStroke.CAP_BUTT,
	                         BasicStroke.JOIN_ROUND,
	                         1.0f,
	                         dash1,
	                         2.0f);
}

public void paintComponent(Graphics g) {
	super.paintComponent(g);

	Graphics2D g2 = (Graphics2D) g;
	// Draw Grid
	g.setColor(Color.black);
	g2.setStroke(stroke);
	for( int i = 0; i < SIZE; i++ ) {
		g2.drawLine( i*(SCALE+1), 0, i*(SCALE+1), SIZE*(SCALE+1)+1 );
		g2.drawLine( 0, i*(SCALE+1), SIZE*(SCALE+1)+1, i*(SCALE+1) );
	}
	g2.drawLine( SIZE*(SCALE+1), 0, SIZE*(SCALE+1), SIZE*(SCALE+1)+1 );
	g2.drawLine( 0, SIZE*(SCALE+1), SIZE*(SCALE+1)+1, SIZE*(SCALE+1) );

	//Draw units
	for( int i = 0; i < SIZE; i++ ) {
		for( int j = 0; j < SIZE; j++ ) {
			//Food
			if( grid[i][j] == 1 ) {
				g.setColor(Color.green);
				g2.fillRect( i*(SCALE+1)+5, j*(SCALE+1)+5, SCALE-7, SCALE-7 );
			}
			//Unit
			if( grid[i][j] == 2 ) {
				g.setColor(Color.red);
				g2.fillRect( i*(SCALE+1)+2, j*(SCALE+1)+2, SCALE-3, SCALE-3 );
			}
		}
	}
}

public Dimension getPreferredSize() {
	return new Dimension(SIZE*(SCALE+1)+1, SIZE*(SCALE+1)+1);
}

}
