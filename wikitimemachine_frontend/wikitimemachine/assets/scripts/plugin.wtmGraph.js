$.fn.wtmGraph = function(options){

  //Define Values for Parameters
  options = $.extend({
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
  }, options);

  var div = this.selector;
  var query = constructQuery(options);

  //Display Loader
  $(div).html("<div class='loader'></div>");

  if(options.visualization == "graph") {
    buildGraph(div,query,options);
  } 
  else if(options.visualization == "table") {
    buildTable(div,query,options);
  }
}

function constructQuery(options) {
  var query = "";

  query += "/wtm-interface/index.php";
  query += "?function="+options.type;
  query += "&language="+options.language;
  query += "&year="+options.year;
  query += "&person="+options.person;
  query += "&category="+options.category;
  query += "&order="+options.order;
  query += "&nodes="+options.limit;

  return query;
}

function calculateProperties(nodes,links) {
  $("#nodesBadge").html(nodes.length);
  $("#linksBadge").html(links.length);

  //Graph Density for directed Graphs
  $("#densityBadge").html((links.length / (nodes.length * (nodes.length - 1))).toFixed(2));
}

function buildGraph(div,query,options) {

  //Destroy previous Graphs
  if(typeof svg !== 'undefined') {
    node.remove();
    link.remove();
    svg.remove();
    nodes = [];
    links = [];  
  }

  //Get Data from Interface
  d3.json(query, function(error, graph) {

    graphLinksTemp = graph.links;
    graph.links = new Array();

    for(var i = 0; i < graphLinksTemp.length; i++) {
      if (graphLinksTemp[i].value > options.threshold) {
        graph.links.push(graphLinksTemp[i]);
      }
    }

    $(div).html("");

    var width = $(div).width();
    var height = $(div).height();
    var radius = 6;

    var color = d3.scale.category10();
  
    var force = d3.layout
      .force()
      .charge(-20)
      .friction(0.5)
      .linkStrength(1)
      .gravity(0)
      .linkDistance(300 - (options.limit * 0.9))
      .alpha(0)
      .size([width, height]);

    var zoom = d3.behavior
      .zoom()
      .scaleExtent([1, 10])
      .on("zoom", zoomed);

    var drag = d3.behavior
      .drag()
      .origin(function(d) { return d; })
      .on("dragstart", dragstarted)
      .on("drag", dragged)
      .on("dragend", dragended);

    var svg = d3.select(div)
      .append("svg")
      .attr("width", width)
      .attr("height", height)
      .append("g")
      .call(zoom)
      .on("dblclick.zoom", null);

    var rect = svg
      .append("rect")
      .attr("width", width)
      .attr("height", height)
      .style("fill", "none")
      .style("pointer-events", "all");

    var container = svg.append("g");
                
    force
      .nodes(graph.nodes)
      .links(graph.links)
      .start();
                  
    var link = container
      .append("g")
      .attr("class", "links")
      .selectAll(".link")
      .data(graph.links)
      .enter().append("line")
      .attr("class", "link")
      .style("stroke-width", 0.5);

    var node = container
      .selectAll(".node")
      .data(graph.nodes)
      .enter()
      .append("a")
      .append("circle")
      .attr("data-container", "body")
      .attr("data-toggle", "popover")
      .attr("data-placement", "top")
      .attr("title", function(d) { return d.name.replace(/_/g," "); })
      .attr("data-content", function(d) { return "Birth: " + d.birth + " <br>Death: " + d.death + "<hr>Value: " + d.value + "<hr>Source: <a href='http://" + options.language + ".wikipedia.org/wiki/" + d.name + "' target='_blank'>Wikipedia</a>"; })
      .attr("class","node")
      .attr("r", function(d) { return (d.group + 5) - (options.limit/100); })
      .style("opacity", 1)
      .style("fill", function(d) { return color(d.group); })
      .on("dblclick", connectedNodes);
     
    force.on("tick", function() { 
      link
        .attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

        node
          .attr("cx", function(d) { return d.x = Math.max(radius, Math.min(width - radius, d.x)); }) 
          .attr("cy", function(d) { return d.y = Math.max(radius, Math.min(height - radius, d.y)); });        
    }); 


    //Variables und Functions for Highlighting
    var toggle = 0;
    var linkedByIndex = {};

    for(i = 0; i < graph.nodes.length; i++) {
      linkedByIndex[i + "," + i] = 1;
    };
        
    graph.links.forEach(function(d) {
      linkedByIndex[d.source.index + "," + d.target.index] = 1;
    });

    function neighboring(a, b) {
      return linkedByIndex[a.index + "," + b.index];
    }

    function connectedNodes() {
      if(toggle == 0) {
        d = d3.select(this).node().__data__;
        node.style("opacity", function (o) {
            return neighboring(d, o) | neighboring(o, d) ? 1 : 0.1;
        });
        link.style("opacity", function (o) {
            return d.index == o.source.index | d.index == o.target.index ? 1 : 0.1;
        });
        toggle = 1;
      } else {
        node.style("opacity", 1);
        link.style("opacity", 1);
        toggle = 0;
      }
    }
             
    //Functions for Dragging and Zooming   
    function zoomed() {
      container.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
    }

    function dragstarted(d) {
      d3.event.sourceEvent.stopPropagation();
      d3.select(this).classed("dragging", true);
      force.start();
    }

    function dragged(d) {
      d3.select(this).attr("cx", d.x = d3.event.x).attr("cy", d.y = d3.event.y);
    }

    function dragended(d) {
      d3.select(this).classed("dragging", false);
    }

    //Initialize Popovers
    $('[data-toggle="popover"]').popover({
        html: true,
        trigger: 'manual'
    })
    .on("mouseenter", function () {
      $(this).popover("show");

      $(".popover").on("mouseleave", function () {
        $(this).popover('hide');
      });
    })
    .on("mouseleave", function () {
        if(!$(".popover:hover").length) {
          $(this).popover("hide")
        }
    });

    //Calculate Graph Properties
    calculateProperties(graph.nodes,graph.links);
  });
}

function buildTable(div,query,options) {

  var html;
  var color = d3.scale.category10();

  //Build Head of the Table
  html = "<table width='100%' class='table table-striped table-hover'>";
  html += "<thead>";
  html += "<tr>";
  html += "<th>#</th>";
  html += "<th>Name</th>";
  html += "<th>Birth</th>";
  html += "<th>Death</th>";
  html += "<th>Value</th>";
  html += "<th><img src='http://icons.iconarchive.com/icons/sykonist/popular-sites/32/Wikipedia-icon.png' width='16'></th>";
  html += "</tr>";
  html += "</thead>";

  html += "<tbody>";

  //Get Data from Interface
  $.getJSON(query, function(data) {

    //Build Rows of the Table
    $.each(data.nodes, function(key,value) {

      html += "<tr>";
      html += "<td><span class='badge' style='background:" + color(value.group) + ";'>" + (key + 1) + "</span></td>";
      html += "<td>"+value.name.replace(/_/g," ") + "</td>";
      html += "<td>"+value.birth + "</td>";
      html += "<td>"+value.death + "</td>";
      html += "<td>"+value.value + "</td>";
      html += "<td><a href='http://" + options.language + ".wikipedia.org/wiki/" + value.name + "' target='_blank'>Link</a></td>";
      html += "</tr>";
    });

    html += "</tbody>";
    html += "</table>";

    //Calculate Graph Properties
    calculateProperties(data.nodes,data.links);

    //Add Table to Element
    $(div).html(html);
  });
}