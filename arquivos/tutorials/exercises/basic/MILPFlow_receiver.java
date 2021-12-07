
import java.io.*;
import java.util.*;
import java.nio.file.*;

public class MILPFlow_receiver {

    private static String PATH                     = "/var/www/html/arquivos/";
    private static String BATCH_FILENAME =    PATH + "batch.txt";
    private static String DONE_FILENAME =     PATH + "done.txt";
    private static String TOPOLOGY_FILENAME = PATH + "topology.txt";

    private static String P4_PATH = PATH + "tutorials/exercises/basic/";

    private static String MYTOPO_PATH = P4_PATH + "mytopo/";
    private static String MYTOPO_JSON = MYTOPO_PATH + "topology.json";

    
    private File BATCH;
    private File DONE;
    private File TOPOLOGY;
    private File TOPOLOGY_JSON;

    public interface IP4_Topology {
	public abstract StringBuffer getElements();
    }

    
    //Internal class: 'static' to be visible only inside the main class

    //Design Pattern: Interface
    public static class P4_Host implements IP4_Topology{

	private String name;
	private String ip;
	private String mac;
	
	public P4_Host(StringBuffer topology){
	    
	}
	public StringBuffer getElements(){
	    StringBuffer elements = new StringBuffer();
	    elements.append("\"h1\": {\"ip\": \"10.0.1.1/24\",\n\"mac\": \"08:00:00:00:01:11\",\n\"commands\":[\"route add default gw 10.0.1.10 dev eth0\",\n\"arp -i eth0 -s 10.0.1.10 08:00:00:00:01:66\"]}\n");
	    
	    return elements;
	}
	
    }//end of P4_Host internal class

    public static class P4_Switch implements IP4_Topology{

	private String name;
	private String ip;
	private String mac;
	
	public P4_Switch(StringBuffer topology){
	    
	}
	public StringBuffer getElements(){
	    StringBuffer elements = new StringBuffer();
	    elements.append("\"s1\": { \"runtime_json\" : \"pod-topo2/s1-runtime.json\" }\n");	    
	    return elements;
	}
	
    }//end of P4_Switch internal class

public static class P4_Link implements IP4_Topology{

	private String name;
	private String ip;
	private String mac;
	
	public P4_Link(StringBuffer topology){
	    
	}
	public StringBuffer getElements(){
	    StringBuffer elements = new StringBuffer();
	    elements.append("[\"h1\", \"s1-p1\"]");
	    
	    return elements;
	}
	
    }//end of P4_Link internal class
    
    public void readTopology(){

	try {
	    String line;
	    StringBuffer topology = new StringBuffer();
	    BufferedReader reader = new BufferedReader(new FileReader(TOPOLOGY_FILENAME));
	    while((line = reader.readLine())!=null){
		//System.out.println(line);
		topology.append(line+"\n");
	    }
	    System.out.println("Topology: " + topology.toString());

	    writeJSON(topology);
	    
	} catch (IOException io){
	    System.out.println("Exception reading topology from file: " + io.getMessage());
	    io.printStackTrace();
	} catch (Exception e){
	    System.out.println("Uncaught Exception reading topology from file: " + e.getMessage());
	    e.printStackTrace();
	}
	
    }//end readTopology

    public void writeJSON(StringBuffer topology){

	try {
	    //Create a new directory for topology
	    File f = new File(MYTOPO_PATH);
	    if (! f.exists() ){
		f.mkdir();
		System.out.println(MYTOPO_PATH + " directory created.");
	    
		FileWriter topologyJSON = new FileWriter(MYTOPO_JSON, false); //no append

		//Begin JSON file
		topologyJSON.write("{\n");

		//Hosts
		topologyJSON.write("\"hosts\": {\n");
		P4_Host p4_host = new P4_Host(topology);
		topologyJSON.write(p4_host.getElements().toString());
		topologyJSON.write("},\n");

		//Switches
		topologyJSON.write("\"switches\": {\n");
		P4_Switch p4_switch = new P4_Switch(topology);
		topologyJSON.write(p4_switch.getElements().toString());
		topologyJSON.write("},\n");

		//Links
		topologyJSON.write("\"links\": [\n");
		P4_Link p4_link = new P4_Link(topology);
		topologyJSON.write(p4_link.getElements().toString());
		topologyJSON.write("]\n");

		//End JSON file
		topologyJSON.write("}\n");

		topologyJSON.close();
	    } else {
		System.out.println(MYTOPO_PATH + " directory NOT created. Directory not empty.");
		throw new Exception();
	    }
	} catch (IOException io){
	    System.out.println("Exception writing topology to file: " + io.getMessage());
	    io.printStackTrace();
	} catch (Exception e){
	    System.out.println("Uncaught Exception writing topology to file: " + e.getMessage());
	    e.printStackTrace();
	}
	

    }//end writeJSON

    public void about(){
	System.out.println("--------------------------------------------------------");
	System.out.println("MILPFlow Receiver (Java version 1.0) - Since: 23-08-2021");
	System.out.println("Author: Lucio A. Rocha");
	System.out.println("Last update: 06-10-2021");
	System.out.println("--------------------------------------------------------");
    }
    
    public void wait(int time){
	try {
	    Thread t = new Thread();
	    t.sleep(1000);
	} catch (Exception e){
	    e.printStackTrace();
	    e.getMessage();
	}
    }

