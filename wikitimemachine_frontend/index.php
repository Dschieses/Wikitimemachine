    <?php get_header(); ?>

    <!-- Start Blog -->
    <div class="container">
      <div class="row">
        <div class="col-sm-9">
          <?php if (have_posts()) : while (have_posts()) : the_post(); ?>
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title"><a href="<?php the_permalink() ?>"><?php the_title(); ?></a></h3>
            </div>
            <div class="panel-body">
              <?php the_excerpt(); ?>
              <a href="<?php the_permalink() ?>">Read more...</a>
            </div>
          </div>            
          <?php endwhile; ?>
          <?php endif; ?>
        </div>

        <div class="col-sm-3">
          <?php get_sidebar(); ?>
        </div>
      </div>
    </div>
    <!-- End Blog -->

   <?php get_footer(); ?>