<?php

class wtmInterfaceV1 {
 
	//REQUIRED PARAMETERS
  private $lang = "en";
  private $year = 2000;

  //OPTIONAL PARAMETER
  private $limit = 100;
  private $order = "indegree";

    //DATABSE PARAMETER
  private $host = "91.250.82.104";
  private $user = "wikipulse";
  private $pass = "COINcourse2014";
  private $db = "wikihistory";
	private $connection;

	//RESULTS
	private $nodes;
	private $links;
	private $json;

	//HELPER
	private $pageIds;
	private $mapIds;

	//INITIAL FUNCTION
	function __construct() {
     $this->connectToDatabase();
 	}

 	//SETTER
 	public function setLang($lang) {
    if($lang) { $this->lang = $lang; }
 	}

 	public function setYear($year) {
    if($year) { $this->year = $year; }
 	}

 	public function setLimit($limit) {
    if($limit) { $this->limit = $limit; }
 	}

 	public function setOrder($order) {
    if($order) { $this->order = $order; }
 	}

 	//MAIN FUNCTIONS
  private function connectToDatabase() {
	  $this->connection = new mysqli($this->host, $this->user, $this->pass, $this->db);
  }

  private function getNodes() {
	  $result = $this->connection->query("SELECT pe.id, pe.name, pe.year_from, pe.year_to, au.".$this->order." FROM people pe INNER JOIN people_aux au ON pe.id = au.id WHERE pe.wiki_language = '".$this->lang."' AND pe.year_from < ".$this->year." AND pe.year_to > ".$this->year." ORDER BY au.".$this->order." DESC LIMIT ".$this->limit);

	  $this->pageIds = array();
	  $counter = 0;

	  while($row = $result->fetch_array(MYSQLI_ASSOC)) {
		  $this->nodes[$counter]["name"] = utf8_encode(str_replace("_"," ",$row["name"]));
		  $this->nodes[$counter]["group"] = 1; 

		  $this->pageIds[] = $row["id"];
  		$this->mapIds[$row["id"]] = $counter;
		  $counter++;
	  }
  }

  private function getLinks() {
  	$result = $this->connection->query("SELECT person_from, person_to FROM connections_norm WHERE person_from IN (".implode(",", $this->pageIds).") AND person_to IN (".implode(",", $this->pageIds).") AND wiki_language = '".$this->lang."'");

	  $counter = 0;
	
	  while($row = $result->fetch_array(MYSQLI_ASSOC)) {
		  $this->links[$counter]["source"] = intval($this->mapIds[$row["person_from"]]);
		  $this->links[$counter]["target"] = intval($this->mapIds[$row["person_to"]]); 
		  $counter++;
	  }
  }

  private function constructJson() {
  	$this->json["nodes"] = $this->nodes;
	  $this->json["links"] = $this->links;
  }

  public function printJson() {
  	$this->getNodes();
  	$this->getLinks();
  	$this->constructJson();

  	print_r(json_encode($this->json));
  }
}

?>