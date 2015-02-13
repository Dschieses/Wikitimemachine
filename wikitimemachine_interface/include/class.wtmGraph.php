<?php

class wtmGraph {
 
 	//REQUIRED PARAMETERS
  private $version;
  private $year = 0;
  private $person = 0;
  private $function = "year";

  //OPTIONAL PARAMETER
  private $category = "%";
  private $limit = 25;
  private $order = "indegree";

  //SYSTEM PARAMETER
  private $db_host;
  private $db_user;
  private $db_pass;
  private $db_name;
	private $connection;

	//RESULTS
	private $nodesArr;
	private $linksArr;
  private $categoriesArr;
	private $json;

	//HELPER
	private $pageIds;
	private $mapIds;
  private $sumOrder;
  private $highOrder;

  //QUERIES
  private $nodesQuery = array();
  private $linksQuery = array();

  //SETTER
  public function setDbHost($db_host) {
    if($db_host) { $this->db_host = $db_host; }
  }

  public function setDbUser($db_user) {
    if($db_user) { $this->db_user = $db_user; }
  }

  public function setDbPass($db_pass) {
    if($db_pass) { $this->db_pass = $db_pass; }
  }

  public function setDbName($db_name) {
    if($db_name) { $this->db_name = $db_name; }
  }

  public function setFunction($function) {
    if($function) { $this->function = $function; }
  }

  public function setVersion($version) {
    if($version) { $this->version = $version; }
  }

 	public function setYear($year) {
    if($year) { $this->year = $year; }
 	}

  public function setPerson($person) {
    if($person) { $this->person = $person; }
  }

 	public function setCategory($category) {
    if($category) { $this->category = $category; }
 	}

 	public function setNodes($nodes) {
    if($nodes) { $this->nodes = $nodes; }
 	}

 	public function setOrder($order) {
    if($order) { $this->order = $order; }
 	}

  function __construct() {
  }

 	//MAIN FUNCTIONS
  private function connectToDatabase() {
	  $this->connection = new mysqli($this->db_host, $this->db_user, $this->db_pass, $this->db_name);
  }

  private function constructQueries() {
    $this->nodesQuery["year"][2] = "SELECT pa.pageId, pa.title, pa.birthDate, pa.deathDate, pa.".$this->order." FROM category ca INNER JOIN pagetocategory pc ON ca.categoryId = pc.categoryId INNER JOIN pages pa ON pc.pageId = pa.pageId WHERE ca.categoryTitle LIKE '".utf8_decode($this->category)."' AND pa.birthDate < ".$this->year." AND pa.deathDate > ".$this->year." GROUP BY pa.pageId ORDER BY pa.".$this->order." DESC LIMIT ".$this->nodes;
    $this->nodesQuery["year"][1] = "SELECT pe.id AS pageId, pe.name AS title, pe.year_from AS birthDate, pe.year_to AS deathDate, au.".$this->order." FROM people pe INNER JOIN people_aux au ON pe.id = au.id WHERE pe.year_from < ".$this->year." AND pe.year_to > ".$this->year." ORDER BY au.".$this->order." DESC LIMIT ".$this->nodes;

    $this->categoriesQuery[2] = "SELECT ca.categoryTitle, ca.shortTitle, count FROM (SELECT pc.categoryId, COUNT(pc.categoryId) AS count FROM pages pa INNER JOIN pagetocategory pc ON pa.pageId = pc.pageId WHERE pa.birthDate < ".$this->year." AND pa.deathDate > ".$this->year." GROUP BY pc.categoryId ORDER BY count DESC LIMIT 50) AS categorySelection INNER JOIN category ca ON ca.categoryId = categorySelection.categoryId ORDER BY ca.shortTitle";

    $this->linksQuery[2] = "SELECT fromPageId, toPageId FROM connection WHERE fromPageId IN (".implode(",", $this->pageIds).") AND toPageId IN (".implode(",", $this->pageIds).")";
    $this->linksQuery[1] = "SELECT person_from AS fromPageId, person_to AS toPageId FROM connections_norm WHERE person_from IN (".implode(",", $this->pageIds).") AND person_to IN (".implode(",", $this->pageIds).")";
  }

  private function getNodes() {
    $this->constructQueries();
    $result = $this->connection->query($this->nodesQuery[$this->function][$this->version]);

    $this->pageIds = array();
    $this->sumOrder = 0;
    $this->highOrder = 0;
    $counter = 0;

    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
      $this->nodesArr[$counter]["id"] = $row["pageId"];
      $this->nodesArr[$counter]["name"] = utf8_encode($row["title"]);
      $this->nodesArr[$counter]["group"] = $row[$this->order];
      $this->nodesArr[$counter]["birth"] = $row["birthDate"]; 
      $this->nodesArr[$counter]["death"] = $row["deathDate"]; 
      $this->nodesArr[$counter]["value"] = $row[$this->order]; 

      $this->pageIds[] = $row["pageId"];
      $this->mapIds[$row["pageId"]] = $counter;

      $this->sumOrder = $this->sumOrder + $row[$this->order];
      if($row[$this->order] > $this->highOrder) {
        $this->highOrder = $row[$this->order];
      }

      $counter++;
    }

    $this->calcGroup();
  }

  private function getLinks() {
    $this->constructQueries();
    $result = $this->connection->query($this->linksQuery[$this->version]);

    $counter = 0;
  
    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
      $this->linksArr[$counter]["source"] = intval($this->mapIds[$row["fromPageId"]]);
      $this->linksArr[$counter]["target"] = intval($this->mapIds[$row["toPageId"]]); 
      $counter++;
    }

    $this->calcLinkValue();
  }

  private function getCategories() {
    $this->constructQueries();
    $result = $this->connection->query($this->categoriesQuery[$this->version]);

    $counter = 0;
  
    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
      $this->categoriesArr[$counter]["categoryTitle"] = utf8_encode($row["categoryTitle"]);
      $this->categoriesArr[$counter]["shortTitle"] = utf8_encode($row["shortTitle"]); 
      $counter++;
    }    
  }

  public function printGraph() {
    $this->connectToDatabase();

    $this->getNodes();
    $this->getLinks();

    $this->json["nodes"] = $this->nodesArr;
    $this->json["links"] = $this->linksArr;

    print_r(json_encode($this->json));
  }

  public function printCategories() {
    $this->connectToDatabase();

    $this->getCategories();

    print_r(json_encode($this->categoriesArr));
  }

  //HELPER FUNCTIONS
  private function calcGroup() {
    for($i = 0; $i < count($this->nodesArr); $i++) {
      $this->nodesArr[$i]["group"] = round(($this->nodesArr[$i]["group"] / $this->highOrder) * 10);
    }
  }

  private function calcLinkValue() {
    for($i = 0; $i < count($this->linksArr); $i++) {
      $tempValue = round(($this->nodesArr[$this->linksArr[$i]["target"]]["value"] + $this->nodesArr[$this->linksArr[$i]["source"]]["value"]) / 2);
      if($tempValue > $highValue) {
        $highValue = $tempValue;
      }
    }   

    for($i = 0; $i < count($this->linksArr); $i++) {
      $value = round(($this->nodesArr[$this->linksArr[$i]["target"]]["value"] + $this->nodesArr[$this->linksArr[$i]["source"]]["value"]) / 2);
      $normValue = round(($value / $highValue) * 100);
      $this->linksArr[$i]["value"] = $normValue;
    }  
  }
}

?>