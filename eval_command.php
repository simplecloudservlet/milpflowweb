<?php

session_start();

// define variables and set to empty values
$nameErr = $websiteErr = "";
$valid_command = $valid_history = $website;
$result = "Invalid input.";

  if (empty($_POST["input_command"])) {
    $nameErr = "Input command is required";
  } else {
    $valid_command = test_input($_POST["input_command"]);
    $result = "Done.";
    // check if name only contains letters and whitespace
    if (!preg_match("/^[a-zA-Z-' ]*$/",$valid_command)) {
      $nameErr = "Only letters and white space allowed";
    }
  }

  if (empty($_POST["history_area"])) {
    $nameErr = "Input command is required";
  } else {
    $valid_history = test_input($_POST["history_area"]);
    // check if name only contains letters and whitespace
    if (!preg_match("/^[a-zA-Z-' ]*$/",$valid_command)) {
      $nameErr = "Only letters and white space allowed";
    }
  }
    
  if (empty($_POST["website"])) {
    $website = "";
  } else {
    $website = test_input($_POST["website"]);
    // check if URL address syntax is valid (this regular expression also allows dashes in the URL)
    if (!preg_match("/\b(?:(?:https?|ftp):\/\/|www\.)[-a-z0-9+&@#\/%?=~_|!:,.;]*[-a-z0-9+&@#\/%=~_|]/i",$website)) {
      $websiteErr = "Invalid URL";
    }
  }

    $_SESSION['valid_history'] = $valid_history . "\n" . $valid_command;

    $_SESSION['valid_command'] = $valid_command;

    $_SESSION['result'] = $result;

    header("Location: index.php");
    exit();


function test_input($data) {
  $data = trim($data);
  $data = stripslashes($data);
  $data = htmlspecialchars($data);
  return $data;
}
?>
