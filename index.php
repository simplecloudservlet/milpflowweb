
<?php
   //https://www.w3schools.com/php/php_sessions.asp 
   session_start();
   
?>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
    <!--<meta charset="utf-8">    
      <meta name="viewport" content="width=device-width, initial-scale=1">  
    -->
        <!-- Prevent GET http favicon -->
        <link rel="icon" href="data:;base64,=">
        <!-- Style CSS -->
        <link rel="stylesheet" href="myUL.css">
        <title>MILPFlow Web Interface</title>
        <script type='text/javascript' src='jquery-1.11.3.min.js'></script>
    </head>
    
    <body>
    
        <div class="main_page">
            <div class="page_header floating_element">
                <span class="floating_element">
                    MILPFlow Web Interface
                </span>
            </div>
            <tt>(Sponsored by ;-)</tt>            
            
            <div class="content_section floating_element">
                
            <!--Begin section -->
                <div class="section_header section_header_red">
                    <div id="about"></div>It works!
                </div>
                <div class="content_section_text">
                    <p> This is the default welcome page used to test the correct operation of the <tt>MILPFlow Web Interface</tt> after installation on Linux systems. Web files are located at <tt>/var/www/html/</tt>) in your HTTP server.
                    </p>
                </div>
                <!--End section-->
                
                <!--Begin section-->
                <div class="section_header">
                    <div id="webinterface"></div>
                    MILPFlow Controller
                </div>
                <div class="content_section_text">
                    
                    <!--w3schools.com-->
                    <button id="botao_reset" onclick="sendReset()">RESET</button>
                    <script>
                        function sendReset() {
                            var xhttp = new XMLHttpRequest();
                            xhttp.onreadystatechange = function() {
                                if (this.readyState == 4 && this.status == 200) {
                                    document.getElementById("debug_area").innerHTML = this.responseText;
                                    document.getElementById("history_area").innerHTML = "";
                                }
                            };
                            xhttp.open("GET", "reset.php", true);
                            xhttp.send();
                        }
                    </script>
                    
                    <button id="botao_newtemplate" onclick="newTemplate()">NEW TEMPLATE</button>
                    <script>
                        function newTemplate() {
                            var xhttp = new XMLHttpRequest();
                            xhttp.onreadystatechange = function() {
                                if (this.readyState == 4 && this.status == 200) {
                                    document.getElementById("debug_area").innerHTML = this.responseText;
                                }
                            };
                            xhttp.open("GET", "new_template.php", true);
                            xhttp.send();
                        }
                    </script>
                    
                    <button id="botao_template" onclick="sendTemplate()">SEND TEMPLATE</button>
                    <script>
                        function sendTemplate() {
                            var xhttp = new XMLHttpRequest();
                            xhttp.onreadystatechange = function() {
                                if (this.readyState == 4 && this.status == 200) {
                                    document.getElementById("debug_area").innerHTML = this.responseText;
                                }
                            };
                            xhttp.open("GET", "send_template.php", true);
                            xhttp.send();
                        }
                    </script>
                    
                    
                    <button id="botao_done" onclick="sendDone()">SEND DONE</button>
                    <script>
                        function sendDone() {
                            var xhttp = new XMLHttpRequest();
                            xhttp.onreadystatechange = function() {
                                if (this.readyState == 4 && this.status == 200) {
                                    document.getElementById("debug_area").innerHTML = this.responseText;
                                }
                            };
                            xhttp.open("GET", "send_done.php", true);
                            xhttp.send();
                        }
                    </script>
                 
                    <br>
                    <br>
                    
                    <form method="post" action="eval_command.php">  
                        MILPFlow(config)# <input type="text" name="input_command" 
                                                 value=""> <input type="submit" name="submit" value="Submit">
                        <br>
                        <br>
                        <!-- Debug -->
                        <textarea id="debug_area" name="debug_area" rows="1" cols="100" readonly><?php echo $_SESSION['result']; ?></textarea>
                        <br>
                        <br>
                        
                        <!--Begin section-->
                        <div class="section_header">
                            <div id="history"></div>
                                Command History
                        </div>
                        <div class="content_section_text">
                            <textarea id="history_area" name="history_area" rows="5" cols="100" readonly><?php echo $_SESSION['valid_history']; ?></textarea>
                        </div>
            
<!--
[6] MILPFlow(config)# add host H1
[5] MILPFlow(config)# add host H2
[4] MILPFlow(config)# add switch S1
[3] MILPFlow(config)# add link h1 s1 p1s
[2] MILPFlow(config)# add link h2 s1 p2
[1] MILPFlow(config)# add link s1 p3 s3 p1
-->
                <!--End section -->
                        
                    </form>

                    <!-- <h2>Last command: <?php echo $_SESSION['valid_command']; ?> </h2> -->
                    
                     
                    <!--<br>
                    <label for="fname">MILPFlow(config)#</label>
                    <input type="text" id="input_command" name="input_command">

                    <button id="botao_history">SEND</button>
                    <script type="application/javascript">
                        $(document).ready(function(){
                            $('#botao_history').click(function(){
                                let contents = $('#history_area').val();
                                $('#history_area').val($('#input_command').val()+'\n'+contents);
                            });         
                        });
                    </script>
                    -->
                    
                    <!-- Tree -->
                    <ul id="myUL">
                        <li><span class="caret">Hosts</span>
                            <ul class="nested">
                                <li><span class="caret">H1</span> 
                                    <ul class="nested"> 
                                        <li>IP:10.0.1.1/24</li>
                                        <li>MAC:08:00:00:00:01:11</li>
                                        <li><span class="caret">COMMANDS</span>
                                            <ul class="nested">
                                                <li>RULE1:route add default gw 10.0.1.10 dev eth0</li>
                                                <li>RULE2:arp -i eth0 -s 10.0.1.10 08:00:00:00:01:00</li>
                                            </ul>
                                        </li>  
                                    </ul>
                                </li>
                            </ul>
                        </li>
                    </ul>                    
                    <script>
                        var toggler = document.getElementsByClassName("caret");
                        var i;

                        for (i = 0; i < toggler.length; i++) {
                            toggler[i].addEventListener("click", function() {
                                this.parentElement.querySelector(".nested").classList.toggle("active");
                                this.classList.toggle("caret-down");
                            });
                        }
                    </script>

                    
                </div>
                <!--End section -->
                
                
                
                <!--Begin section-->
                <div class="section_header">
                    <div id="bugs"></div>
                    Reporting Problems
                </div>
                <div class="content_section_text">
                    <p>Please use the <tt>e-mail</tt> to report bugs: <a href="mailto: luciorocha at utfpr.edu.br">Contact</a>.
                    </p>
                </div>
                <!--End section -->
                
            <!-- End content_section -->
            </div>
            
        <!-- End main_page -->
        </div>
        
        <div class="validator">
        </div>
    </body>
    
</html>