    public void clean(){

	try {
	    BATCH = new File(BATCH_FILENAME);
	    if ( Files.deleteIfExists(BATCH.toPath()) )
		 System.out.println("Batch file removed.");

	    DONE = new File(DONE_FILENAME);
	    if ( Files.deleteIfExists(DONE.toPath()) )
		 System.out.println("Done file removed.");
	    
	    TOPOLOGY = new File(TOPOLOGY_FILENAME);
	    if ( Files.deleteIfExists(TOPOLOGY.toPath()) )
		 System.out.println("Topology file removed.");
	    //Remove MYTOPO_PATH and all files inside it.
	    TOPOLOGY_JSON = new File(MYTOPO_PATH);
	    String[]entries = TOPOLOGY_JSON.list();
	    if ( entries != null ){
		for(String s: entries){
		    File currentFile = new File(TOPOLOGY_JSON.getPath(),s);
		    if ( currentFile.delete() )
			System.out.println(currentFile.getName() + " removed.");
		}
		if ( TOPOLOGY_JSON.exists() ){
		    TOPOLOGY_JSON.delete();
		    System.out.println(MYTOPO_PATH + " directory removed.");
		} else {
		    System.out.println("Error removing directory: " + TOPOLOGY_JSON);
		    throw new Exception();
		}
	    }//end if
	    
	} catch (Exception e){
	    e.printStackTrace();
	    System.out.println(e.getMessage());
	}
    }//end clean

    //Source: https://stackoverflow.com/questions/804951/is-it-possible-to-read-from-a-inputstream-with-a-timeout
    public int readInputStreamWithTimeout(InputStream is, byte[] b, int timeoutMillis)
	throws IOException  {
	int bufferOffset = 0;
	long maxTimeMillis = System.currentTimeMillis() + timeoutMillis;
	while (System.currentTimeMillis() < maxTimeMillis && bufferOffset < b.length) {
	    int readLength = java.lang.Math.min(is.available(),b.length-bufferOffset);
	    // can alternatively use bufferedReader, guarded by isReady():
	    int readResult = is.read(b, bufferOffset, readLength);
	    if (readResult == -1) break;
	    bufferOffset += readResult;
	}
	return bufferOffset;
    }

    
    public void init_p4(){

	//init_p4 can be tiny Bash program inside the P4 project folder
	//to start the "make run" command
	System.out.println("\nInitiate P4Runtime in: " + P4_PATH);

	try {

	    //Needs to update topology before run P4Runtime

	    
	    Runtime runtime = Runtime.getRuntime();
	    //Process process = runtime.exec("env"); //OK
	    //Process process = runtime.exec(P4_PATH+"call_p4"); //OK, bash and C
	    Process process = runtime.exec("make"); //OK

	    //Source: https://stackoverflow.com/questions/804951/is-it-possible-to-read-from-a-inputstream-with-a-timeout
	    try{
		BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
		//InputStream in = process.getInputStream();
		byte[] bytes = new byte[1024];
		int len;
		//while((len = in.read(bytes)) != -1 ){
		String str = new String();
		long maxTimeMillis = System.currentTimeMillis() + 5000; //This timeout is to avoid reach Mininet terminal
		while((str = in.readLine())!=null && System.currentTimeMillis() < maxTimeMillis){
		    //System.out.write(bytes,0,len);
		    System.out.println("["+str+"]");
		}
		System.out.println("Mininet initiated.");
		//System.out.println("Process exited with: " + process.waitFor());

		readTopology();
		
	    } catch (Exception e){
		e.printStackTrace();
		System.out.println("1.Exception initiating P4Runtime: [" + e.getMessage() + "]");
	    }
	}catch (Exception e ){
	    	e.printStackTrace();
		System.out.println("2.Exception: [" + e.getMessage() + "]");
	}
    }//end init_p4

    public void close_p4(){

	//close_p4 is a tiny Bash program inside the P4 project folder
	//to start the "make run" command
	System.out.println("\nClose P4Runtime in: " + P4_PATH);

	try {
	    Runtime runtime = Runtime.getRuntime();
	    Process process = runtime.exec(P4_PATH+"close_p4.sh");
	    try(InputStream in = process.getInputStream();){
		byte[] bytes = new byte[4096];
		int len;
		while((len = in.read(bytes)) != -1){
		    System.out.write(bytes,0,len);
		}
		
		System.out.println("Process exited with: " + process.waitFor());
	    } catch (Exception e){
		e.printStackTrace();
		System.out.println("1.Exception closing P4Runtime: [" + e.getMessage() + "]");
	    }
	}catch (Exception e ){
	    	e.printStackTrace();
		System.out.println("2.Exception: [" + e.getMessage() + "]");
	}
    }//end close_p4
    
    public MILPFlow_receiver(){
	about();
	clean();
	String symbol = new String("|/-\\");
	
	int i=0, j=0, k=1;
	while ( ! BATCH.exists() ){
	    for(i=0;i<4;i++){
		System.out.print("\rWaiting for batch files..."+symbol.charAt(i++));
		wait(1000);
		if ( BATCH.exists() ){
		    k=1;
		    System.out.print("\nReceived batch file at: " + new Date().toString() );

		    init_p4();

		    while ( ! DONE.exists() ){
			j=0;
			System.out.print("\n[");
			while (j<20 && ! DONE.exists() ){
			    System.out.print(".");
			    wait(1);
			    j++;	    
			}//end while
			System.out.print(k+"]");
			k++;
		    }//end while
		    close_p4();
		    System.out.println("Done!");
		    System.out.println("\nBatch terminated at: " + new Date().toString() );
		    clean();
		}//end if
		    
	    }//end for
	    
	}//fim while
	    
    }//fim construtor

    
    public static void main(String [] args){
	new MILPFlow_receiver();

    }
    
}
