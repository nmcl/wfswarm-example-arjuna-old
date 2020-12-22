import java.io.*;

/*
 * Take the data given and find where the two wires cross.
 * 
 * The size of the circuit board should have been obtained
 * correctly beforehand using something like CircuitSize or
 * TestPlotter. The algorithms which those programs use
 * could be incorporated directly here but for now we'll keep
 * things simple.
 */

public class CircuitBoard
{
    public static final char UP = 'U';
    public static final char DOWN = 'D';
    public static final char LEFT = 'L';
    public static final char RIGHT = 'R';

    public CircuitBoard (int length, int width)
    {
        _length = length;
        _width = width;

        _theBoard = new int[_length][_width];

        for (int i = 0; i < _length; i++)
        {
            for (int j = 0; j < _width; j++)
                _theBoard[i][j] = 0;
        }
    }

    public boolean plotLine (String[] line)
    {
        int xPos = _length/2;  // always start in the centre of the grid
        int yPos = _width/2;

        for (String str : line)
        {
            switch (str.charAt(0))
            {
                case TestPlotter.LEFT:
                {
                    int left = Integer.parseInt(str.substring(1));

                    if (xPos - left >= 0)
                    {
                        for (int i = xPos; i != xPos - left; i--)
                            _theBoard[i][yPos]++;

                        xPos -= left;
                    }
                    else
                    {
                        System.out.println("LEFT "+Integer.parseInt(str.substring(1))+" moved xPos negative!");

                        return false;
                    }
                }
                break;
                case TestPlotter.RIGHT:
                {
                    int right = Integer.parseInt(str.substring(1));

                    if (xPos + right < _length)
                    {
                        for (int i = xPos; i != xPos + right; i++)
                            _theBoard[i][yPos]++;

                        xPos += right;
                    }
                    else
                    {
                        System.out.println("RIGHT "+Integer.parseInt(str.substring(1))+" moved xPos beyond max length!");

                        return false;
                    }
                }
                break;
                case TestPlotter.UP:
                {
                    int up = Integer.parseInt(str.substring(1));

                    if (yPos + up < _width)
                    {
                        for (int i = yPos; i != yPos + up; i++)
                            _theBoard[xPos][i]++;

                        yPos += up;
                    }
                    else
                    {
                        System.out.println("UP "+Integer.parseInt(str.substring(1))+" moved yPos beyond max width!");

                        return false;
                    }
                }
                break;
                case TestPlotter.DOWN:
                {
                    int down = Integer.parseInt(str.substring(1));

                    if (yPos - down >= 0)
                    {
                        for (int i = yPos; i != yPos - down; i--)
                            _theBoard[xPos][i]++;

                        yPos -= down;
                    }
                    else
                    {
                        System.out.println("DOWN "+Integer.parseInt(str.substring(1))+" moved yPos negative!");

                        return false;
                    }
                }
                break;
                default:
                {
                    System.out.println("Unknown instruction: "+str.charAt(0));

                    return false;
                }
            }
        }

        return true;
    }

    public int getDistance ()
    {
        int result = -1;
        int xPos = 0;
        int yPos = 0;

        for (int i = 0; i < _length; i++)
        {
            for (int j = 0; j < _width; j++)
            {
                if (_theBoard[i][j] > 1)
                {
                    System.out.println("Wires cross at <"+i+", "+j+">");
                }
            }
        }

        return -1;
    }

    public final void printBoard ()
    {
        for (int i = 0; i < _length; i++)
        {
            for (int j = 0; j < _width; j++)
            {
                System.out.print(_theBoard[i][j]);
            }

            System.out.println();
        }
    }

    private int[][] _theBoard = null;
    int _length = 0;
    int _width = 0;
}