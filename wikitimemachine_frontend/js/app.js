  $(document).ready(function() {
    initGraph(1900);

    $("#slider").slider({
      min: 1900,
      max: 2030,
      slide: function(event, ui) {
        $("#yearLabel").html(ui.value);
      },
      stop: function(event, ui) {
        removeGraph();

        initGraph(ui.value);
      }
    });

    var force, svg, link, node;

    function initGraph(year) {
      var width = 700,
          height = 500;

      var color = d3.scale.category20();

      force = d3.layout.force()
          .charge(-120)
          .linkDistance(30)
          .size([width, height]);

      svg = d3.select("#graph").append("svg")
          .attr("width", width)
          .attr("height", height);

      d3.json("http://localhost/wiki/wtmInterface/index.php?year=" + year, function(error, graph) {
        force
            .nodes(graph.nodes)
            .links(graph.links)
            .start();

        link = svg.selectAll(".link")
            .data(graph.links)
          .enter().append("line")
            .attr("class", "link")
            .style("stroke-width", function(d) { return Math.sqrt(d.value); });

        node = svg.selectAll(".node")
            .data(graph.nodes)
          .enter().append("circle")
            .attr("class", "node")
            .attr("r", 5)
            .style("fill", function(d) { return color(d.group); })
            .call(force.drag);

        node.append("title")
            .text(function(d) { return d.name; });

        force.on("tick", function() {
          link.attr("x1", function(d) { return d.source.x; })
              .attr("y1", function(d) { return d.source.y; })
              .attr("x2", function(d) { return d.target.x; })
              .attr("y2", function(d) { return d.target.y; });

          node.attr("cx", function(d) { return d.x; })
              .attr("cy", function(d) { return d.y; });
        });
      });
    }

    function removeGraph() {
      node.remove();
      link.remove();
      svg.remove();
      nodes = [];
      links = [];     
    }
  });