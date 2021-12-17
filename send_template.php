<?php

    session_start();

    $srcfile  = 'arquivos/new_template.json';
    $destfile = 'arquivos/template.json';

    if ( file_exists($srcfile) ){
        if(copy($srcfile, $destfile)){ 
            $send_template_result = "template.json file sent.";
        }
    } else {
        $send_template_result = "template.json file not sent. Please, choose 'new template' first.";    
    }
    
    $_SESSION['send_template_result'] = $send_template_result;
    
    echo $_SESSION['send_template_result'];

?>