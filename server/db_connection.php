<?php
/**
 * Created by PhpStorm.
 * User: Xaver
 * Date: 26.05.15
 * Time: 13:50
 */

function connectToDatabase()
{
    $host = "localhost";
    $user = "root";
    $pw = "";
    $mysqldb = "fussball_app";

    /*
    $host = "xwes.de.mysql";
    $user = "xwes_de";
    $pw = "mpx17em0892";
    $mysqldb = "fußball_app";
    */

    $connection = mysql_connect($host, $user, $pw) or die ("Verbindung fehlgeschlagen");

    mysql_select_db($mysqldb, $connection) or die("DB nicht gefunden");

    return $connection;
}

?>