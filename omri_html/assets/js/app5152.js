<!-- ================================================== -->

<!-- =============== BEGIN FIXED HEADER SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function(){ "use strict";



  function menuAffix() {



      var menuBeforeAffix = jQuery('body main > header .navbar').height();



      var scrollTop       = jQuery('body main > header .navbar').scrollTop();



      jQuery('body main > header').css('padding-top', menuBeforeAffix);



//      jQuery('header.top-slider .tp-banner-container').css('margin-top', - menuBeforeAffix);

      jQuery('header.top-slider').css('margin-top', - menuBeforeAffix);



      jQuery('.affix-menu').affix({

        offset: {

          top: scrollTop

        }

      });



  };



  jQuery('.affix-menu').each(function(){



    menuAffix();



  });



  jQuery(window).resize(function(){



    jQuery('.affix-menu').each(function(){



        menuAffix();



      });



  });

  jQuery('.menu-item-has-children > a').click(function(e){

    e.preventDefault();

  });



});

<!-- ================================================== -->

<!-- =============== END FIXED HEADER SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN SLIDER REVOLUTION SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function() { "use strict";

    jQuery(function(){

        jQuery('.tp-banner').revolution(

            {

                delay:9000,

                startwidth:1170,

                startheight:670,

                hideTimerBar:"on",

                hideThumbs:0,

                fullWidth:"on",

                forceFullWidth:"on"

            }

        );

    });

    jQuery(function(){

        jQuery('.tp-banner-800').revolution(

            {

                delay:9000,

                startwidth:1135,

                startheight:800,

                hideTimerBar:"on",

                hideThumbs:0

            }

        );

    });

    jQuery(function(){

        jQuery('.tp-banner-750').revolution(

            {

                delay:9000,

                startwidth:1135,

                startheight:750,

                hideTimerBar:"on",

                hideThumbs:0

            }

        );

    });

    jQuery(function(){

        jQuery('.tp-banner-890').revolution(

            {

                delay:9000,

                startwidth:1135,

                startheight:890,

                hideTimerBar:"on",

                hideThumbs:0

            }

        );

    });

});

<!-- ================================================== -->

<!-- =============== END SLIDER REVOLUTION SETTINGS ================ -->

<!-- ================================================== -->

    



<!-- ================================================== -->

<!-- =============== BEGIN PRETTYPHOTO SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function() { "use strict";



    jQuery("*[data-gal^='prettyPhoto']").prettyPhoto({hook: 'data-gal'});



});

<!-- ================================================== -->

<!-- =============== END PRETTYPHOTO SETTINGS ================ -->

<!-- ================================================== -->

    



<!-- ================================================== -->

<!-- =============== BEGIN FIXED FOOTER SETTINGS ================ -->

<!-- ================================================== -->

jQuery('body.fixed-footer').each(function(){ "use strict";



    var mb = jQuery('body .footer').outerHeight();



    jQuery(this).css("margin-bottom",mb)



});



jQuery(window).resize(function(){ "use strict";

    

    jQuery('body.fixed-footer').each(function(){



        var mb = jQuery('body .footer').outerHeight();



        jQuery(this).css("margin-bottom",mb)



    });

    

});

<!-- ================================================== -->

<!-- =============== END FIXED FOOTER SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN ROWSPAN SETTINGS ================ -->

<!-- ================================================== -->

jQuery('table.table_2 tbody > tr > td[rowspan="9"]').each(function(){ "use strict";



    var mt = jQuery('table.table_2 tbody > tr > td[rowspan="9"]').outerHeight();

    var mts = 10;

    var mtsS = mt - mts;



    jQuery('table.table_2 tbody > tr > td[rowspan="9"] > div.big_blue_cell').css("height", mtsS)



});



jQuery('table.table_2 tbody > tr > td[rowspan="9"]').resize(function(){ "use strict";



    var mt = jQuery('table.table_2 tbody > tr > td[rowspan="9"]').outerHeight();

    var mts = 10;

    var mtsS = mt - mts;



    jQuery('table.table_2 tbody > tr > td[rowspan="9"] > div.big_blue_cell').css("height", mtsS)



});







