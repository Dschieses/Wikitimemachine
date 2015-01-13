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
    limit: 25
  }, options);

  colors = new Array(
    "#C6E2FF",
    "#B9D3EE",
    "#B9D3EE",
    "#9FB6CD",
    "#9FB6CD",
    "#6C7B8B",
    "#6C7B8B",
    "#708090",
    "#708090"
  );

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

  query += "http://localhost/wiki/wtmInterface/index.php";
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

  //Initialize Graph
  var width = $(div).width();
  var height = $(div).height();

  var color = d3.scale.category20();

  force = d3.layout.force()
    .linkStrength(0)
    .charge(-150)
    .linkDistance(60)
    .size([width, height]);

  //Get Data from Interface
  d3.json(query, function(error, graph) {
    
    //Clear Loader
    $(div).html("");

    svg = d3.select(div).append("svg")
      .attr("width", width)
      .attr("height", height);

    force
      .nodes(graph.nodes)
      .links(graph.links)
      .start();

    nodeList = graph.nodes;

    //Initialize Links
    link = svg
      .selectAll(".link")
      .data(graph.links)
      .enter()
      .append("line")
      .attr("class", "link")
      .style("marker-end",  "url(#suit)")
      .style("stroke-width", function(d) { return Math.sqrt(d.value); });

    //Initialize Nodes and add Attributes for Popover
    node = svg
      .selectAll(".node")
      .data(graph.nodes)
      .enter()
      .append("a")
      .attr("class", "personDetail")
      .attr("data-container", "body")
      .attr("data-toggle", "popover")
      .attr("data-placement", "top")
      .attr("title", function(d) { return name = d.name.replace(/_/g," "); })
      .append("circle")
      .attr("class","node")
      .attr("r", function(d) { return d.group + 3; })
      .style("opacity", function(d) { return d.group * 0.15; })
      .style("fill", function(d) { return color(d.group); })
      .call(force.drag)

    //Initialize Text
    text = svg
      .append("g")
      .selectAll("text")
      .data(force.nodes())
      .enter()
      .append("text")
      .attr("class", "textLabel")
      .text(function(d) { return name = d.name.replace(/_/g," "); });

    /*
    arrows = svg
      .append("defs")
      .selectAll("marker")
      .data(["suit", "licensing", "resolved"])
      .enter()
      .append("marker")
      .attr("id", function(d) { return d; })
      .attr("viewBox", "0 -5 10 10")
      .attr("refX", 25)
      .attr("refY", 0)
      .attr("markerWidth", 6)
      .attr("markerHeight", 6)
      .attr("orient", "auto")
      .append("path")
      .attr("d", "M0,-5L10,0L0,5 L10,0 L0, -5")
      .attr("class", "arrow")
      .style("stroke", "#999")
      .style("opacity", "1");
    */

    force.on("tick", function() {
      link.attr("x1", function(d) { return d.source.x; })
        .attr("y1", function(d) { return d.source.y; })
        .attr("x2", function(d) { return d.target.x; })
        .attr("y2", function(d) { return d.target.y; });

      node.attr("cx", function(d) { return d.x; })
        .attr("cy", function(d) { return d.y; });

      text.attr("x", function(d) { return d.x; })
        .attr("y", function(d) { return d.y; });
    });

    //Initialize Popovers
    $(".personDetail").click(function() {
      $(this).popover("show");

      //Set Timeout to prevent double fired Event
      setTimeout(function() { popover = true; }, 100);
    }); 

    $("body").click(function() {
      if(popover) {
        $(".personDetail[aria-describedby]").popover("destroy");
        popover = false;
      }
    });
 
    //Calculate Graph Properties
    calculateProperties(graph.nodes,graph.links)

  });
}

function buildTable(div,query,options) {

  var html;

  //Build Head of the Table
  html = "<table width='100%' class='table table-striped table-hover'>";
  html += "<thead>";
  html += "<tr>";
  html += "<th>#</th>";
  html += "<th>Name</th>";
  html += "<th>Birth</th>";
  html += "<th>Death</th>";
  html += "<th>Value</th>";
  html += "<th><img src='https://cdn2.iconfinder.com/data/icons/windows-8-metro-style/256/mind_map.png' width='16'></th>";
  html += "<th><img src='http://icons.iconarchive.com/icons/sykonist/popular-sites/32/Wikipedia-icon.png' width='16'></th>";
  html += "</tr>";
  html += "</thead>";

  html += "<tbody>";

  //Get Data from Interface
  $.getJSON(query, function(data) {

    //Build Rows of the Table
    $.each(data.nodes, function(key,value) {
      html += "<tr>";
      html += "<td>"+(key+1)+"</td>";
      html += "<td>"+value.name.replace(/_/g," ")+"</td>";
      html += "<td>"+value.birth+"</td>";
      html += "<td>"+value.death+"</td>";
      html += "<td>"+value.value+"</td>";
      html += "<td><a href='#person/"+value.id+"'>Link</a></td>";
      html += "<td><a href='http://"+options.language+".wikipedia.org/wiki/"+value.name+"' target='_blank'>Link</a></td>";
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