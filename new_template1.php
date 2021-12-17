<?php

    session_start();
    
    $file = "arquivos/new_template.json";
    $contents = "{
    \"hosts\": [
	{
            \"id\": \"h1\",
	    \"ip\": \"10.0.1.1\",
            \"mask\": \"24\",
            \"mac\": \"08:00:00:00:01:11\",
	    \"commands\": [
		{
		    \"size\": \"2\",
		    \"0\": \"route add default gw 10.0.1.10 dev eth0\",
		    \"1\": \"arp -i eth0 -s 10.0.1.10 08:00:00:00:01:00\"
		}
	    ]
            
        },
	{
            \"id\": \"h2\",
	    \"ip\": \"10.0.2.2\",
            \"mask\": \"24\",
            \"mac\": \"08:00:00:00:02:22\",
	    \"commands\": [
		{
		    \"size\": \"2\",
		    \"0\": \"route add default gw 10.0.2.20 dev eth0\",
		    \"1\": \"arp -i eth0 -s 10.0.2.20 08:00:00:00:02:00\"
		}
	    ]
            
        }
    ],
    \"switches\" : [
	{
	    \"switch_id\": \"s1\",
	    \"num_tables\": \"2\",
	    \"table0\": [
		{
		    \"ipv4_dstAddr\": \"10.0.1.1\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:01:11\",
		    \"port\": \"1\" 
		}
	    ],
	    \"table1\": [
		{
		    \"ipv4_dstAddr\": \"10.0.2.2\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:02:00\",
		    \"port\": \"2\" 
		}
	    ]
	},
	{
	    \"switch_id\": \"s2\",
	    \"num_tables\": \"2\",
	    \"table0\": [
		{
		    \"ipv4_dstAddr\": \"10.0.1.1\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:01:00\",
		    \"port\": \"2\" 
		}
	    ],
	    \"table1\": [
		{
		    \"ipv4_dstAddr\": \"10.0.2.2\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:02:22\",
		    \"port\": \"1\" 
		}
	    ]
	}
    ],
    \"links\": [
	{
	    \"link\": \"h1|s1-p1\"
	},
	{
	    \"link\": \"h2|s2-p1\"
	},
    {
	    \"link\": \"s1-p2|s2-p2\"
	}
    ]
}";

    $fd = fopen($file,'w');
    if ( $fd ){
        fwrite($fd, $contents);
        fclose($fd);
    }
    
    if ( file_exists($file) ){
        $newtemplate_result = "New template created.";
    }
        
    $_SESSION["newtemplate_result"] = $newtemplate_result;
    echo $_SESSION["newtemplate_result"];
?>