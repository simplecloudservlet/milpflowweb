<?php

    session_start();

    $srcfile = 'arquivos/topology.txt';
    $destfile = 'arquivos/batch.txt';

    if ( file_exists($srcfile) ){
        if(copy($srcfile, $destfile)){
            $send_batch_result = "Batch file sent.";
        }
    } else {
        $send_batch_result = "Batch file not sent. Please, save the topology first.";    
    }
    
    $_SESSION['send_batch_result'] = $send_batch_result;
    header("Location: index.php");
    exit();
 
?>