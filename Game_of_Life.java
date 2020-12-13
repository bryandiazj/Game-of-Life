import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Game_of_Life
{	
	public static void main (String [] args)
	{
		int [][]initStates = null;	//Initial cell states
		String inputFileName = "pattern1.txt";		//The file name containing initial states
		initStates = getInitialStates(inputFileName);
		
		Grid gd = new Grid(initStates);
		
		gd.display();		//Display cells
		gd.getStats();		//Get stats of cells on a grid
		gd.run();			//Start running the simulation
	}

	//The first parameter "fileName" is the input file name containing initial states of cells. 
	//The first line in the file contains rows and columns of a grid. The remaining lines contain the states represented by 
	//1s meaning live and 0s dead.
	//This method takes an input file name and returns the initial states of cells.
	public static int [][] getInitialStates(String fileName)
	{
		File file = new File(fileName);
		
		//Create a 2D array for the initial states of cells
		int [][] states = null;
			
		try {
			Scanner input = new Scanner(file);
	
			int rows = input.nextInt();
			int cols = input.nextInt();

			states = new  int  [rows][];
			for( int i=0; i < rows; i++)
			{
				states[i] = new int[cols];
			}

			//Read initial states from the input file
			for ( int i = 0; i < rows; i++ )
				for ( int j = 0; j < cols; j++ )
				{
					states[i][j] = input.nextInt();
				}	
		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		return states;	
	}
}



class Coordinate
{
	//Constructors
	public Coordinate()
	{
		_x = 0;
		_y = 0;
	}
	public Coordinate(int x, int y)
	{
		_x = x;
		_y = y;
	}

	public final void close()
	{
	}

	public final int getX()
	{
		return _x;
	}
	public final int getY()
	{
		return _y;
	}
	public final void setX(int anX)
	{
		_x = anX;
	}
	public final void setY(int aY)
	{
		_y = aY;
	}

	private int _x;
	private int _y;
}


class Grid
{
	public Grid(int[][] states)
	{
		int r = states.length;
		int c = states[0].length;
		char img;
		rows = r;
		cols = c;

		//Create a 2D array of pointers to squares
		_squares = new Square [r][];

		for (int i = 0; i < r; i++)
		{
			_squares[i] = new Square [c];
		}

		//For each position (i, j) on the grid, add a cell
		for (int i = 0; i < r; i++)
		{
			for (int j = 0; j < c; j++)
			{
				if (states[i][j] == 1) {
					img = LIVE_IMAGE;
				}
				else {
					img = DEAD_IMAGE;
				}
				
				_squares[i][j] = new Cell (this, new Coordinate(i, j), img , 0);
				
			}
		}
	}
	//Destructor
			public final void close()
			{
				for (int i = 0; i < rows; i++)
				{
					for (int j = 0; j < cols; j++)
					{
						_squares[i][j].setImage(DEAD_IMAGE);
					}
				}

			}

			//Display all cells on the grid
			public final void display()
			{
				//Print the images of cells on the grid
				for (int i = 0; i < rows; i++)
				{
					for (int j = 0; j < cols; j++)
					{
						_squares[i][j].print();
					}

					System.out.print("\n");
				}
			}


			//Get statistics of the cells on the grid
			public final void getStats()
			{
				int numLiveCells = 0;
				int numDeadCells = 0;

				for (int i = 0; i < rows; i++)
				{
					for (int j = 0; j < cols; j++)
					{
						if (_squares[i][j].getImage() == LIVE_IMAGE)
						numLiveCells++;
						else
							numDeadCells++;
					}
				}

				System.out.print("The number of live cells = ");
				System.out.print(numLiveCells);
				System.out.print("\n");
				System.out.print("The number of dead cells = ");
				System.out.print(numDeadCells);
				System.out.print("\n");
				System.out.print("Total cells = ");
				System.out.print(numLiveCells + numDeadCells);
				System.out.print("\n");
			}
	
	public final int getRows()
	{
		return rows;
	}

	public final int getCols()
	{
		return cols;
	}
	
	public final Square getSquare(int x, int y)
	{
		if (x < 0 &&  y < 0) return _squares[0][0];
		else if ( x < 0 && (y >= 0 && y < cols)) return _squares[0][y];
		else if ( x < 0 && (y >= 0 && y <= cols)) return _squares[0][y-1];
		else if ((x >= 0 && x < rows) && y < 0) return _squares[x][0];
		else if ((x >= 0 && x <= rows) && y < 0) return _squares[x-1][0];
		else if (x >= rows && y >= cols) return _squares[rows-1][cols-1];
		else if (x >= rows && y < cols) return _squares[rows-1][y];
		else if ( x < rows && y >= cols) return _squares[x][cols-1];
		else return _squares[x][y];
	}
	
	public void run()
	{
		//Run the simulation generation by generation
		for (int n = 1; n <= NUM_RUNS; n++)
		{
			//Update states of cells on the grid
			
			for (int i = 0; i < rows; i++)
			{
				for (int j = 0; j < cols; j++)
				{
						_squares[i][j].update();
				}
			}
			for (int i = 0; i < rows; i++)
			{
				for (int j = 0; j < cols; j++)
				{
						if (_squares[i][j].getLiveInfo() == true)
							_squares[i][j].setImage(LIVE_IMAGE);
						else _squares[i][j].setImage(DEAD_IMAGE);
				
		
				}
			}
			if (n % NUM_DISPLAY == 0)
			{
				System.out.print("\nRuns = ");
				System.out.print(n);
				System.out.print("\n");
				display();
				getStats();
				System.out.print("\n");
			}
		}
	}
	final int NUM_RUNS = 15	;			//The number of runs (generation) to run the simulation
	final int NUM_DISPLAY = 1 ;	                //How frequently to print the grid with cells
	final char LIVE_IMAGE = 'O';			//Image for live cells
	final char DEAD_IMAGE = '-';			//Image for dead cells
	
	private int rows;			  //The number of rows of this grid
	private int cols;			  //The number of columns of this grid
	private Square [][]_squares;  //A 2D array of pointers to squares (parent class of cells)
}


abstract class Square
{
	private Grid _grid;			//The grid where this square lives on 
	private Coordinate _coord;	//Coordinates of this square 

	public Square(Grid aGrid, Coordinate aCoord)
	{
		this._grid = aGrid;
		this._coord = aCoord;
	}

	public Grid getGrid()
	{
		return _grid;
	}
	
	public Coordinate getCoordinate()
	{
		return _coord;
	}
	
	//abstract methods for dynamic binding
    abstract boolean getLiveInfo();
	abstract char getImage();
	abstract void setImage(char img);
	abstract void print();
	abstract void update();
}




class Cell extends Square
{
	//Cell constructor. Initializer will be used to initialize the parent part
	public Cell(Grid aGrid, Coordinate aCoord, char aImage, int f)
	{
	super (aGrid, aCoord);
	this._image = aImage;
	this._lifeSpan = f;
	}

	public final char getImage()
	{
		return _image;
	}

	public final void setImage(char img)
	{
		_image = img;
	}

	public final boolean getLiveInfo()
	{
		return _toBeLive;
	}

	public final void setLiveInfo(boolean lf)
	{
		_toBeLive = lf;
	}
	
	//Count how many live cells among the eight neighbors using the rules
	public final int countLiveNeighbors()
	{
		Square s = this;
		Coordinate coord = this.getCoordinate();
		
		int num = 0;
		int x =coord.getX();
		int y =coord.getY();
		Grid g = getGrid();
		int rows = g.getRows();
		int cols = g.getCols();
		
		s.getGrid();
		g.getSquare(x, y);
		
				if (g.getSquare(x-1, y-1).getImage() == LIVE_IMAGE)
				{
					num++;
				}
				
				if (g.getSquare(x, y-1).getImage() == LIVE_IMAGE) {
					num++;
				}
				if (g.getSquare(x+1, y-1).getImage() == LIVE_IMAGE) {
					num++;
				}
				if (g.getSquare(x-1, y).getImage() == LIVE_IMAGE) {
					num++;
				}
				if (g.getSquare(x+1, y).getImage() == LIVE_IMAGE) {
					num++;
				}
				if (g.getSquare(x-1, y+1).getImage() == LIVE_IMAGE) {
					num++;
				}
				if (g.getSquare(x, y+1).getImage() == LIVE_IMAGE) {
					num++;
				}
				if (g.getSquare(x+1, y+1).getImage() == LIVE_IMAGE) {
					num++;
				}

		return num;
	}


	//When a cell becomes dead, _lifeSpan will be reset to zero
	public final void resetLifeSpan()
	{
		_lifeSpan = 0;
	}


	//If a cell continues to be live, _lifeSpan will increment by one
	public final void incrementLiveSpan()
	{
		_lifeSpan++;
	}


	//Based on the number of live neighbor cells, update the state of each cell
	//for next generation (run) using the rules
	public final void update()
	{
		int liveNeighbors = countLiveNeighbors();
		if (liveNeighbors < 2 && this.getImage() == LIVE_IMAGE) {
			this.resetLifeSpan();
			//this.setImage(DEAD_IMAGE);
			this.setLiveInfo(false);
		}
		else if ((liveNeighbors == 2 || liveNeighbors == 3) && this.getImage() == LIVE_IMAGE) {
			this.incrementLiveSpan();
			//this.setImage(LIVE_IMAGE);
			this.setLiveInfo(true);
		}
		else if (liveNeighbors > 3 && this.getImage() == LIVE_IMAGE) {
			this.resetLifeSpan();
			//this.setImage(DEAD_IMAGE);
			this.setLiveInfo(false);
		}
		else if (liveNeighbors == 3 && this.getImage() == DEAD_IMAGE) {
			this.incrementLiveSpan();
			//this.setImage(LIVE_IMAGE);
			this.setLiveInfo(true);
		}
	}


	//Print out the image of this cell
	public final void print()
	{
		System.out.print(_image);
		System.out.print(" ");
	}
	
	final char LIVE_IMAGE = 'O';			//Image for live cells
	final char DEAD_IMAGE = '-';			//Image for dead cells

	private char _image;	//For example: cell: O, square: -
	private int _lifeSpan;  //The number of generations this cell has lived
	private boolean _toBeLive; //True if this cell will be live next generation
}
/*
 - - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - O O O - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 29
The number of dead cells = 371
Total cells = 400

Runs = 1
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 25
The number of dead cells = 375
Total cells = 400


Runs = 2
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - O O O - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - O O - O O - - - - - - - - 
- - - - - - O O O - O O O - - - - - - - 
- - - - - - O O O - O O O - - - - - - - 
- - - - - - O O O - O O O - - - - - - - 
- - - - - - O O O - O O O - - - - - - - 
- - - - - - - O O - O O - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 47
The number of dead cells = 353
Total cells = 400


Runs = 3
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - O - - - - - O - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - O - - - - - - - O - - - - - - 
- - - - - O - - - - - - - O - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - O - - - - - O - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 25
The number of dead cells = 375
Total cells = 400


Runs = 4
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - O O O - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - O O O O O - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - O O O O O - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 25
The number of dead cells = 375
Total cells = 400


Runs = 5
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 27
The number of dead cells = 373
Total cells = 400


Runs = 6
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - O O O - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - O - O - O - - - - - - - - 
- - - - - - - O - O - O - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - O - O - O - - - - - - - - 
- - - - - - - O - O - O - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 35
The number of dead cells = 365
Total cells = 400


Runs = 7
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - O O O - - - - - - O - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - O - - - O - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 27
The number of dead cells = 373
Total cells = 400


Runs = 8
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - O O O - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- O O - - - - - - O - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 27
The number of dead cells = 373
Total cells = 400


Runs = 9
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 29
The number of dead cells = 371
Total cells = 400


Runs = 10
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - O O O - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 25
The number of dead cells = 375
Total cells = 400


Runs = 11
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - O O O - - - - - - O - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 29
The number of dead cells = 371
Total cells = 400


Runs = 12
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - O O O - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- O O - - - - - - O - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 27
The number of dead cells = 373
Total cells = 400


Runs = 13
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - O - - - - - - - O - - 
- - - - - - - - - O - - - - - - - O - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- O O - - - - - - O - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 23
The number of dead cells = 377
Total cells = 400


Runs = 14
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - O O O - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - O - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 19
The number of dead cells = 381
Total cells = 400


Runs = 15
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - O - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - O - O - - - - - - - - - 
- - - - - - - - O O O - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- O O - - - - - - - - - - - - - - - - - 
- - - - - - - - - - - - - - - - - - - - 
The number of live cells = 29
The number of dead cells = 371
Total cells = 400
 */


