<?php
/*
Template Name: Template - Graph
*/
?>

<?php get_header(); ?>

    <!-- Start Wrapper -->
    <div class="container">
      <div class="row">
        <div class="col-sm-9">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title">Graph by Year</h3>
            </div>
            <ul class="list-group">
              <li class="list-group-item align-right no-border">
                <ul class="list-inline">
                <li>Nodes: <span id="nodesBadge" class="badge">0</span></li>
                <li>Links: <span id="linksBadge" class="badge">0</span></li>
                <li>Density: <span id="densityBadge" class="badge">0</span></li>
                </ul>   
             </li>
            </ul>
            <div id="graph" class="panel-body">
            </div>
            <div class="panel-footer">     
              <ul class="list-inline align-center" style="margin-bottom:0px;">
                <li><span class="badge decile decile-1">Decile 1</span></li>
                <li><span class="badge decile decile-2">Decile 2</span></li>
                <li><span class="badge decile decile-3">Decile 3</span></li>
                <li><span class="badge decile decile-4">Decile 4</span></li>
                <li><span class="badge decile decile-5">Decile 5</span></li>
                <li><span class="badge decile decile-6">Decile 6</span></li>
                <li><span class="badge decile decile-7">Decile 7</span></li>
                <li><span class="badge decile decile-8">Decile 8</span></li>
                <li><span class="badge decile decile-9">Decile 9</span></li>
                <li><span class="badge decile decile-10">Decile 10</span></li>
              </ul>   
            </div>
          </div>
        </div>

        <div class="col-sm-3">
          <div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">
            <div class="panel panel-default">
              <div class="panel-heading" role="tab" id="headingOne">
                <h4 class="panel-title">
                  <a data-toggle="collapse" data-parent="#accordion" href="#collapseOne" aria-expanded="true" aria-controls="collapseOne">
                    Basic Settings
                  </a>
                </h4>
              </div>
              <div id="collapseOne" class="panel-collapse collapse in" role="tabpanel" aria-labelledby="headingOne">
                <div class="panel-body">
                  <form role="form">
                    <div class="form-group">
                      <label>Language</label>
                      <select id="langSelect" class="form-control">
                      </select>
                    </div>
                    <div class="form-group">
                      <label>Category</label>
                      <select id="categorySelect" class="form-control" disabled="disable">
                      </select>
                    </div>
                    <hr>
                    <div class="form-group">
                      <label>Year</label> 
                      <span id="yearBadge" class="badge">0</span>
                      <div id="yearSlider"></div>
                    </div>
                  </form>
                </div>
              </div>
            </div>
            <div class="panel panel-default">
              <div class="panel-heading" role="tab" id="headingTwo">
                <h4 class="panel-title">
                  <a class="collapsed" data-toggle="collapse" data-parent="#accordion" href="#collapseTwo" aria-expanded="false" aria-controls="collapseTwo">
                    Extended Settings
                  </a>
                </h4>
              </div>
              <div id="collapseTwo" class="panel-collapse collapse" role="tabpanel" aria-labelledby="headingTwo">
                <div class="panel-body">
                  <form role="form">
                    <div class="form-group">
                      <label>Visualization</label>
                      <select id="visualizationSelect" class="form-control">
                        <option value="graph">Graph</option>
                        <option value="table">Table</option>
                      </select>
                    </div>
                    <hr>
                    <div class="form-group">
                      <label>Results</label> 
                      <span id="nodeBadge" class="badge">125</span>
                      <div id="limitSlider"></div>
                    </div>
                    <div class="form-group"> 
                      <label>Threshold</label> 
                      <span id="thresholdBadge" class="badge">0</span>
                      <div id="thresholdSlider"></div>
                    </div>
                    <hr>
                    <div class="form-group">
                      <label>Order</label>
                      <select id="orderSelect" class="form-control">
                        <option value="indegree">Indegree</option>
                        <option value="outdegree">Outdegree</option>
                        <option value="pagerank">PageRank</option>
                      </select>
                    </div>
                  </form>
                </div>
              </div>
            </div>
          </div>

          <button type="button" id="createGraph" class="btn btn-warning btn-lg btn-block">Calculate</button>

        </div>
      </div>
    </div>
    <!-- End Wrapper -->


<?php get_footer(); ?>