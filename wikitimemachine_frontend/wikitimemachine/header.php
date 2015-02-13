<!DOCTYPE html >
  <head>
    <meta http-equiv="Content-Type" content="<?php bloginfo('html_type'); ?>; charset=<?php bloginfo('charset'); ?>" />
     
    <title><?php wp_title(); ?> - <?php bloginfo('name'); ?></title>
     
    <link rel="stylesheet" href="<?php bloginfo('stylesheet_url'); ?>" type="text/css" media="screen" />
    <link rel="stylesheet" href="<?php echo CSSPATH; ?>/jquery-ui.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="<?php echo CSSPATH; ?>/bootstrap.min.css" type="text/css" media="screen" />
    <link rel="stylesheet" href="<?php echo CSSPATH; ?>/custom.css" type="text/css" media="screen" />
     
    <?php wp_head(); ?>
  </head>

  <body>
    <!-- Start Navigation -->
    <nav class="navbar navbar-inverse navbar-fixed-top" role="navigation">
      <div class="container">
        <div class="navbar-header">
          <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar" aria-expanded="false" aria-controls="navbar">
            <span class="sr-only">Toggle navigation</span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
            <span class="icon-bar"></span>
          </button>
          <a class="navbar-brand" href="<?php echo home_url(); ?>">Wikitimemachine</a>
        </div>
        <div id="navbar" class="collapse navbar-collapse">
          <?php wp_nav_menu(array('theme_location' => 'main-nav', 'menu_class' => 'nav navbar-nav', 'depth' => 1)); ?>
        </div>
      </div>
    </nav>
    <!-- End Navigation -->