jQuery('table.table_2 tbody > tr > td[rowspan="2"]').each(function(){ "use strict";



    var mtt = jQuery('table.table_2 tbody > tr > td[rowspan="2"]').outerHeight();

    var mts = 10;

    var mtsSS = mtt - mts;



    jQuery('table.table_2 tbody > tr > td[rowspan="2"] > div.blue_cell_1').css("height", mtsSS)



});



jQuery('table.table_2 tbody > tr > td[rowspan="2"]').resize(function(){ "use strict";



    var mtt = jQuery('table.table_2 tbody > tr > td[rowspan="2"]').outerHeight();

    var mts = 10;

    var mtsSS = mtt - mts;



    jQuery('table.table_2 tbody > tr > td[rowspan="2"] > div.blue_cell_1').css("height", mtsSS)



});







jQuery('table.table_2 tbody > tr > td[rowspan="5"]').each(function(){ "use strict";



    var mttt = jQuery('table.table_2 tbody > tr > td[rowspan="5"]').outerHeight();

    var mts = 10;

    var mtsSSS = mttt - mts;



    jQuery('table.table_2 tbody > tr > td[rowspan="5"] > div.big_blue_cell_1').css("height", mtsSSS)



});



jQuery('table.table_2 tbody > tr > td[rowspan="5"]').resize(function(){ "use strict";



    var mttt = jQuery('table.table_2 tbody > tr > td[rowspan="5"]').outerHeight();

    var mts = 10;

    var mtsSSS = mttt - mts;



    jQuery('table.table_2 tbody > tr > td[rowspan="5"] > div.big_blue_cell_1').css("height", mtsSSS)



});







jQuery('table.table_2 tbody > tr > td').each(function(){ "use strict";



    var mtttt = jQuery('table.table_2 tbody > tr > td').outerHeight();

    var mts = 10;

    var mtsSSSS = mtttt - mts;



    jQuery('table.table_2 tbody > tr > td > div.blue_cell_2').css("height", mtsSSSS)



});



jQuery('table.table_2 tbody > tr > td').resize(function(){ "use strict";



    var mtttt = jQuery('table.table_2 tbody > tr > td').outerHeight();

    var mts = 10;

    var mtsSSSS = mtttt - mts;



    jQuery('table.table_2 tbody > tr > td > div.blue_cell_2').css("height", mtsSSSS)



});

<!-- ================================================== -->

