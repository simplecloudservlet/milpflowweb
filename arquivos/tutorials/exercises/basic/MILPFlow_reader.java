/*
   MILPFlow_reader
   Description: reader of template.json
   Author: Lucio A. Rocha
   Last update: 13/11/2021

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
    public static String PATH              = "/var/www/html/arquivos/";
    private static String P4_PATH = PATH + "tutorials/exercises/basic/";
    public static String TEMPLATE_FILENAME = P4_PATH + "template.json";

    private StringBuffer template;

    private ArrayList<P4_Host> list_p4_hosts;
    private ArrayList<P4_Switch> list_p4_switches;
    
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

	    //writeJSON(topology);
	    
	} catch (IOException io){
	    System.out.println("Exception reading template from file: " + io.getMessage());
	    io.printStackTrace();
	} catch (Exception e){
	    System.out.println("Uncaught Exception reading template from file: " + e.getMessage());
	    e.printStackTrace();
	}
	
    }//end readTopology

    public class P4_Host {
	private String id, ip, mask, mac;
	private ArrayList<String> list_commands;

	public P4_Host(String id, String ip, String mask, String mac, ArrayList<String> list_commands){
	    this.id=id;
	    this.ip=ip;
	    this.mask=mask;
	    this.mac=mac;
	    this.list_commands = list_commands;
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
    }//end P4_Table
    
    public class P4_Switch {
	private String id;
	private ArrayList<P4_Table> list_p4_table;

	public P4_Switch(String id, ArrayList<P4_Table> list_p4_table){
	    this.id=id;
	    this.list_p4_table=list_p4_table;
	}
	public String getId(){
	    return this.id;
	}
    }//end P4_Switch class
    
    public MILPFlow_reader(){

	readTemplate();

	list_p4_hosts = new ArrayList<>();
	
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

	     list_p4_hosts.add(new P4_Host(id, ip, mask, mac, list_commands));
	    
	}//end for

	//---
	list_p4_switches = new ArrayList<>();
	
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
	    list_p4_switches.add(p4_switch);
						
	}//end for
	
	for( P4_Switch p4_switch : list_p4_switches )
	    System.out.println(p4_switch.getId());
	
	
    }//end constructor

    public static void main(String[] args) {
	new MILPFlow_reader();     
    }
}
