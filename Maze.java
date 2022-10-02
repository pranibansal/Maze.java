import java.io.*;
import java.util.Stack;


class Data 
{
    public int n;                 // dimension of maze
    public boolean[][] north;     // is there a wall to north of cell i, j
    public boolean[][] east;
    public boolean[][] south;
    public boolean[][] west;
      
    public Data(String filename) {
    	readMaze(filename);
    }
    
    public void readMaze(String filename) {
		try {
			File file = new File(filename); 
			BufferedReader buf = new BufferedReader(new FileReader(file));
			
			String text = buf.readLine();
			n = Integer.parseInt(text);
			
			north = new boolean[n+2][n+2];
        	east  = new boolean[n+2][n+2];
        	south = new boolean[n+2][n+2];
        	west  = new boolean[n+2][n+2];
			
			while ((text = buf.readLine()) != null) {
				String[] tokens = text.split(" ");
				int x = Integer.parseInt(tokens[0]);
				int y = Integer.parseInt(tokens[1]);
				north[x][y] = (tokens[2].equals("1") ? true : false);
				east[x][y]  = (tokens[3].equals("1") ? true : false);
				south[x][y] = (tokens[4].equals("1") ? true : false);
				west[x][y]  = (tokens[5].equals("1") ? true : false);
			}
			
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
}


public class Maze
{
    private int n;                 // dimension of maze
    private boolean[][] visited;
    private boolean done = false;
    private Data data;

    public Maze(Data data) {
    	this.data = data;
        this.n = data.n;
        StdDraw.setXscale(0, n+2);
        StdDraw.setYscale(0, n+2);
    }
    
    public void drawDot(int x, int y, String color) {
    
    	float size = (float)0.25;
    	if (color.equals("RED")) {
    		StdDraw.setPenColor(StdDraw.RED);
    		size = (float)0.375;
    	}
   		else if (color.equals("BLUE"))
    		StdDraw.setPenColor(StdDraw.BLUE);
    	else if (color.equals("GRAY"))
    		StdDraw.setPenColor(StdDraw.GRAY);	 
    	else 
    		StdDraw.setPenColor(StdDraw.BLACK);			
    	
        StdDraw.filledCircle(x + 0.5, y + 0.5, size);
        StdDraw.show();
        StdDraw.pause(200);
        //try{System.in.read();}
        //catch(Exception e){}
    }


	// draw the maze
    public void draw() {

        StdDraw.setPenColor(StdDraw.BLACK);
        for (int x=1; x <= n; x++) {
            for (int y=1; y <= n; y++) {
                if (data.south[x][y]) StdDraw.line(x, y, x+1, y);
                if (data.north[x][y]) StdDraw.line(x, y+1, x+1, y+1);
                if (data.west[x][y])  StdDraw.line(x, y, x, y+1);
                if (data.east[x][y])  StdDraw.line(x+1, y, x+1, y+1);
            }
        }
        StdDraw.show();
        
        drawDot(n, 1, "RED");
        drawDot(1, n, "RED");
    }
    
    private void dfsapproach(int x, int y) {
        if (x == 0 || y == 0 || x == n+1 || y == n+1) return;
        if (done || visited[x][y]) return;
        visited[x][y] = true;

        
        drawDot(x, y, "BLUE");

        // reached end
        if (x == n && y == 1) done = true;

        if (!data.north[x][y]) dfsapproach(x, y + 1);
        if (!data.east[x][y])  dfsapproach(x + 1, y);
        if (!data.south[x][y]) dfsapproach(x, y - 1);
        if (!data.west[x][y])  dfsapproach(x - 1, y);

        if (done) return;
        drawDot(x, y, "GRAY");
    }


    public void solve() {
        visited = new boolean[n+2][n+2];
        
        for (int row = 1; row <= n; row++)
        
            for (int col = n; col >= 1; col--){
                visited[row][col] = false;
                
            }
        
        done = false;
        //dfsapproach(1, n);
        stackapproach();
    }

    public class Node {
        int x;
        int y;
        public Node(int x1, int y1) {x=x1; y=y1;}
        public Node(int x1, int y1, int g1) {
            x=x1;
            y=y1;
            
        }
        public int getX() {return x;}
        public int getY() {return y;}
        
    }
    
    
        
    private void stackapproach() {
        
        Stack<Node> s = new Stack<Node>();
        
        Stack<Node> explored = new Stack<>();
        boolean isRetract = false;
        
        s.push(new Node(1,n));
                    
        while (true) {
            if(s.isEmpty()){ System.out.println("\n\n*****PATH NOT FOUND >.<");break;}
            
            Node temp = s.pop();
            
            int a = temp.getX();
            int b = temp.getY();
             
            visited[a][b] = true;
            drawDot(a, b, "BLUE");

            int curSize = s.size();
            
            if (a==n && b==1){ System.out.println("\n\n*****PATH FOUND!!!");break;}

            if (!data.south[a][b]) {
                Node node = new Node(a,b-1);
                if (!visited[a][b-1])
                    s.push(node);
                               
            }
            if (!data.east[a][b]) {
                Node node = new Node(a+1,b);
                if (!visited[a+1][b])
                    s.push(node);
                               
            }
            if (!data.north[a][b]) {
                Node node = new Node(a,b+1);
                if (!visited[a][b+1])
                    s.push(node);
                               
            }
            if (!data.west[a][b]) {
                Node node = new Node(a-1,b);
                if (!visited[a-1][b])
                    s.push(node); 
                               
            }
            //if 
            if (curSize == s.size()){

                drawDot(a, b, "GRAY");
                //System.out.println(s.peek().getX() + ", " + s.peek().getY());

                isRetract = true;
                if (!explored.isEmpty())

                    s.push(explored.pop());
            }
            else{
                isRetract = false;

            }

            if (!isRetract){
                explored.push(temp);}
                        
        }        
    }
 
        
 
   static public void main (String args[]) { 
       
       if (args.length > 0) {
       	   Data data = new Data(args[0]);
           Maze maze = new Maze(data);
     	
           maze.draw();
           maze.solve();
      }
      else
	       System.out.println("Cannot read data file ...");
   }
}



