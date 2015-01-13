<?php

class wtmLanguages {
 
  //DATABSE PARAMETER
  private $db_host = "127.0.0.1";
  private $db_user = "root";
  private $db_pass = "";
  private $db_name = "wtm";
	private $connection;

  //RESULTS
  private $language;
  private $languages;

  //PUBLIC PARAMETER
  public $lg_host = "";
  public $lg_user = "";
  public $lg_pass = "";
  public $lg_name = "";
  public $version = "";

	//INITIAL FUNCTION
	function __construct() {
     $this->connectToDatabase();
 	}

 	//MAIN FUNCTIONS
  private function connectToDatabase() {
	  $this->connection = new mysqli($this->db_host, $this->db_user, $this->db_pass, $this->db_name);
  }

  private function getLanguages() {
    $result = $this->connection->query("SELECT id, lg_name, lg_short, version FROM languages");

    $counter = 0;

    while($row = $result->fetch_array(MYSQLI_ASSOC)) { 
      $this->languages[$counter]["id"] = $row["id"];
      $this->languages[$counter]["lg_name"] = $row["lg_name"];
      $this->languages[$counter]["lg_short"] = $row["lg_short"];
      $this->languages[$counter]["version"] = $row["version"];

      $counter++;
    }
  }

  public function setDatabase($language) {
    $this->language = $language;

    $result = $this->connection->query("SELECT * FROM languages WHERE lg_short = '".$this->language."'");
    $row = $result->fetch_array(MYSQLI_ASSOC);

    $this->lg_host = $row["db_host"];
    $this->lg_user = $row["db_user"];
    $this->lg_pass = $row["db_pass"];
    $this->lg_name = $row["db_name"];
    $this->version = $row["version"];
  }

  public function printLanguages() {
    $this->getLanguages();
    print_r(json_encode($this->languages));
  }
}

?>