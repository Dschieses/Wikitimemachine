    <?php get_header(); ?>


    <?php if(get_children(array('post_parent' => $post->ID, 'post_status' => 'publish')) || $post->post_parent != 0) : ?>

    <!-- Start Page with Childs-->
    <div class="container">
      <div class="row">
        <div class="col-sm-9">
          <?php if (have_posts()) : while (have_posts()) : the_post(); ?>
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title"><?php the_title(); ?></h3>
            </div>
            <div class="panel-body">
              <?php the_content(); ?>
            </div>
          </div>            
          <?php endwhile; ?>
          <?php endif; ?>
        </div>

        <div class="col-sm-3">
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title">Submenu</h3>
            </div>
            <div class="panel-body">
              <ul class="list">
                <?php wp_list_pages(array('child_of' => $post->post_parent == 0 ? $post->ID : $post->post_parent, 'title_li' => '')); ?>
              </ul>
            </div>
          </div>
        </div>
      </div>
    </div>
    <!-- End Page with Childs-->

    <?php else : ?>

    <!-- Start Page without Childs -->
    <div class="container">
      <div class="row">
        <div class="col-sm-12">
          <?php if (have_posts()) : while (have_posts()) : the_post(); ?>
          <div class="panel panel-default">
            <div class="panel-heading">
              <h3 class="panel-title"><?php the_title(); ?></h3>
            </div>
            <div class="panel-body">
              <?php the_content(); ?>
            </div>
          </div>            
          <?php endwhile; ?>
          <?php endif; ?>
        </div>
      </div>
    </div>
    <!-- End Page without Childs -->

    <?php endif; ?>

   <?php get_footer(); ?>