<!-- =============== END ROWSPAN SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN OWL CAROUSEL SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function() { "use strict";

    jQuery('.departaments-owl_1 ul , .departaments-owl ul').owlCarousel({

        items:1,

        navText: false,

        responsive:{

          1600:{

            items:4,
            nav:true

          },

          1366:{

            items:3,
            nav:true

          },

          1024:{

            items:2

          },

          480:{

            items:1

          }

        }

    });

    jQuery('.owl-carousel').each(function(){

        jQuery("#owl-twitter").owlCarousel({

            items: 1

        });

        jQuery(".vlad-testimonial").owlCarousel({

            items : 1,

            margin : 15,

            navText : ['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],

            nav:true

        });

        jQuery("#owl-latest_news_1").owlCarousel({

            items : 1,

            margin : 15,

            responsive:{

              1199:{

                items:4

              },
              980:{

                items:4

              },
              768:{

                items:2

              },
              480:{

                items:1

              }

            },

            navText : ['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],

            nav:true

        });

        jQuery("#owl-latest_news_2").owlCarousel({

            // Most important owl features

            items : 1,

            margin : 15,

            responsive:{

              1199:{

                items:3

              },
              980:{

                items:3

              },
              768:{

                items:2

              },
              480:{

                items:1

              }

            },

            navText : ['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],

            nav:true

        });

        jQuery("#owl-doctors_1").owlCarousel({

            // Most important owl features

            items : 1,

            margin : 15,

            responsive:{

              1199:{

                items:3

              },
              980:{

                items:2

              },
              768:{

                items:2

              },
              480:{

                items:1

              }

            },

            navText : ['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],

            nav:true

        });

        jQuery("#owl-doctors_2").owlCarousel({

            items : 1,

            margin : 15,

            responsive:{

              1199:{

                items:4

              },
              980:{

                items:4

              },
              768:{

                items:2

              },
              480:{

                items:1

              }

            },

            navText : ['<i class="fa fa-angle-left"></i>','<i class="fa fa-angle-right"></i>'],

            nav:true

        });

    });

});

<!-- ================================================== -->

<!-- =============== END OWL CAROUSEL SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN VIDEO YOUTUBE SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function(){ "use strict";

    jQuery('.vdyt , .thumbnail , .isotope-item , .caption_news_c').fitVids();

});

<!-- ================================================== -->

<!-- =============== END VIDEO YOUTUBE SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN VIDEO BACKGROUND SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function(){ "use strict";

    jQuery(function(){

        jQuery(".player").mb_YTPlayer();

    });

});

<!-- ================================================== -->

<!-- =============== END VIDEO BACKGROUND SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN ROYAL SLIDER SETTINGS ================ -->

<!-- ================================================== -->

jQuery(function() { "use strict";

    jQuery('.rsMetrix').royalSlider({

        arrowsNavAutoHide: false,

        autoScaleSliderHeight: 425,

        thumbsFitInViewport: false,

        startSlideId: 0,

        globalCaption: false,

        autoScaleSlider: true,

        loop: false,

        navigateByClick: true,

        arrowsNav:true,

        arrowsNavHideOnTouch: true,

        controlNavigation: false,

        preloadNearbyImages:true,

        imageScalePadding: 0

    });



    jQuery('.rsMedic_1').royalSlider({

        arrowsNavAutoHide: false,

        autoScaleSliderHeight: 500,

        thumbsFitInViewport: false,

        startSlideId: 0,

        globalCaption: false,

        autoScaleSlider: true,

        loop: false,

        navigateByClick: true,

        arrowsNav:true,

        arrowsNavHideOnTouch: true,

        controlNavigation: false,

        preloadNearbyImages:true,

        imageScalePadding: 0

    });



    jQuery('.rsMedic').royalSlider({

        arrowsNavAutoHide: false,

        autoScaleSliderHeight: 425,

        thumbsFitInViewport: false,

        startSlideId: 0,

        globalCaption: false,

        autoScaleSlider: true,

        loop: false,

        navigateByClick: true,

        arrowsNav:true,

        arrowsNavHideOnTouch: true,

        controlNavigation: false,

        preloadNearbyImages:true,

        imageScalePadding: 0,

        autoPlay: {

          enabled: true,

          delay: 10000,

          pauseOnHover: true

        }

    });

});



<!-- ================================================== -->

<!-- =============== END ROYAL SLIDER SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== START ISOTOPE ================ -->

<!-- ================================================== -->

jQuery(document).ready(function(){ "use strict";



  function isotope() {



    var container = jQuery('.isotope-container');



    container.imagesLoaded(function() {

      container.isotope();

    });



    jQuery('.alternate-divs').imagesLoaded(function() {

      jQuery('.alternate-divs').isotope();

    });



    jQuery('.alternate-divs').isotope();



    container.isotope();



    var jQueryoptionSets = jQuery('.filter-isotope .option-set'),

        jQueryoptionLinks = jQueryoptionSets.find('a');



    jQueryoptionLinks.click(function(){

      var jQuerythis = jQuery(this);

      if ( jQuerythis.hasClass('selected') ) {

         return false;

      }

      var jQueryoptionSet = jQuerythis.parents('.option-set');

      jQueryoptionSet.find('.selected').removeClass('selected');

      jQuerythis.addClass('selected');



      var options = {},

          key = jQueryoptionSet.attr('data-option-key'),

          value = jQuerythis.attr('data-option-value');

      value = value === 'false' ? false : value;

      options[ key ] = value;

      if ( key === 'layoutMode' && typeof changeLayoutMode === 'function' ) {

        changeLayoutMode( jQuerythis, options )

      } else {

        container.isotope( options );

      }



        return false;

    });



    function hoverEvents() {



      jQuery(this).css('overflow','inherit')



    };



    function unHoverEvents() {



      jQuery(this).css('overflow','hidden')



    };



    container.hover(hoverEvents,unHoverEvents);



  };



  isotope();



  jQuery(window).resize(function(){



    isotope();



  });



});

<!-- ================================================== -->

<!-- =============== END ISOTOPE ================ -->

<!-- ================================================== -->



<!-- ================================================== -->

<!-- =============== START CIRCLES SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function(){ "use strict";

    jQuery('.pie-progress-charts').each(function(){

        //Circles 1

        Circles.create({

          id:         'circle-1',

          percentage: 75,

          radius:     110,

          width:      3,

          number:     75,

          text:       '%',

          colors:     ['#eaeaea', '#8cd5fa'],

          duration:   1000

        });



        //Circles 2

        Circles.create({

          id:         'circle-2',

          percentage: 31,

          radius:     110,

          width:      3,

          number:     31,

          text:       '%',

          colors:     ['#eaeaea', '#8cd5fa'],

          duration:   1000

        });



        //Circles 3

        Circles.create({

          id:         'circle-3',

          percentage: 23,

          radius:     110,

          width:      3,

          number:     23,

          text:       '%',

          colors:     ['#eaeaea', '#8cd5fa'],

          duration:   1000

        });



        //Circles 4

        Circles.create({

          id:         'circle-4',

          percentage: 86,

          radius:     110,

          width:      3,

          number:     86,

          text:       '%',

          colors:     ['#eaeaea', '#8cd5fa'],

          duration:   1000

        });

    });

});

