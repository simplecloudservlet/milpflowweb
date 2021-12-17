/*
   MILPFlow_reader
   Description: 
      Reader of template.json and
       Create topology.json and sX-runtime.json files.
      
   Author: Lucio A. Rocha
   Last update: 15/12/2021

   How to compile/run: 
   # javac -cp .:gson-2.8.2.jar MILPFlow_reader.java
   # java -cp .:gson-2.8.2.jar MILPFlow_reader
 */

import java.io.*;
import java.util.*;
import java.nio.file.*;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class MILPFlow_reader {
    private String PATH              = "/var/www/html/arquivos/";
    private String P4_PATH           = PATH + "tutorials/exercises/basic/";
    private String TEMPLATE_FILENAME = P4_PATH + "template.json";

    private StringBuffer template;

    private List_P4_Element LIST_P4_HOSTS;
    private List_P4_Element LIST_P4_SWITCHES;
    private List_P4_Element LIST_P4_LINKS;


    private String TMP_TOPO_PATH = "mytopo2/";
    private String MYTOPO_PATH = P4_PATH + TMP_TOPO_PATH;
    private String MYTOPO_JSON = MYTOPO_PATH + "topology2.json";
   
    private File TOPOLOGY_JSON;

    public void clean(){

	try {
	    
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

    
    public void writeTopology(){

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
		topologyJSON.write(LIST_P4_HOSTS.getElements());
		topologyJSON.write("\n},\n");

		//Switches
		topologyJSON.write("\"switches\": {\n");
		topologyJSON.write(LIST_P4_SWITCHES.getElements());
		topologyJSON.write("\n},\n");

		//Links
		topologyJSON.write("\"links\": [\n");
		topologyJSON.write(LIST_P4_LINKS.getElements());
		topologyJSON.write("\n]\n");

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
	
    }//end writeTopology
    
    public void writeControlPlane(){
	StringBuffer result = new StringBuffer();

	ArrayList<P4_Element> listElement = LIST_P4_SWITCHES.getList();
	Iterator itemSwitch = listElement.iterator();
	P4_Switch p4_switch;
	while ( itemSwitch.hasNext() ){ //Foreach switch
	    p4_switch = (P4_Switch) itemSwitch.next();

	    try {
		FileWriter controlPlaneJSON = new FileWriter(MYTOPO_PATH + p4_switch.getId() + "-runtime.json", false); //no append	
		//Begin JSON file
		controlPlaneJSON.write("{\n");
		
		controlPlaneJSON.append("\"target\": \"bmv2\",\n");
		controlPlaneJSON.append("\"p4info\": \"build/basic.p4.p4info.txt\",\n");
		controlPlaneJSON.append("\"bmv2_json\": \"build/basic.json\",\n");
		controlPlaneJSON.append("\"table_entries\": [\n");
		controlPlaneJSON.append("\t{\n");
		controlPlaneJSON.append("\t\"table\": \"MyIngress.ipv4_lpm\",\n");
		controlPlaneJSON.append("\t\"default_action\": true,\n");
		controlPlaneJSON.append("\t\"action_name\": \"MyIngress.drop\",\n");
		controlPlaneJSON.append("\t\"action_params\": { }\n");
		controlPlaneJSON.append("\t},\n");
		
		ArrayList<P4_Table> list_p4_table = p4_switch.getListTable();
		Iterator itemTable = list_p4_table.iterator();
		P4_Table p4_table;
		while( itemTable.hasNext() ){ //Foreach table entry inside the switch
		    p4_table = (P4_Table) itemTable.next();
		    
		    controlPlaneJSON.append("\t{\n");
		    controlPlaneJSON.append("\t\"table\": \"MyIngress.ipv4_lpm\",\n");
		    controlPlaneJSON.append("\t\"match\": {\n");
		    controlPlaneJSON.append("\t\t\"hdr.ipv4.dstAddr\": [\"" + p4_table.getIpv4DstAddr() + "\", 32]\n");
		    controlPlaneJSON.append("\t},\n");
		    controlPlaneJSON.append("\t\"action_name\": \"MyIngress.ipv4_forward\",\n");
		    controlPlaneJSON.append("\t\"action_params\": {\n");
		    controlPlaneJSON.append("\t\t\"dstAddr\": \"" + p4_table.getMacDstAddr() + "\",\n");
		    controlPlaneJSON.append("\t\t\"port\": " + p4_table.getPort() + "\n");
		    controlPlaneJSON.append("\t\t}\n");
		    controlPlaneJSON.append("\t}");
		    
		    if( itemTable.hasNext() )
			controlPlaneJSON.append(",");
		    
		    controlPlaneJSON.append("\n");
		    
		}//end while

		controlPlaneJSON.append("\n]\n");

		//End JSON file
		controlPlaneJSON.write("}\n");

		//Close file
		controlPlaneJSON.close();
		
	    } catch (IOException io){
		System.out.println("Exception writing control plane to file: " + io.getMessage());
		io.printStackTrace();
	    } catch (Exception e){
		System.out.println("Uncaught Exception writing control plane to file: " + e.getMessage());
		e.printStackTrace();
	    }
	    
	}//end while
	
    }//end writeControlPlane
    
    public void readTemplate(){

	try {
	    String line;
	    template = new StringBuffer();
	    BufferedReader reader = new BufferedReader(new FileReader(TEMPLATE_FILENAME));
	    while((line = reader.readLine())!=null){
		//System.out.println(line);
		template.append(line+"\n");
	    }
	    System.out.println("Template: " + template.toString());
	    
	} catch (IOException io){
	    System.out.println("Exception reading template from file: " + io.getMessage());
	    io.printStackTrace();
	} catch (Exception e){
	    System.out.println("Uncaught Exception reading template from file: " + e.getMessage());
	    e.printStackTrace();
	}
	
    }//end readTemplate


    //Design Pattern: Iterator
    public interface ListIterator {
	public abstract boolean hasNext();
	public abstract Object next();
    }

    public class ItemIterator implements ListIterator {

	private ArrayList<P4_Element> list;
	private int pos=0;

	public ItemIterator(ArrayList<P4_Element> list){
	    this.list=list;
	}
	public boolean hasNext(){
	    boolean result=false;
	    if ( pos < this.list.size() )
		result=true;
	    return result;
	}
	public P4_Element next(){
	    P4_Element item = this.list.get(pos);
	    pos++;
	    return item;
	}
	
    }
    
    public class List_P4_Element {
	public ArrayList<P4_Element> listElement;

	public List_P4_Element(){
	    this.listElement = new ArrayList<>();
	}

	public void add(P4_Element host){
	    listElement.add(host);
	}
	//Return elements in JSON format
	public String getElements(){
	    StringBuffer result = new StringBuffer(); 

	    ItemIterator list = new ItemIterator(listElement);
	    while ( list.hasNext() ){

		//Polymorphism
		P4_Element p4_element = (P4_Element) list.next();
		result.append(p4_element);
		if ( list.hasNext() )
		    result.append(",\n");
	    }
	    return result.toString();
	}
	public ArrayList<P4_Element> getList(){
	    return this.listElement;
	}
    }
    
    public abstract class P4_Element {
	public abstract String toString();
    }
    
    public class P4_Host extends P4_Element {
	private String id, ip, mask, mac;
	private ArrayList<String> list_commands;

	public P4_Host(String id, String ip, String mask, String mac, ArrayList<String> list_commands){
	    this.id=id;
	    this.ip=ip;
	    this.mask=mask;
	    this.mac=mac;
	    this.list_commands = list_commands;
	}
	public String getId(){
	    return this.id;
	}
	public String getIp(){
	    return this.ip;
	}
	public String getMask(){
	    return this.mask;
	}
	public String getMAC(){
	    return this.mac;
	}
	public ArrayList<String> getListCommands(){
	    return this.list_commands;
	}
	public String toString(){
	       //Example: "\"h1\": {\"ip\": \"10.0.1.1/24\",\n\"mac\": \"08:00:00:00:01:11\",\n\"commands\":[\"route add default gw 10.0.1.10 dev eth0\",\n\"arp -i eth0 -s 10.0.1.10 08:00:00:00:01:66\"]}");
	    StringBuffer result = new StringBuffer();
	    result.append("\t\""+getId()+"\": {\"ip\": \""+getIp()+"/"+getMask()+
			  "\",\n\t\"mac\": \""+getMAC()+
			  "\",\n\t\"commands\":[");

	    Iterator list = getListCommands().iterator();
	    while( list.hasNext() ){
		result.append( "\n\t\t\""+ list.next() + "\"");
		if ( list.hasNext() )
		    result.append(",");
	    }
	    result.append("\n\t]}");
	    return result.toString();
	}
	
    }//end P4_Host class
    
    public class P4_Table {
	private String ipv4_dstAddr, ipv4_dstMask, mac_dstAddr, port;
	public P4_Table(String ipv4_dstAddr, String ipv4_dstMask, String mac_dstAddr, String port){
	    this.ipv4_dstAddr=ipv4_dstAddr;
	    this.ipv4_dstMask=ipv4_dstMask;
	    this.mac_dstAddr=mac_dstAddr;
	    this.port=port;
	}
	public String getIpv4DstAddr(){
	    return this.ipv4_dstAddr;
	}
	public String getMacDstAddr(){
	    return this.mac_dstAddr;
	}
	public String getPort(){
	    return this.port;
	}
    }//end P4_Table
    
    public class P4_Switch extends P4_Element {
	private String id;
	private ArrayList<P4_Table> list_p4_table;

	public P4_Switch(String id, ArrayList<P4_Table> list_p4_table){
	    this.id=id;
	    this.list_p4_table=list_p4_table;
	}
	public String getId(){
	    return this.id;
	}
	public ArrayList<P4_Table> getListTable(){
	    return this.list_p4_table;
	}
	public String toString(){
	    StringBuffer result = new StringBuffer();

	    result.append("\t\""+getId()+"\": { \"runtime_json\" : \"" +
			  TMP_TOPO_PATH + getId() +"-runtime.json\"}");
		
	    return result.toString();
	}
    }//end P4_Switch class

    public class P4_Link extends P4_Element {
	private String link;
	private String src;
	private String dst;
	public P4_Link(String link){
	    this.link=link;
	    StringTokenizer token = new StringTokenizer(link,"|");
	    this.src = token.nextToken();
	    this.dst = token.nextToken();
	}
	public String getSrc(){
	    return this.src;
	}
	public String getDst(){
	    return this.dst;
	}
	public String toString(){
	    StringBuffer result = new StringBuffer();

	    result.append("\t[\""+getSrc()+"\", \"" + getDst() + "\"]");
		
	    return result.toString();
	}
    }//end P4_Link class
    
    public void parseTemplate(){

	LIST_P4_HOSTS = new List_P4_Element();
	
	//Source: https://devqa.io/how-to-parse-json-in-java/
	JsonObject jsonObject = new JsonParser().parse(this.template.toString()).getAsJsonObject();

        JsonArray hosts = jsonObject.getAsJsonArray("hosts");
	System.out.println(hosts.size());
	String id, ip, mask, mac;
	for(int i=0;i<hosts.size();i++){
	     id = hosts.get(i).getAsJsonObject().get("id").getAsString();
	     System.out.println(id);
	     ip = hosts.get(i).getAsJsonObject().get("ip").getAsString();
	     System.out.println(ip);
	     mask = hosts.get(i).getAsJsonObject().get("mask").getAsString();
	     System.out.println(mask);
	     mac = hosts.get(i).getAsJsonObject().get("mac").getAsString();
	     System.out.println(mac);
	     
	     JsonArray commands = hosts.get(i).getAsJsonObject().getAsJsonArray("commands");
	     String size_str = commands.get(0).getAsJsonObject().get("size").getAsString();
	     System.out.println(size_str);
	     int num_commands = Integer.parseInt(size_str);
	     
	     ArrayList<String> list_commands = new ArrayList<>();
	     for(int j=0; j<num_commands; j++){
		 String command = commands.get(0).getAsJsonObject().get(j+"").getAsString();
		 System.out.println(command);
		 list_commands.add(command);
	     }//end for

	     LIST_P4_HOSTS.add(new P4_Host(id, ip, mask, mac, list_commands));
	    
	}//end for

	//---
	LIST_P4_SWITCHES = new List_P4_Element();
	
	JsonArray switches = jsonObject.getAsJsonArray("switches");
	int num_switches = switches.size();
	System.out.println(num_switches);
	for(int i=0;i<num_switches;i++){
	    String switch_id = switches.get(i).getAsJsonObject().get("switch_id").getAsString();
	    System.out.println(switch_id);

	    String num_tables_str = switches.get(i).getAsJsonObject().get("num_tables").getAsString();
	    System.out.println(num_tables_str);
	    int num_tables = Integer.parseInt(num_tables_str);
	    ArrayList<P4_Table> list_p4_table = new ArrayList<>();
	    for(int j=0;j<num_tables;j++){
		
		JsonArray table = switches.get(i).getAsJsonObject().getAsJsonArray("table"+j);
		
		String ipv4_dstAddr = table.get(0).getAsJsonObject().get("ipv4_dstAddr").getAsString();
		System.out.println(ipv4_dstAddr);
		String ipv4_dstMask = table.get(0).getAsJsonObject().get("ipv4_dstMask").getAsString();
		System.out.println(ipv4_dstMask);
		String mac_dstAddr = table.get(0).getAsJsonObject().get("mac_dstAddr").getAsString();
		System.out.println(mac_dstAddr);
		String port = table.get(0).getAsJsonObject().get("port").getAsString();
		System.out.println(port);

		P4_Table p4_table = new P4_Table(ipv4_dstAddr, ipv4_dstMask, mac_dstAddr, port);
		list_p4_table.add(p4_table);
	    }//end for

	    P4_Switch p4_switch = new P4_Switch(switch_id, list_p4_table);
	    LIST_P4_SWITCHES.add(p4_switch);
						
	}//end for
	System.out.println(LIST_P4_SWITCHES.getElements());
	    
	//---
	LIST_P4_LINKS = new List_P4_Element();
	
	JsonArray links = jsonObject.getAsJsonArray("links");
	int num_links = links.size();
	System.out.println(num_links);
	String link;
	for(int i=0;i<links.size();i++){
	    link = links.get(i).getAsJsonObject().get("link").getAsString();
	    //System.out.println(link);
	    P4_Link p4_link = new P4_Link(link);
	    LIST_P4_LINKS.add(p4_link);
	}
	System.out.println(LIST_P4_LINKS.getElements());
    }
    
    public MILPFlow_reader(){

	clean();
	
	readTemplate();

	parseTemplate();

	writeTopology();

	writeControlPlane();
	
    }//end constructor

    public static void main(String[] args) {
	new MILPFlow_reader();     
    }
}
