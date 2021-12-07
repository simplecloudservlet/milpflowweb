<?php

    session_start();
    
    $save_topology_result = "";
    $bytes_written = 0;
    if ($_POST['text'].trim().length >= 0){
    
        $bytes_written = file_put_contents('arquivos/topology.txt', $_POST['text']); 
        if ($bytes_written>=0){
             $save_topology_result = 'Topology saved.';
        }
    }
    $_SESSION['save_topology_result'] = $save_topology_result . $bytes_written;
    header("Location: index.php");
    exit();

?>