<?php

    session_start();

    $srcfile = 'arquivos/new_template.json';
    $destfile = 'arquivos/template.json';

    $reset_result = "Template file not exist.";
    
    if ( file_exists($srcfile) ){
        unlink($srcfile); 
        $reset_result = "new_template.json removed.";
    }
    if ( file_exists($destfile) ){
        unlink($destfile); 
        $reset_result = $reset_result . " template.json removed.";
    }
    $_SESSION['reset_result'] = $reset_result;
    
    echo $_SESSION['reset_result'];
?>