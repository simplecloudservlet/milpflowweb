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
            
        },
        {
            \"id\": \"h3\",
            \"ip\": \"10.0.3.3\",
            \"mask\": \"24\",
            \"mac\": \"08:00:00:00:03:33\",
	    \"commands\": [
		{
		    \"size\": \"2\",
		    \"0\": \"route add default gw 10.0.3.30 dev eth0\",
		    \"1\": \"arp -i eth0 -s 10.0.3.30 08:00:00:00:03:00\"
		}
	    ]
            
        },
        {
            \"id\": \"h4\",
            \"ip\": \"10.0.4.4\",
            \"mask\": \"24\",
            \"mac\": \"08:00:00:00:04:44\",
	    \"commands\": [
		{
		    \"size\": \"2\",
		    \"0\": \"route add default gw 10.0.4.40 dev eth0\",
		    \"1\": \"arp -i eth0 -s 10.0.4.40 08:00:00:00:04:00\"
		}
	    ]
            
        },
        {
            \"id\": \"h5\",
            \"ip\": \"10.0.5.5\",
            \"mask\": \"24\",
            \"mac\": \"08:00:00:00:05:55\",
	    \"commands\": [
		{
		    \"size\": \"2\",
		    \"0\": \"route add default gw 10.0.5.50 dev eth0\",
		    \"1\": \"arp -i eth0 -s 10.0.5.50 08:00:00:00:05:00\"
		}
	    ]
            
        }
    ],
    \"switches\" : [
	{
	    \"switch_id\": \"s1\",
	    \"num_tables\": \"5\",
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
		    \"mac_dstAddr\": \"08:00:00:00:02:22\",
		    \"port\": \"2\" 
		}
	    ],
        \"table2\": [
		{
		    \"ipv4_dstAddr\": \"10.0.3.3\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:03:00\",
		    \"port\": \"3\" 
		}
	    ],
	    \"table3\": [
		{
		    \"ipv4_dstAddr\": \"10.0.4.4\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:04:00\",
		    \"port\": \"4\" 
		}
	    ],
	    \"table4\": [
		{
		    \"ipv4_dstAddr\": \"10.0.5.5\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:05:55\",
		    \"port\": \"5\" 
		}
	    ]
	},
    {
	\"switch_id\": \"s2\",
	    \"num_tables\": \"5\",
	    \"table0\": [
		{
		    \"ipv4_dstAddr\": \"10.0.1.1\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:03:00\",
		    \"port\": \"4\" 
		}
	    ],
	    \"table1\": [
		{
		    \"ipv4_dstAddr\": \"10.0.2.2\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:04:00\",
		    \"port\": \"3\" 
		}
	    ],
        \"table2\": [
		{
		    \"ipv4_dstAddr\": \"10.0.3.3\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:03:33\",
		    \"port\": \"1\" 
		}
	    ],
	    \"table3\": [
		{
		    \"ipv4_dstAddr\": \"10.0.4.4\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:04:44\",
		    \"port\": \"2\" 
		}
	    ],
	    \"table4\": [
		{
		    \"ipv4_dstAddr\": \"10.0.5.5\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:03:00\",
		    \"port\": \"4\" 
		}
	    ]
	},
    {
	\"switch_id\": \"s3\",
	    \"num_tables\": \"5\",
	    \"table0\": [
		{
		    \"ipv4_dstAddr\": \"10.0.1.1\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:01:00\",
		    \"port\": \"1\" 
		}
	    ],
	    \"table1\": [
		{
		    \"ipv4_dstAddr\": \"10.0.2.2\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:01:00\",
		    \"port\": \"1\" 
		}
	    ],
        \"table2\": [
		{
		    \"ipv4_dstAddr\": \"10.0.3.3\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:02:00\",
		    \"port\": \"2\" 
		}
	    ],
	    \"table3\": [
		{
		    \"ipv4_dstAddr\": \"10.0.4.4\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:02:00\",
		    \"port\": \"2\" 
		}
	    ],
	    \"table4\": [
		{
		    \"ipv4_dstAddr\": \"10.0.5.5\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:01:00\",
		    \"port\": \"1\" 
		}
	    ]
	},
    {
	\"switch_id\": \"s4\",
	    \"num_tables\": \"5\",
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
		    \"mac_dstAddr\": \"08:00:00:00:01:00\",
		    \"port\": \"2\" 
		}
	    ],
        \"table2\": [
		{
		    \"ipv4_dstAddr\": \"10.0.3.3\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:02:00\",
		    \"port\": \"1\" 
		}
	    ],
	    \"table3\": [
		{
		    \"ipv4_dstAddr\": \"10.0.4.4\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:02:00\",
		    \"port\": \"1\" 
		}
	    ],
	    \"table4\": [
		{
		    \"ipv4_dstAddr\": \"10.0.5.5\",
		    \"ipv4_dstMask\": \"32\",
		    \"mac_dstAddr\": \"08:00:00:00:01:00\",
		    \"port\": \"2\" 
		}
	    ]
	}
    ],
    \"links\": [
	{
	    \"link\": \"h1|s1-p1\"
	},
	{
	    \"link\": \"h2|s1-p2\"
	},
    {
	    \"link\": \"s1-p3|s3-p1\"
	},
    {
	    \"link\": \"s1-p4|s4-p2\"
	},
    {
	    \"link\": \"h3|s2-p1\"
	},
    {
	    \"link\": \"h4|s2-p2\"
	},
    {
	    \"link\": \"s2-p3|s4-p1\"
	},
    {
	    \"link\": \"s2-p4|s3-p2\"
	},
    {
	    \"link\": \"h5|s1-p5\"
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