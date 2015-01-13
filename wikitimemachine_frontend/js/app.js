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
    limit: 25
  }

  $(function() {
   
    //Configure Routes
    routie({
      '': function() {
        window.location.href = "#about";
      },  
      'graph': function() {

          //Set Parameters
          options.type = "year";

          //Load HTML Template for Year to Wrapper
          $('#wrapper').html($('#year').html());

          //Initialize Graph
          $('#graph').wtmGraph(options);

          //Initialize Controls for Year
          initControls();
          initYearControls();
      },  
      'person/:id': function(id) {

          //Set Parameters
          options.type = "person";
          options.visualization = "graph";
          options.person = id;

          //Load HTML Template for Person to Wrapper
          $('#wrapper').html($('#person').html());

          //Initialize Graph
          $('#graph').wtmGraph(options);

          //Initialize Controls for Person
          initControls();
          initPersonControls();
      },   
      'about': function() {

        //Load HTML Template for About to Wrapper
        $('#wrapper').html($('#about').html());
      },   
      'imprint': function() {

        //Load HTML Template for Imprint to Wrapper
        $('#wrapper').html($('#imprint').html());
      }
    });
  });


  function initControls() {

    //Load Languages
    $.getJSON("http://localhost/wiki/wtmInterface/index.php?function=language", function(data) {
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

    //Clear Selectbox
    select.html("");

    if(version > 1) {

      //Enable Selectbox
      select.html("").removeAttr("disabled");
      elementsArr.push("<option value='%'>All</option>");

      $.getJSON("http://localhost/wiki/wtmInterface/index.php?function=categories&language="+options.language, function(data) {

        //Add Categories to Array
        $.each(data, function(key,value) {
          console.log(value);
          elementsArr.push("<option value='"+value.categoryTitle+"'>"+value.categoryTitle+"</option>");
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
        version = $("option:selected", this).attr("data-version");

        //Initialize Categories
        initCategories(version);

        //Initialize Graph
        $('#graph').wtmGraph(options);
    });

    //Initialize Control for Categories  
    $("#categorySelect").change(function() {
        options.category = this.value;

        //Initialize Graph
        $('#graph').wtmGraph(options);
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

        //Initialize Graph
        $('#graph').wtmGraph(options);
      }
    });

    //Initialize Control for Visualization  
    $("#visualizationSelect").change(function() {
        options.visualization = this.value;

        //Initialize Graph
        $('#graph').wtmGraph(options);
    });

    //Initialize Control for Limit  
    $("#limitSlider").slider({
      min: 1,
      max: 100,
      value: 25,
      slide: function(event, ui) {

        //Set Limit to Badge
        $("#nodeBadge").html(ui.value);
      },
      stop: function(event, ui) {
        options.limit = ui.value;

        //Initialize Graph
        $('#graph').wtmGraph(options);
      }
    });

    //Initialize Control for Order 
    $("#orderSelect").change(function() {
        options.order = this.value;

        //Initialize Graph
        $('#graph').wtmGraph(options);
    });
  }

  function initPersonControls() {

    $("#testit").click(function() {
      routie('year');
    });

    //Initialize Control for Limit 
    $("#limitSlider").slider({
      min: 1,
      max: 100,
      value: 25,
      slide: function(event, ui) {

        //Set Limit to Badge
        $("#nodeBadge").html(ui.value);
      },
      stop: function(event, ui) {
        options.limit = ui.value;

        //Initialize Graph
        $('#graph').wtmGraph(options);
      }
    });

    //Initialize Control for Order 
    $("#orderSelect").change(function() {
        options.order = this.value;

        //Initialize Graph
        $('#graph').wtmGraph(options);
    });

    //Initialize Control for Visualization 
    $("#visualizationSelect").change(function() {
        options.visualization = this.value;

        //Initialize Graph
        $('#graph').wtmGraph(options);
    });
  }

}(window.jQuery, window, document));
