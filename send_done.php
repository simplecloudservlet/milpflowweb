<?php
    
    session_start();

    $send_done_result = 'Error sending done.';
    $bytes_written = file_put_contents('arquivos/done.txt', $_POST['text']);
    if ($bytes_written>=0){
        
        $send_done_result = 'Done sent.';
    }
    $_SESSION['send_done_result'] = $send_done_result;
    
    echo $_SESSION['send_done_result'];
?>