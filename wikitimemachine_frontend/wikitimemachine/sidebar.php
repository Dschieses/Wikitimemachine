  
  <?php if (function_exists('dynamic_sidebar') && dynamic_sidebar('sidebar-widget-top')) : else : ?>
  <div class="panel panel-default">
    <div class="panel-heading">
      <h3 class="panel-title">Widgetized Area</h3>
    </div>
    <div class="panel-body">
      This panel is active and ready for you to add some widgets via the WP Admin
    </div>
  </div>
  <?php endif; ?>

  <?php if (function_exists('dynamic_sidebar') && dynamic_sidebar('sidebar-widget-bottom')) : else : ?>
  <div class="panel panel-default">
    <div class="panel-heading">
      <h3 class="panel-title">Widgetized Area</h3>
    </div>
    <div class="panel-body">
      This panel is active and ready for you to add some widgets via the WP Admin
    </div>
  </div>
  <?php endif; ?>