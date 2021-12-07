<?php

    session_start();

    $srcfile = 'arquivos/topology.txt';
    $reset_result = "Topology file not exist.";
    //$valid_history = test_input($_POST["history_area"]);

    if ( file_exists($srcfile) ){
        $bytes_written = file_put_contents($srcfile, ""); 
        if ($bytes_written>=0){
            $reset_result = "Topology reset.";
            //$valid_history = "";
        }
    }
    //$_SESSION['valid_history'] = $valid_history;
    $_SESSION['reset_result'] = $reset_result;
    

    header("Location: index.php");
    exit();

function test_input($data) {
  $data = trim($data);
  $data = stripslashes($data);
  $data = htmlspecialchars($data);
  return $data;
}

?>