<!-- ================================================== -->

<!-- =============== END CIRCLES SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN BEFORE AFTER SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function(){ "use strict";

    // function beforeAfterMedic() {

        var imgpath = jQuery('#contains').data('handle');

        jQuery('#contains').beforeAfter({

            imagePath : imgpath,

            animateIntro : true,

            introDelay : 2000,

            introDuration : 500,

            showFullLinks : false

        });

    // }

    // beforeAfterMedic();

    // jQuery(window).ready(function(){

    //     beforeAfterMedic();

    // });

});

<!-- ================================================== -->

<!-- =============== END BEFORE AFTER SETTINGS ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN TRIGGER TOP-NAV ================ -->

<!-- ================================================== -->

jQuery('ul.navi li').click(function(){ "use strict";

    jQuery('.top-nav').slideToggle();

    if(jQuery('ul.navi li a i').hasClass('fa-angle-down')) {

        jQuery('ul.navi li a i').removeClass('fa-angle-down');

        jQuery('ul.navi li a i').addClass('fa-angle-up');

    } else {

        jQuery('ul.navi li a i').removeClass('fa-angle-up');

        jQuery('ul.navi li a i').addClass('fa-angle-down');

    }

});

<!-- ================================================== -->

<!-- =============== END TRIGGER TOP-NAV ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN SELECTED ISOTOPE ================ -->

<!-- ================================================== -->

jQuery('.option-set').each(function( i, listGroup) { "use strict";

    var $listGroup = jQuery(listGroup);

        $listGroup.on('click', 'li' > 'a', function() {

        $listGroup.find('selected').removeClass('selected');

        jQuery(this).addClass('selected');

    });

});

<!-- ================================================== -->

<!-- =============== END SELECTED ISOTOPE ================ -->

<!-- ================================================== -->





<!-- ================================================== -->

<!-- =============== BEGIN TOOLTIP ISOTOPE ================ -->

<!-- ================================================== -->

