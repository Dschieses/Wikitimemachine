<?php

	require_once "include/class.wtmInterfaceV1.php";
	require_once "include/class.wtmInterfaceV2.php";

	if($_GET["v"] == "1") {
		$interface = new wtmInterfaceV1();

		if(isset($_GET["year"])) { $interface->setYear($_GET["year"]); }
		if(isset($_GET["lang"])) { $interface->setLang($_GET["lang"]); }
		if(isset($_GET["limit"])) { $interface->setLimit($_GET["limit"]); }
		if(isset($_GET["order"])) { $interface->setOrder($_GET["order"]); }

		$interface->printJson();
	}
	else {
		$interface = new wtmInterfaceV2();

		if(isset($_GET["year"])) { $interface->setYear($_GET["year"]); }
		if(isset($_GET["lang"])) { $interface->setLang($_GET["lang"]); }
		if(isset($_GET["cat"])) { $interface->setCat($_GET["cat"]); }
		if(isset($_GET["limit"])) { $interface->setLimit($_GET["limit"]); }
		if(isset($_GET["order"])) { $interface->setOrder($_GET["order"]); }

		$interface->printJson();
	}

?>