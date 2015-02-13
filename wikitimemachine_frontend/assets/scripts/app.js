(function($, window, document) {

  //Define Values for Parameters
  var options = {
    type: "year",
    visualization: "graph",
    year: 0, 
    person: 0,
    language: "en",
    category: "%",
    order: "indegree",
    limit: 125,
    version: 1,
    threshold: 0
  }

  $(function() {
   
    //Set Parameters
    options.type = "year";

    //Initialize Graph
    $('#graph').wtmGraph(options);

    //Initialize Controls for Year
    initControls();
    initYearControls();
  });

  function initControls() {

    //Load Languages
    $.getJSON("/wtm-interface/index.php?function=language", function(data) {
      var langSelect = $("#langSelect");
      var elementsArr = new Array();

      //Add Languages to Array
      $.each(data, function(key,value) {
        elementsArr.push("<option value='"+value.lg_short+"' data-version='"+value.version+"'>"+value.lg_name+"</option>");
      });

      //Add Languages to Selectbox
      langSelect.html(elementsArr.join(""));
    });
  }

  function initCategories(version) {

    var select = $("#categorySelect");
    var elementsArr = new Array();

    //Clear
    select.html("");
    options.category = "%";

    if(version > 1) {

      //Enable Selectbox
      select.html("").removeAttr("disabled");
      elementsArr.push("<option value='%'>All</option>");

      $.getJSON("/wtm-interface/index.php?function=categories&language="+options.language+"&year="+options.year, function(data) {

        //Add Categories to Array
        $.each(data, function(key,value) {

          elementsArr.push("<option value='"+value.categoryTitle+"'>"+value.shortTitle+"</option>");
        });

        //Add Categories to Selectbox
        select.html(elementsArr.join("")); 
      });
    } 
    else {

      //Disable Selectbox
      select.attr("disabled","disable");
    }
  }

  function initYearControls() {
    
    //Initialize Control for Language
    $("#langSelect").change(function() {
        options.language = this.value;
        options.version = $("option:selected", this).attr("data-version");

        //Initialize Categories
        initCategories(options.version);
    });

    //Initialize Control for Categories  
    $("#categorySelect").change(function() {
        options.category = this.value;
    });

    //Initialize Control for Years  
    $("#yearSlider").slider({
      min: -2000,
      max: 2000,
      value: 0,
      slide: function(event, ui) {

        //Set Year to Badge
        $("#yearBadge").html(ui.value);
      },
      stop: function(event, ui) {
        options.year = ui.value;

        //Initialize Categories
        initCategories(options.version);
      }
    });

    //Initialize Control for Visualization  
    $("#visualizationSelect").change(function() {
        options.visualization = this.value;
    });

    //Initialize Control for Limit  
    $("#limitSlider").slider({
      min: 1,
      max: 250,
      value: 125,
      slide: function(event, ui) {

        //Set Limit to Badge
        $("#nodeBadge").html(ui.value);
      },
      stop: function(event, ui) {
        options.limit = ui.value;
      }
    });

    //Initialize Control for Order 
    $("#orderSelect").change(function() {
        options.order = this.value;
    });

    //Initialize Control for Order 
    $("#createGraph").click(function() {
      //Initialize Graph
      $('#graph').wtmGraph(options);
    });

    //Initialize Control for Limit  
    $("#thresholdSlider").slider({
      min: 0,
      max: 100,
      value: 0,
      slide: function(event, ui) {

        //Set Limit to Badge
        $("#thresholdBadge").html(ui.value);
      },
      stop: function(event, ui) {
        options.threshold = ui.value;
      }
    });

  }

}(window.jQuery, window, document));