jQuery(document).ready(function(){ "use strict";

     jQuery('.thumbnail.thumbnail_awesome_team > ul > li > a').tooltip({

        placement: 'top',

        html: 'true' 

    });

    jQuery('table.table_1 tbody > tr > td.toolt:first-child > div').tooltip({

        placement: 'top',

        html: 'true' 

    });

    jQuery('table.table_1 tbody > tr > td.toolt:nth-last-child(n+3) > div').tooltip({

        placement: 'right',

        html: 'true' 

    });

    jQuery('table.table_1 tbody > tr > td.toolt:nth-last-child(-n+2) > div').tooltip({

        placement: 'left',

        html: 'true' 

    });

    jQuery('table.table_2 tbody > tr > td.toolt:nth-last-child(n+3) > div').tooltip({

        placement: 'right',

        html: 'true' 

    });

    jQuery('table.table_2 tbody > tr > td.toolt:nth-last-child(-n+2) > div').tooltip({

        placement: 'left',

        html: 'true' 

    });

    jQuery('table.table_2 tbody > tr > td.toolt:nth-last-child(n+3) > div > div').tooltip({

        placement: 'right',

        html: 'true' 

    });

    jQuery('table.table_2 tbody > tr > td.toolt:nth-last-child(-n+2) > div > div').tooltip({

        placement: 'left',

        html: 'true' 

    });

});

<!-- ================================================== -->

<!-- =============== END TOOLTIP ISOTOPE ================ -->

<!-- ================================================== -->



<!-- ================================================== -->

<!-- =============== BEGIN SELECT SETTINGS ================ -->

<!-- ================================================== -->

jQuery(document).ready(function() { "use strict";



    jQuery('.drop-select').ddslick({



        onSelected: function(data){



            jQuery(data.selectedData.value).siblings().removeClass('active');



            jQuery(data.selectedData.value).addClass('active');



            if (jQuery(window).width() <= 768){

                jQuery(data.selectedData.value).siblings().removeClass('active');

                jQuery(data.selectedData.value + "_small").addClass('active');

            } else {

                jQuery(window).resize(function(){

                    if (jQuery(window).width() <= 768){

                        jQuery(data.selectedData.value).siblings().removeClass('active');

                        jQuery(data.selectedData.value + "_small").addClass('active');

                    }

                });

            }



            if (jQuery(window).width() >= 768){

                jQuery(data.selectedData.value).siblings().removeClass('active');

                jQuery(data.selectedData.value + "_small").addClass('active');

                jQuery(data.selectedData.value).addClass('active');

            } else {

                jQuery(window).resize(function(){

                    if (jQuery(window).width() >= 768){

                        jQuery(data.selectedData.value).siblings().removeClass('active');

                        jQuery(data.selectedData.value + "_small").addClass('active');

                        jQuery(data.selectedData.value).addClass('active');

                    }

                });

            }

        }

    });



    jQuery('.drop-select-1').ddslick({



        onSelected: function(data){



            jQuery(data.selectedData.value).siblings().removeClass('active');



            jQuery(data.selectedData.value).addClass('active');



            if (jQuery(window).width() <= 768){

                jQuery(data.selectedData.value).siblings().removeClass('active');

                jQuery(data.selectedData.value + "_small").addClass('active');

            } else {

                jQuery(window).resize(function(){

                    if (jQuery(window).width() <= 768){

                        jQuery(data.selectedData.value).siblings().removeClass('active');

                        jQuery(data.selectedData.value + "_small").addClass('active');

                    }

                });

            }



            if (jQuery(window).width() >= 768){

                jQuery(data.selectedData.value).siblings().removeClass('active');

                jQuery(data.selectedData.value + "_small").addClass('active');

                jQuery(data.selectedData.value).addClass('active');

            } else {

                jQuery(window).resize(function(){

                    if (jQuery(window).width() >= 768){

                        jQuery(data.selectedData.value).siblings().removeClass('active');

                        jQuery(data.selectedData.value + "_small").addClass('active');

                        jQuery(data.selectedData.value).addClass('active');

                    }

                });

            }

        }

    });

    jQuery('.drop-select-2').ddslick();

    jQuery('.drop-select-3').ddslick();



});

<!-- ================================================== -->

<!-- =============== END SELECT SETTINGS ================ -->

<!-- ================================================== -->