<?php

class wtmInterfaceV2 {
 
 	//REQUIRED PARAMETERS
  private $lang = "DE";
  private $year = 2000;

    //OPTIONAL PARAMETER
  private $cat = "%";
  private $limit = 100;
  private $order = "indegree";

    //DATABSE PARAMETER
  private $host = "127.0.0.1";
  private $user = "root";
  private $pass = "";
  private $db = "wikitimemachine";
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

 	public function setCat($cat) {
    if($cat) { $this->cat = $cat; }
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
		$result = $this->connection->query("SELECT pa.pageId, pa.title, pa.birthDate, pa.deathDate, pa.".$this->order." FROM category ca INNER JOIN pagetocategory pc ON ca.categoryId = pc.categoryId INNER JOIN pages pa ON pc.pageId = pa.pageId WHERE ca.lang = '".$this->lang."' AND pc.lang = '".$this->lang."' AND pa.lang = '".$this->lang."' AND ca.categoryTitle LIKE '".$this->cat."' AND pa.birthDate < ".$this->year." AND pa.deathDate > ".$this->year." GROUP BY pa.pageId ORDER BY pa.".$this->order." DESC LIMIT ".$this->limit);
  
		$this->pageIds = array();
		$counter = 0;

		while($row = $result->fetch_array(MYSQLI_ASSOC)) {
			$this->nodes[$counter]["name"] = utf8_encode($row["title"]);
			$this->nodes[$counter]["group"] = 1; 

			$this->pageIds[] = $row["pageId"];
    	$this->mapIds[$row["pageId"]] = $counter;
			$counter++;
		}
  }

  private function getLinks() {
  	$result = $this->connection->query("SELECT fromPageId, toPageId FROM connection WHERE fromPageId IN (".implode(",", $this->pageIds).") AND toPageId IN (".implode(",", $this->pageIds).") AND lang = '".$this->lang."'");

	  $counter = 0;
	
	  while($row = $result->fetch_array(MYSQLI_ASSOC)) {
		  $this->links[$counter]["source"] = intval($this->mapIds[$row["fromPageId"]]);
		  $this->links[$counter]["target"] = intval($this->mapIds[$row["toPageId"]]); 
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