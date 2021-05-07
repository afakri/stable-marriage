import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util .*;
import java.lang.Integer;
/** 
@author Ayman Fakri
*/
 /****************************************************
	*
	*             Ayman Fakri 300120735
	*
    ****************************************************/
public class GaleShapley {
    private Stack<Entry<Integer,String>> Sue;                                       /*Contains unmatched employers*/
    private Integer[] students, employers;                                          /*Contains students and employers matches*/
    private String[] students_names,employers_names;                                /*Contains employers and students name*/
    private Integer[][]A;                                                           /* Contains students ranking*/
    private int n ;                                                                 /*number of employers/students */
    private HashMap<Integer,String> setmatches;                                     /*employer's index and student's name */
    private HeapPriorityQueue<Integer,Integer>[] PQ;                                /*employers ranking */

    /**
	 * Default constructor
	 */
    public GaleShapley(){

    }

    /****************************************************
	*
	*             GaleShapley Methods
	*
    ****************************************************/
    
    /**
	 * initializes all the arrays and priority queue from the file taken as argument 
     * Uses a BufferReader to read through the file
	 * @param filename
	 * @return void
	 */
    public void initialize(String filename) throws IOException{
        Sue= new Stack<Entry<Integer,String>>();
        BufferedReader objReader = new BufferedReader(new FileReader(filename));
        String line = objReader.readLine();
        n = Integer.parseInt(line);
        A = new Integer[n][n];
        employers_names = new String[n];
        students_names = new String[n];
        students = new Integer[n];
        employers = new Integer[n];
        
        for(int i=0;i<n;i++){
            students[i]=new Integer(-1);
            employers[i]=new Integer(-1);
            line = objReader.readLine();
            employers_names[i]=line;
            Sue.push(new Entry(new Integer(i),employers_names[i]));
        }

        for(int i=0;i<n;i++){
            line = objReader.readLine();
            students_names[i]=line;
        }
        /**
         * rankings stocks all the rankings 
         */
        String rankings= "";
        line= objReader.readLine();
        while(line!=null){
            rankings=rankings+line;
            line= objReader.readLine();
            rankings=rankings+" ";
        }
        objReader.close();
        rankings=rankings.replace(","," ");
        String[] rank = rankings.split(" ");
        /**
         * odd indexes represent the student ranking in the matrix given
         */
        int count=1;
        for(int i=0;i<n;i++){
            for(int j=0;j<n;j++){
                A[j][i]=new Integer(Integer.parseInt(rank[count]));
                count=count+2;
            }
        }
        
        PQ = new HeapPriorityQueue[n];
        for(int i=0;i<n;i++){
            PQ[i]= new HeapPriorityQueue<Integer,Integer>(n);
        }
        /**
         * even indexes represent employers ranking
         */
        for(int i=0;i<n;i++){
            count=i*(n*2);
            for(int j=0;j<n;j++){
                PQ[i].insert(new Integer(Integer.parseInt(rank[count])),new Integer(j) );
                count=count+2;
            }
        
        }
    }
    /**
     * executes the Gale-Shapley algorithm
     * @return HashMap containing employer and student matches
     */
    public HashMap<Integer,String> execute(){
        int e,s,e1;
        Entry<Integer,Integer> temp;
        while(!Sue.empty()){
            e = Sue.pop().getKey().intValue();
            temp = PQ[e].removeMin();
            s = temp.getValue().intValue();
            e1=students[s];
            if(students[s]==-1){
                students[s]=e;
                employers[e]=s;
            }
            else if(A[s][e].compareTo(A[s][e1])<0){
                students[s]=e;
                employers[e]=s;
                employers[e1]=-1;
                Sue.push(new Entry<Integer,String>(new Integer(e1), employers_names[e1]));
            }
            else{
                Sue.push(new Entry<Integer,String>(new Integer(e), employers_names[e]));
            }
        }
        setmatches= new HashMap<Integer,String>();
        for(int i=0;i<n;i++){
            setmatches.put(i,students_names[employers[i]]);
        }
        return setmatches;
    }
    /**
     * saves the matches in a .txt file in the same directory
     * @param filename
     * @throws IOException
     */
    public void save(String filename) throws IOException{
        PrintWriter out = new PrintWriter("matches_test_N"+Integer.toString(n)+".txt");
        for(int i=0;i<n;i++){
            out.print("Match "+i+": ");
            out.print(employers_names[i]+" - "+setmatches.get(i));
            out.println();
        }
        out.close();
    }
    
}
