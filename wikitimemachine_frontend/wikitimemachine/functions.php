<?php

	define('CSSPATH', get_template_directory_uri().'/assets/css');
    define('IMAGEPATH', get_template_directory_uri().'/assets/images');
    define('JSPATH', get_template_directory_uri().'/assets/scripts');

    if(function_exists('register_nav_menus')) {
        register_nav_menus(array(
            'main-nav' => __('Main')
        ));
    }

	if (function_exists('register_sidebar')) {
		register_sidebar(array(
			'name' => 'Sidebar Widget Top',
			'id'   => 'sidebar-widget-top',
			'before_widget' => '<div class="panel panel-default">',
			'after_widget'  => '</div></div>',
			'before_title'  => '<div class="panel-heading"><h3 class="panel-title">',
			'after_title'   => '</h3></div><div class="panel-body">'
		));
		register_sidebar(array(
			'name' => 'Sidebar Widget Bottom',
			'id'   => 'sidebar-widget-bottom',
			'before_widget' => '<div class="panel panel-default">',
			'after_widget'  => '</div></div>',
			'before_title'  => '<div class="panel-heading"><h3 class="panel-title">',
			'after_title'   => '</h3></div><div class="panel-body">'
		));
	}