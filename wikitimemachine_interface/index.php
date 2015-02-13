<?php
	header('Content-Type: text/html; charset=utf-8');

	require_once "include/class.wtmLanguages.php";
	require_once "include/class.wtmGraph.php";

	if(EMPTY($_GET)) {
		echo "ERROR: FUNTION REQUIRED";
	}
	else {
		if($_GET["function"] == "language") {
			$languages = new wtmLanguages();

			$languages->printLanguages();
			unset($languages);
		}
		elseif($_GET["function"] == "person" || $_GET["function"] == "year") {

			$languages = new wtmLanguages();
			$languages->setDatabase($_GET["language"]);

			$interface = new wtmGraph();
			$interface->setDbHost($languages->lg_host);
			$interface->setDbUser($languages->lg_user);
			$interface->setDbPass($languages->lg_pass);
			$interface->setDbName($languages->lg_name);
			$interface->setVersion($languages->version);

			if(isset($_GET["function"])) { $interface->setFunction($_GET["function"]); }
			if(isset($_GET["year"])) { $interface->setYear($_GET["year"]); }
			if(isset($_GET["person"])) { $interface->setPerson($_GET["person"]); }
			if(isset($_GET["category"])) { $interface->setCategory($_GET["category"]); }
			if(isset($_GET["nodes"])) { $interface->setNodes($_GET["nodes"]); }
			if(isset($_GET["order"])) { $interface->setOrder($_GET["order"]); }

			$interface->printGraph();	
			unset($interface);	
		}	
		elseif($_GET["function"] == "categories") {
			$languages = new wtmLanguages();
			$languages->setDatabase($_GET["language"]);

			$interface = new wtmGraph();
			$interface->setDbHost($languages->lg_host);
			$interface->setDbUser($languages->lg_user);
			$interface->setDbPass($languages->lg_pass);
			$interface->setDbName($languages->lg_name);
			$interface->setVersion($languages->version);

			if(isset($_GET["function"])) { $interface->setFunction($_GET["function"]); }
			if(isset($_GET["version"])) { $interface->setVersion($_GET["version"]); }
			if(isset($_GET["year"])) { $interface->setYear($_GET["year"]); }

			$interface->printCategories();	
			unset($interface);	
		}
	}
?>