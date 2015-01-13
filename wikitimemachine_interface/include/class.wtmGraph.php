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
    $this->nodesQuery["year"][2] = "SELECT pa.pageId, pa.title, pa.birthDate, pa.deathDate, pa.".$this->order." FROM category ca INNER JOIN pagetocategory pc ON ca.categoryId = pc.categoryId INNER JOIN pages pa ON pc.pageId = pa.pageId WHERE ca.categoryTitle LIKE '".$this->category."' AND pa.birthDate < ".$this->year." AND pa.deathDate > ".$this->year." GROUP BY pa.pageId ORDER BY pa.".$this->order." DESC LIMIT ".$this->nodes;
    $this->nodesQuery["year"][1] = "SELECT pe.id AS pageId, pe.name AS title, pe.year_from AS birthDate, pe.year_to AS deathDate, au.".$this->order." FROM people pe INNER JOIN people_aux au ON pe.id = au.id WHERE pe.year_from < ".$this->year." AND pe.year_to > ".$this->year." ORDER BY au.".$this->order." DESC LIMIT ".$this->nodes;

    $this->nodesQuery["person"][1] = "(SELECT pe.id AS pageId, pa.indegree, pe.name AS title, pe.year_from AS birthDate, pe.year_to AS deathDate FROM people pe INNER JOIN people_aux pa ON pe.id = pa.id WHERE pe.id = ".$this->person.") UNION ALL (SELECT personId AS pageId, ".$this->order.", name AS title, year_from AS birthDate, year_to AS deathDate FROM (SELECT cn.person_to AS personId, pa.indegree, pa.outdegree FROM connections_norm cn INNER JOIN people_aux pa ON cn.person_to = pa.id WHERE cn.person_from = ".$this->person." UNION ALL SELECT cn.person_from AS personId, pa.indegree, pa.outdegree FROM connections_norm cn INNER JOIN people_aux pa ON cn.person_from = pa.id WHERE cn.person_to = ".$this->person.") AS groupedLinks INNER JOIN people ON personId = id WHERE personID != ".$this->person." GROUP BY personId ORDER BY ".$this->order." DESC LIMIT ".$this->nodes.")";
    $this->nodesQuery["person"][2] = "";

    $this->categoriesQuery[2] = "SELECT categoryTitle,shortTitle,count FROM (SELECT categoryId, COUNT(categoryId) AS count FROM pagetocategory GROUP BY categoryId LIMIT 100) AS pagecategory INNER JOIN category ca ON pagecategory.categoryId = ca.categoryId ORDER BY categoryTitle";

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
  }

  private function getCategories() {
    $this->constructQueries();
    $result = $this->connection->query($this->categoriesQuery[$this->version]);

    $counter = 0;
  
    while($row = $result->fetch_array(MYSQLI_ASSOC)) {
      $this->categoriesArr[$counter]["categoryTitle"] = utf8_encode($row["categoryTitle"]);
      //$this->categoriesArr[$counter]["shortTitle"] = $row["shortTitle"]; 
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
}

?>